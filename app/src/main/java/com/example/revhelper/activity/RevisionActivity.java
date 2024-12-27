package com.example.revhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.revhelper.adapters.CoachAdapter;
import com.example.revhelper.databinding.ActivityRevisionBinding;
import com.example.revhelper.dto.CoachOnRevisionParce;
import com.example.revhelper.dto.CoachRepresentViewDto;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.mapper.CoachMapper;
import com.example.revhelper.model.CoachOnRevision;
import com.example.revhelper.model.Order;
import com.example.revhelper.model.Train;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RevisionActivity extends AppCompatActivity {

    private Map<String, CoachOnRevision> coachMap = new HashMap<>();
    private List<CoachRepresentViewDto> coaches = new ArrayList<>();
    private ActivityRevisionBinding binding;
    private CoachAdapter adapter;
    private Order order;
    private Train train;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRevisionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_revision);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Регистрация ActivityResultLauncher, cохранение результатов
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Получаем объект из результата
                        order = result.getData().getParcelableExtra("order");
                        CoachOnRevisionParce coach = result.getData().getParcelableExtra("coach");
                        if (coach != null) {
                            if (coaches.stream()
                                    .anyMatch(coachOld -> coachOld.getCoachNumber().equals(coach.getCoachNumber()))) {
                                coaches = coaches.stream()
                                        .filter(coachOld -> !coachOld.getCoachNumber().equals(coach.getCoachNumber()))
                                        .collect(Collectors.toList());
                            }
                            coaches.add(new CoachRepresentViewDto(coach.getCoachNumber(), coach.getCoachWorker()));
                            updateRecyclerView(coaches); //обновляем RecyclerView
                            coachMap.put(coach.getCoachNumber(), new CoachOnRevision(coach.getCoachNumber(),
                                    coach.getCoachWorker(),
                                    coach.isCoachSkudopp(),
                                    coach.isCoachAutomaticDoor(),
                                    LocalDateTime.now()));
                        }
                    }
                }
        );

        // Установить LayoutManager
        // Установить адаптер
        RecyclerView rView = binding.coachRecyclerView;
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new CoachAdapter(this, coaches, coach -> {
            Intent intent = new Intent(RevisionActivity.this, CoachActivity.class);
            if (coachMap.containsKey(coach.getCoachNumber())) {
                CoachOnRevisionParce coachForSend = CoachMapper.fromCoachOnRevisionToParcelable(coachMap.get(coach.getCoachNumber()));
                intent.putExtra("coach", coachForSend);
                launcher.launch(intent);
            } else {
                Toast.makeText(RevisionActivity.this, "Some troubles", Toast.LENGTH_SHORT).show();
            }
        });
        rView.setAdapter(adapter);

        //Нажатие кнопки для вызова SecondActivity
        binding.addCoach.setOnClickListener(v -> {
            Intent intent = new Intent(RevisionActivity.this, CoachActivity.class);
            launcher.launch(intent);
        });

        binding.bckImageButton.setOnClickListener((v -> {

            DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
            dialog.show(getSupportFragmentManager(),"dialog");
            //finish();
        }));

        binding.makeResultButton.setOnClickListener((v -> {

        }));

    }

    public void closeActivity() {
        finish();
    }

    private void updateRecyclerView(List<CoachRepresentViewDto> updatedList) {
        //Обновляем адаптер RecyclerView
        if (adapter != null) {
            adapter.updateData(updatedList);
        }
    }
}