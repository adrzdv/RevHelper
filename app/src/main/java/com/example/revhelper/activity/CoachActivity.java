package com.example.revhelper.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.revhelper.adapters.ViolationAdapter;
import com.example.revhelper.databinding.ActivityCoachBinding;
import com.example.revhelper.dto.CoachOnRevisionParce;
import com.example.revhelper.dto.ViolationDtoParce;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.mapper.CoachMapper;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.CoachOnRevision;
import com.example.revhelper.model.MainNodes;
import com.example.revhelper.model.Violation;
import com.example.revhelper.model.ViolationForCoach;
import com.example.revhelper.services.CheckService;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressLint("NewApi")
public class CoachActivity extends AppCompatActivity {

    private final CheckService checkService = new CheckService();
    private ActivityResultLauncher<Intent> launcher;
    private ActivityCoachBinding binding;
    private AppDatabase appDb;
    private List<MainNodes> mainNodesList;
    private ViolationAdapter adapter;
    private List<ViolationForCoach> violationList = new ArrayList<>();
    private final LocalDateTime revStart = LocalDateTime.now();
    private final DateTimeFormatter formatterToShow = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    Map<String, Boolean> mapCoachNodesValue = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCoachBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        appDb = AppRev.getDb();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //ловим жест на случай возврата
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
                dialog.show(getSupportFragmentManager(), "dialog");

            }
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ViolationDtoParce violationParce = result.getData().getParcelableExtra("violation");
                        if (violationParce != null) {
                            Violation violation = ViolationMapper.fromParceToEntity(violationParce);
                            ViolationForCoach violationForCoach = ViolationMapper.fromEntityToForCouch(violation);
                            if (violationList.contains(violationForCoach)) {
                                Toast toast = Toast.makeText(this, "Нарушение уже добавлено",
                                        Toast.LENGTH_LONG);
                                toast.show();
                                return;
                            }

                            violationList.add(violationForCoach);
                            updateRecyclerView(violationList);
                        }
                    }
                }
        );

        RecyclerView rView = binding.violationListRecycler;
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ViolationAdapter(this, violationList);
        rView.setAdapter(adapter);

        mainNodesList = appDb.mainNodesDao().getMainNodesList();

        String[] nodeTypes = mainNodesList.stream()
                .map(MainNodes::getName)
                .toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nodeTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMainNodes.setAdapter(adapter);
        String revStartString = "";

        //Тут на забыть, что если открываем для редактирования вагон - надо все поля вывести
        if (getIntent().getParcelableExtra("coach") != null) {
            CoachOnRevisionParce coach = getIntent().getParcelableExtra("coach");
            revStartString = coach.getRevisionTime().format(formatterToShow);
            binding.coachNumber.setText(coach.getCoachNumber());
            binding.coachWorker.setText(coach.getCoachWorker());
            mapCoachNodesValue.put(mainNodesList.get(0).getName(), coach.isCoachEnergySystem());
            mapCoachNodesValue.put(mainNodesList.get(1).getName(), coach.isCoachSkudopp());
            mapCoachNodesValue.put(mainNodesList.get(2).getName(), coach.isCoachAutomaticDoor());
            mapCoachNodesValue.put(mainNodesList.get(3).getName(), coach.isCoachProgressive());
            violationList.addAll(coach.getViolationList().stream()
                    .map(ViolationMapper::fromParceToCoach)
                    .collect(Collectors.toList()));

            binding.showNodesTextRes.setText(new StringBuilder().append("Начало проверки: ")
                    .append(revStartString).append('\n')
                    .append(getMainNodesStatesToShow(coach.isCoachEnergySystem(),
                            coach.isCoachSkudopp(), coach.isCoachAutomaticDoor(), coach.isCoachProgressive()))
                    .toString());

        } else {
            revStartString = revStart.format(formatterToShow);
            binding.showNodesTextRes.setText("Начало проверки: " + revStartString);
        }

        binding.saveCoachInfo.setOnClickListener(v -> {
            // Читаем данныe
            String coachNumber = Objects.requireNonNull(binding.coachNumber.getText()).toString();
            String coachWorker = Objects.requireNonNull(binding.coachWorker.getText()).toString();

            if (mapCoachNodesValue.size() < mainNodesList.size()) {
                Toast toast = Toast.makeText(this, "Не заполнена информация по основным узлам",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            if (coachNumber.isEmpty() || coachWorker.isEmpty()) {
                Toast toast = Toast.makeText(this, "ФИО или номер вагона пустые",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            if (!checkService.checkCoachRegex(coachNumber)) {
                Toast toast = Toast.makeText(this, "Неверный формат номера вагона",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            // Создаём объект

            CoachOnRevision coachOnRevision = new CoachOnRevision(coachNumber, coachWorker,
                    mapCoachNodesValue.get(mainNodesList.get(1).getName()),
                    mapCoachNodesValue.get(mainNodesList.get(2).getName()),
                    mapCoachNodesValue.get(mainNodesList.get(0).getName()),
                    mapCoachNodesValue.get(mainNodesList.get(3).getName()),
                    revStart,
                    violationList);

            // Возвращаем объект в предыдущую активити
            Intent resultIntent = new Intent();
            resultIntent.putExtra("coach", CoachMapper.fromCoachOnRevisionToParcelable(coachOnRevision));
            setResult(RESULT_OK, resultIntent);
            // Закрываем SecondActivity
            finish();
        });

        binding.bckImageButton.setOnClickListener((v -> {

            DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
            dialog.show(getSupportFragmentManager(), "dialog");
        }));

        binding.saveNodeParam.setOnClickListener((v -> {

            String mainNodeName = binding.spinnerMainNodes.getSelectedItem().toString();
            int selectedNodeStatusId = binding.radioGroupStatus.getCheckedRadioButtonId();
            boolean statusNode = selectedNodeStatusId == binding.radioGood.getId();
            boolean isSkudoppAvailable;
            boolean isAutomaticDoorsAvailable;
            boolean isEnergySystemNodeAvailable;
            boolean isProgressive;

            if (mainNodeName.equals(mainNodesList.get(0).getName())) {
                isEnergySystemNodeAvailable = statusNode;
                mapCoachNodesValue.put(mainNodesList.get(0).getName(), isEnergySystemNodeAvailable);
                Toast toast = Toast.makeText(this, mainNodesList.get(0).getName(),
                        Toast.LENGTH_LONG);
                toast.show();
            }
            if (mainNodeName.equals(mainNodesList.get(1).getName())) {
                isSkudoppAvailable = statusNode;
                mapCoachNodesValue.put(mainNodesList.get(1).getName(), isSkudoppAvailable);
                Toast toast = Toast.makeText(this, mainNodesList.get(1).getName(),
                        Toast.LENGTH_LONG);
                toast.show();
            }

            if (mainNodeName.equals(mainNodesList.get(2).getName())) {
                isAutomaticDoorsAvailable = statusNode;
                mapCoachNodesValue.put(mainNodesList.get(2).getName(), isAutomaticDoorsAvailable);
                Toast toast = Toast.makeText(this, mainNodesList.get(2).getName(),
                        Toast.LENGTH_LONG);
                toast.show();
            }

            if (mainNodeName.equals(mainNodesList.get(3).getName())) {
                isProgressive = statusNode;
                mapCoachNodesValue.put(mainNodesList.get(3).getName(), isProgressive);
                Toast toast = Toast.makeText(this, mainNodesList.get(3).getName(),
                        Toast.LENGTH_LONG);
                toast.show();
            }

        }));

        binding.addViolationButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoachActivity.this, ViolationListActivity.class);
            launcher.launch(intent);
        });

    }

    private void updateRecyclerView(List<ViolationForCoach> updateViolationList) {
        //Обновляем адаптер RecyclerView
        if (adapter != null) {
            adapter.updateData(updateViolationList);
        }
    }

    private static StringBuilder getMainNodesStatesToShow(boolean isEnergySystemNodeAvailable,
                                                          boolean isSkudoppAvailable,
                                                          boolean isAutomaticDoorsAvailable,
                                                          boolean isProgressive) {
        StringBuilder resNodesToShow = new StringBuilder();
        resNodesToShow = resNodesToShow.append("Система электроснабжения: ");
        if (isEnergySystemNodeAvailable) {
            resNodesToShow.append("да" + '\n');
        } else {
            resNodesToShow.append("нет" + '\n');
        }
        resNodesToShow = resNodesToShow.append("СКУДОПП: ");
        if (isSkudoppAvailable) {
            resNodesToShow.append("да" + '\n');
        } else {
            resNodesToShow.append("нет" + '\n');
        }
        resNodesToShow = resNodesToShow.append("Автоматические двери: ");
        if (isAutomaticDoorsAvailable) {
            resNodesToShow.append("да" + '\n');
        } else {
            resNodesToShow.append("нет" + '\n');
        }
        resNodesToShow = resNodesToShow.append("Прогрессивные нормы: ");
        if (isProgressive) {
            resNodesToShow.append("да" + '\n');
        } else {
            resNodesToShow.append("нет" + '\n');
        }
        return resNodesToShow;
    }
}