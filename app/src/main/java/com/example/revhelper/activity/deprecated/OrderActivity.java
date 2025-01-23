package com.example.revhelper.activity.deprecated;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revhelper.R;
import com.example.revhelper.adapters.CoachSingleAdapter;
import com.example.revhelper.fragments.AddCoachInOrderActivity;
import com.example.revhelper.databinding.ActivityOrderBinding;
import com.example.revhelper.mapper.TrainMapper;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.deprecated.OrderParcelable;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.Train;
import com.example.revhelper.model.dto.TrainDto;
import com.example.revhelper.model.enums.RevisionType;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Deprecated
@SuppressLint({"DefaultLocale", "NewApi"})
public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private AppDatabase appDb;
    private OrderParcelable order = new OrderParcelable();
    private TrainDto train;
    private Coach mainCoach;
    private CoachSingleAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private List<CoachOnRevision> coachList = new ArrayList<>();
    private Map<String, CoachOnRevision> coachMap = new HashMap<>();
    private final LocalDateTime now = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        appDb = AppRev.getDb();

        List<Train> trainList = appDb.trainDao().getAllTrains();
        List<Coach> mainCoachList = appDb.coachDao().getAllCoaches();

        adapter = new CoachSingleAdapter(this, coachList, coach -> {
            coachMap.remove(coach.getCoachNumber());
            coachList.remove(coach);
        });

        RecyclerView rCoachView = binding.coachRecyclerView;

        rCoachView.setAdapter(adapter);
        rCoachView.setLayoutManager(new LinearLayoutManager(this));
        rCoachView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] revisionTypes = {RevisionType.IN_TRANSIT.getRevisionTypeTitle(),
                RevisionType.AT_START_POINT.getRevisionTypeTitle(),
                RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle()};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, revisionTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.revisionTypeSpinner.setAdapter(arrayAdapter);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String worker = result.getData().getStringExtra("WORKER");
                        String workerData = result.getData().getStringExtra("SURNAME");
                        String coachNumber = result.getData().getStringExtra("COACH");
                        if (worker != null) {
                            order.getDirectors().put(worker, workerData);
                            StringBuilder workerForView = new StringBuilder();
                            for (String keyWorker : order.getDirectors().keySet()) {
                                workerForView.append(keyWorker).append(": ").append(order.getDirectors().get(keyWorker)).append("\n");
                                binding.trainInfoTextView.setText(workerForView);
                            }
                        }
                        if (coachNumber != null) {
                            CoachOnRevision newCoach = new CoachOnRevision.Builder().setCoachNumber(coachNumber)
                                    .setRevisionTime(now)
                                    .build();
                            coachMap.put(coachNumber, newCoach);
                            if (!coachList.contains(newCoach)) {
                                coachList.add(newCoach);
                            }
                            updateRecyclerView(coachList);
                        }
                    }
                }
        );

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        if (getIntent().getParcelableExtra("ORDER") != null) {
            order = getIntent().getParcelableExtra("ORDER");
            train = TrainMapper.fromParcelableToDto(order.getTrain());
            coachMap = order.getCoachMap();
            coachList = new ArrayList<>(coachMap.values());
            binding.orderNumberTextInput.setText(order.getNumber());
            binding.routeTextInput.setText(order.getRoute());
            LocalDate date = LocalDate.of(order.getDate().getYear(), order.getDate().getMonth(), order.getDate().getDayOfMonth());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = date.format(formatter);
            binding.orderDateTextInput.setText(formattedDate);
            binding.trainAutocompliteTextView.setText(order.getTrain().getNumber());
            updateRecyclerView(coachList);
            binding.addCoach.setEnabled(false);
            binding.addCoach.setAlpha(0.5f);
            binding.cleanList.setEnabled(false);
            binding.cleanList.setAlpha(0.5f);
        }

        binding.cleanList.setOnClickListener((v -> {
            coachList.clear();
            coachMap.clear();
            updateRecyclerView(coachList);
            Toast toast = Toast.makeText(this, "Список вагонов очищен",
                    Toast.LENGTH_LONG);
            toast.show();
        }));

        binding.addCoach.setOnClickListener((v -> {
            Intent intent = new Intent(OrderActivity.this, AddCoachInOrderActivity.class);
            launcher.launch(intent);
        }));

        binding.orderDateTextInput.setOnClickListener((v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (View, year1, month1, dayOfMonth) -> {
                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                binding.orderDateTextInput.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        }));

        binding.bckImageButton.setOnClickListener((v -> {
            DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
            dialog.show(getSupportFragmentManager(), "dialog");
        }));

        ArrayAdapter<Train> trainArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, trainList);
        binding.trainAutocompliteTextView.setAdapter(trainArrayAdapter);
        ArrayAdapter<Coach> coachArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mainCoachList);
        binding.coachAutocompliteTextView.setAdapter(coachArrayAdapter);

        binding.coachAutocompliteTextView.setOnItemClickListener((parent, View, position, id) -> {
            Coach selectedCoach = coachArrayAdapter.getItem(position);
            if (selectedCoach != null) {
                mainCoach = appDb.coachDao().findByCoach(selectedCoach.getCoachNumber());
            }
        });

        binding.trainAutocompliteTextView.setOnItemClickListener((parent, View, position, id) -> {
            Train selectedTrain = trainArrayAdapter.getItem(position);
            if (selectedTrain != null) {
                train = appDb.trainDao().findByNumberWithDep(selectedTrain.getNumber());
            }
        });

        binding.addTrainTeamButton.setOnClickListener((v -> {
            Intent intent = new Intent(OrderActivity.this, AddMasterActivity.class);
            launcher.launch(intent);
        }));

        binding.saveOrder.setOnClickListener(this::saveOrder);

    }

    private void saveOrder(View v) {

        if (binding.orderNumberTextInput.getText() == null) {
            Toast toast = Toast.makeText(this, "Не заполнен номер",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (order.getDirectors().isEmpty()) {
            Toast toast = Toast.makeText(this, "Не заполнена поедная бригада",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (binding.orderDateTextInput.getText().toString().isBlank()) {
            Toast toast = Toast.makeText(this, "Не заполнена дата",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (train == null) {
            Toast toast = Toast.makeText(this, "Не выбран поезд",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        String orderNumber = Objects.requireNonNull(binding.orderNumberTextInput.getText()).toString();
        String orderDate = Objects.requireNonNull(binding.orderDateTextInput.getText()).toString();
        String route = Objects.requireNonNull(binding.routeTextInput.getText()).toString();

        order.setNumber(orderNumber);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        order.setDate(LocalDate.parse(orderDate, formatter));
        order.setRoute(route);
        order.setCoachMap(coachMap);
        order.setTrain(TrainMapper.toParceFromDto(train));
        order.setRevisionType(binding.revisionTypeSpinner.getSelectedItem().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("order", order);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void updateRecyclerView(List<CoachOnRevision> updatedCoachList) {
        if (adapter != null) {
            adapter.updateData(updatedCoachList);
        }
    }
}