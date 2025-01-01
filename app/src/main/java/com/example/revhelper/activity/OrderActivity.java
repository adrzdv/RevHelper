package com.example.revhelper.activity;

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

import com.example.revhelper.R;
import com.example.revhelper.adapters.ViolationAdapter;
import com.example.revhelper.databinding.ActivityOrderBinding;
import com.example.revhelper.dto.OrderParcelable;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.mapper.OrderMapper;
import com.example.revhelper.model.Order;
import com.example.revhelper.model.Train;
import com.example.revhelper.model.TrainDto;
import com.example.revhelper.model.Violation;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressLint({"DefaultLocale", "NewApi"})
public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private AppDatabase appDb;
    private ViolationAdapter violationAdapter;
    private List<Violation> violationNameList;
    private Order order = new Order();
    private TrainDto train;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        appDb = AppRev.getDb();
        violationAdapter = new ViolationAdapter(violationNameList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String worker = result.getData().getStringExtra("WORKER");
                        String workerData = result.getData().getStringExtra("SURNAME");
                        if (!worker.isBlank()) {
                            order.getDirectors().put(worker, workerData);
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

        List<Train> trainList = appDb.trainDao().getAllTrains();

        ArrayAdapter<Train> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, trainList);
        binding.trainAutocompliteTextView.setAdapter(adapter);

        binding.trainAutocompliteTextView.setOnItemClickListener((parent, View, position, id) -> {
            Train selectedTrain = adapter.getItem(position);
            binding.trainInfoTextView.setText(selectedTrain.toString());
            train = appDb.trainDao().findByNumberWithDep(selectedTrain.getNumber());
        });

        binding.addTrainTeamButton.setOnClickListener((v -> {
            Intent intent = new Intent(OrderActivity.this, AddMasterActivity.class);
            launcher.launch(intent);
        }));

        binding.saveOrder.setOnClickListener((v -> {

            if (binding.orderNumberTextInput.getText() == null) {
                Toast toast = Toast.makeText(this, "Не заполнен номер",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            if (binding.orderDateTextInput.getText() == null) {
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

            order.setNumber(orderNumber);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            order.setDate(LocalDate.parse(orderDate, formatter));
            order.setRoute("null");
            order.setTrain(train);
            OrderParcelable orderParce = OrderMapper.fromOrderToParcelable(order);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("order", orderParce);
            setResult(RESULT_OK, resultIntent);
            finish();
        }));

    }
}