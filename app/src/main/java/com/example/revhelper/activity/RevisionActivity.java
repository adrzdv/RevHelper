package com.example.revhelper.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.revhelper.adapters.CoachAdapter;
import com.example.revhelper.databinding.ActivityRevisionBinding;
import com.example.revhelper.dto.CoachOnRevisionParce;
import com.example.revhelper.dto.CoachRepresentViewDto;
import com.example.revhelper.dto.OrderParcelable;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.mapper.CoachMapper;
import com.example.revhelper.mapper.OrderMapper;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.CoachOnRevision;
import com.example.revhelper.model.MainNodesEnum;
import com.example.revhelper.model.Order;
import com.example.revhelper.model.Violation;
import com.example.revhelper.model.ViolationForCoach;
import com.example.revhelper.sys.AppRev;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@SuppressLint("NewApi")
public class RevisionActivity extends AppCompatActivity {

    private Map<String, CoachOnRevision> coachMap = new HashMap<>();
    private List<CoachRepresentViewDto> coaches = new ArrayList<>();
    private ActivityRevisionBinding binding;
    private CoachAdapter adapter;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRevisionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (order != null) {
            binding.orderTextView.setText(getOrderToShow(order));
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        // Регистрация ActivityResultLauncher, cохранение результатов
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Получаем объект из результата
                        OrderParcelable orderParce = result.getData().getParcelableExtra("order");
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
                                    coach.isCoachEnergySystem(),
                                    coach.isCoachProgressive(),
                                    LocalDateTime.now(),
                                    coach.getViolationList().stream()
                                            .map(ViolationMapper::fromParceToCoach)
                                            .collect(Collectors.toList())));
                        } else if (orderParce != null) {
                            order = OrderMapper.fromParcelableToOrder(orderParce);
                            binding.orderTextView.setText(getOrderToShow(order));
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
        }, coach -> { // Обработка удаления вагона
            coachMap.remove(coach.getCoachNumber());
        });
        rView.setAdapter(adapter);

        //Нажатие кнопки для вызова SecondActivity
        binding.addCoach.setOnClickListener(v -> {
            if (order == null) {
                Toast.makeText(RevisionActivity.this, "Внесите уведомление", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(RevisionActivity.this, CoachActivity.class);
            launcher.launch(intent);
        });

        binding.bckImageButton.setOnClickListener((v -> {

            DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
            dialog.show(getSupportFragmentManager(), "dialog");
        }));

        binding.makeResultButton.setOnClickListener((v -> {
            Map<String, String> resMap = makeRevisionResult();
            Intent intent = new Intent(RevisionActivity.this, ResultActivity.class);
            intent.putExtra("resMap", (Serializable) resMap);
            startActivity(intent);

        }));

        binding.orderButton.setOnClickListener((v -> {
            Intent intent = new Intent(RevisionActivity.this, OrderActivity.class);
            launcher.launch(intent);
        }));

    }

    private Map<String, String> makeRevisionResult() {
        Map<String, String> resMap = new HashMap<>();

        int total = 0;

        for (CoachOnRevision coach : coachMap.values()) {

            total = total + coach.getViolationList().stream().mapToInt(ViolationForCoach::getAmount).sum();

        }

        resMap.put("TOTAL", String.valueOf(total));

        resMap.put(MainNodesEnum.AUTO_DOOR.name(), String.valueOf(coachMap.values().stream()
                .filter(CoachOnRevision::isCoachAutomaticDoor)
                .count()));
        resMap.put(MainNodesEnum.SKUDOPP.name(), String.valueOf(coachMap.values().stream()
                .filter(CoachOnRevision::isCoachSkudopp)
                .count()));

        StringBuilder progressiveCoaches = new StringBuilder();

        List<String> coachesNmbrs = coachMap.values().stream()
                .filter(CoachOnRevision::isCoachProgressive)
                .map(CoachOnRevision::getCoachNumber)
                .collect(Collectors.toList());

        for (String coachNumber : coachesNmbrs) {
            progressiveCoaches.append(coachNumber).append(" ");
        }

        resMap.put(MainNodesEnum.PROGRESS.name(), progressiveCoaches.toString());

        List<ViolationForCoach> violationList = AppRev.getDb().violationDao().getAllViolations().stream()
                .map(ViolationMapper::fromEntityToForCouch)
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        for (ViolationForCoach violation : violationList) {
            StringBuilder coaches = new StringBuilder();
            for (CoachOnRevision coach : coachMap.values()) {
                if (coach.getViolationList().contains(violation)) {
                    coaches.append(coach.getCoachNumber())
                            .append(" ")
                            .append(coach.getCoachWorker())
                            .append(" ")
                            .append(coach.getRevisionTime().format(formatter))
                            .append("\n");
                    resMap.put(violation.getName(), coaches.toString());
                }
            }
        }

        return resMap;
    }

    private void updateRecyclerView(List<CoachRepresentViewDto> updatedList) {
        //Обновляем адаптер RecyclerView
        if (adapter != null) {
            adapter.updateData(updatedList);
        }
    }

    private static StringBuilder getOrderToShow(Order order) {

        StringBuilder res = new StringBuilder();

        res = res.append("Поезд: " + order.getTrain().getNumber() + "\n" + "Сообщение: " + order.getTrain().getRoute() + "\n");
        res.append("Формирование: " + order.getTrain().getDepName() + ", " + order.getTrain().getBranchName() + "\n");
        if (order.getTrain().getHasProgressive() == 1) {
            res.append("Прогрессивные нормы: ДА\n");
        } else {
            res.append("Прогрессивные нормы: НЕТ\n");
        }

        if (order.getTrain().getHasRegistrator() == 1) {
            res.append("Видерегистратор: ДА\n");
        } else {
            res.append("Видерегистратор: ДА\n");
        }

        if (order.getTrain().getHasPortal() == 1) {
            res.append("Попутчик: ДА\n");
        } else {
            res.append("Попутчик: НЕТ\n");
        }

        res.append("ЛНП: " + order.getDirectors().get("Начальник поезда") + "\n");

        if (order.getDirectors().containsKey("ПЭМ")) {
            res.append("ПЭМ: " + order.getDirectors().get("ПЭМ") + "\n");
        }
        if (order.getDirectors().containsKey("ДВР")) {
            res.append("ДВР: " + order.getDirectors().get("ДВР") + "\n");
        }
        if (order.getDirectors().containsKey("Менеджер ВБ")) {
            res.append("ЛНП: " + order.getDirectors().get("Менеджер ВБ") + "\n");
        }

        return res;
    }
}