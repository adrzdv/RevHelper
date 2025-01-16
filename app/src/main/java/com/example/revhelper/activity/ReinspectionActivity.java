package com.example.revhelper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.example.revhelper.adapters.CoachSingleAdapterWithoutButton;
import com.example.revhelper.databinding.ActivityReinspectionBinding;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.services.ParseTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReinspectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Map<String, CoachOnRevision> existingCoachMap = new HashMap<>();
    private Map<String, CoachOnRevision> previousInspectionCoachMap;
    private List<CoachOnRevision> coachNumbers = new ArrayList<>();
    private List<CoachOnRevision> enteredCoachNumbers = new ArrayList<>();
    private ActivityReinspectionBinding binding;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private ActivityResultLauncher<Intent> launcher;
    private CoachSingleAdapterWithoutButton adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityReinspectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String coachNumber = result.getData().getStringExtra("COACH");
                        List<CoachOnRevision> resCoachList = result.getData().getParcelableArrayListExtra("NEW_COACHES");

                        if (coachNumber != null) {
                            if (previousInspectionCoachMap.containsKey(coachNumber)) {
                                if (!coachNumbers.contains(coachNumber)) {
                                    coachNumbers.add(previousInspectionCoachMap.get(coachNumber));
                                }
                                existingCoachMap.put(coachNumber, previousInspectionCoachMap.get(coachNumber));
                                Toast toast = Toast.makeText(this, coachNumber + " добавлен",
                                        Toast.LENGTH_LONG);
                                toast.show();
                                updateRecyclerView(coachNumbers);
                            } else {
                                Toast toast = Toast.makeText(this, coachNumber + " не найден в прошлой проверке",
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        if (resCoachList != null) {
                            coachNumbers = resCoachList;
                            Map<String, CoachOnRevision> oldCoachMap = new HashMap<>(existingCoachMap);
                            existingCoachMap = oldCoachMap.entrySet()
                                    .stream()
                                    .filter(entry -> coachNumbers.contains(entry.getValue()))
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                            updateRecyclerView(coachNumbers);
                        }
                    }
                }
        );

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            previousInspectionCoachMap = readFile(fileUri);
                            Toast toast = Toast.makeText(this, "Данные загружены",
                                    Toast.LENGTH_LONG);
                            toast.show();
                            StringBuilder docString = new StringBuilder();
                            docString.append("НОМЕР АКТА: ").append(ParseTable.getDocNumber()).append("\n")
                                    .append("ДАТА АКТА: ").append(ParseTable.getDocDate());
                            makeReinspection();
                            binding.numberDateTextView.setText(docString);
                        }
                    }
                }
        );

        adapter = new CoachSingleAdapterWithoutButton(this, coachNumbers, coach -> {
            if (coach.getViolationList() != null && !coach.getViolationList().isEmpty()) {
                Intent intent = new Intent(ReinspectionActivity.this, ReinspectionCoachView.class);
                intent.putExtra("COACH", coach);
                launcher.launch(intent);
            } else {
                Toast toast = Toast.makeText(this, "Список нарушений пуст",
                        Toast.LENGTH_LONG);
                toast.show();
            }

        });

        RecyclerView rCoachView = binding.coachRecyclerViewReinspection;

        rCoachView.setAdapter(adapter);
        rCoachView.setLayoutManager(new LinearLayoutManager(this));
        rCoachView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        binding.loadReinspectionFile.setOnClickListener(this);
        binding.startReinspection.setOnClickListener(this);
        binding.addExistingCoach.setOnClickListener(this);
        binding.showEnteredCoachList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.load_reinspection_file) {
            getData();
        } else if (v.getId() == R.id.start_reinspection) {
            makeReinspection();
        } else if (v.getId() == R.id.add_existing_coach) {
            addCoach();
        } else if (v.getId() == R.id.show_entered_coach_list) {
            showEnteredCoachList();
        }

    }

    private void showEnteredCoachList() {
        Intent intent = new Intent(ReinspectionActivity.this, ShowEnteredCoachesActivity.class);
        ArrayList<CoachOnRevision> list = new ArrayList<>(existingCoachMap.values());
        intent.putParcelableArrayListExtra("COACHES", list);
        launcher.launch(intent);
    }

    private void addCoach() {
        if (previousInspectionCoachMap == null) {
            Toast toast = Toast.makeText(this, "Сначала загрузите данные",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent(ReinspectionActivity.this, AddCoachInOrderActivity.class);
        launcher.launch(intent);
    }

    private void makeReinspection() {

        //coachNumbers.clear();
        //updateRecyclerView(coachNumbers);
        coachNumbers.addAll(existingCoachMap.values());
        updateRecyclerView(coachNumbers);
    }

    private void getData() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            filePickerLauncher.launch(Intent.createChooser(intent, "Выберите файл"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Установите файловый менеджер.", Toast.LENGTH_SHORT).show();
        }

    }

    private Map<String, CoachOnRevision> readFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            if (inputStream != null) {
                return ParseTable.readExcel(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void updateRecyclerView(List<CoachOnRevision> updatedCoachList) {
        if (adapter != null) {
            adapter.updateData(updatedCoachList);
        }
    }
}