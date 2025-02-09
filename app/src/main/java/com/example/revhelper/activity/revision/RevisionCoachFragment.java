package com.example.revhelper.activity.revision;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.revhelper.R;
import com.example.revhelper.model.entity.Deps;
import com.example.revhelper.sys.SharedViewModel;
import com.example.revhelper.adapters.ViolationAdapter;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.ViolationForCoach;
import com.example.revhelper.model.enums.StatsCoachParams;
import com.example.revhelper.sys.AppRev;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RevisionCoachFragment extends Fragment implements View.OnClickListener {

    private SharedViewModel sharedViewModel;
    private ActivityResultLauncher<Intent> launcher;
    private CoachOnRevision coach;
    private ViolationAdapter violationAdapter;
    private List<ViolationForCoach> violationList;
    private final LocalDateTime revisionStart = LocalDateTime.now();


    public RevisionCoachFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        coach = sharedViewModel.getCoachOnRevision();
        initData(coach);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ViolationForCoach violationForCoach = result.getData().getParcelableExtra("VIOLATION");
                        if (violationForCoach != null) {
                            if (violationList.contains(violationForCoach)) {
                                AppRev.showToast(requireContext(), "Нарушение уже добавлено");
                                return;
                            }
                            violationList.add(violationForCoach);
                            updateRecycler(violationList);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revision_coach, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (coach != null) {
            sharedViewModel.setCoachOnRevision(coach);
        }
    }

    @Override
    public void onClick(@NonNull View v) {

        if (v.getId() == R.id.revision_save_additional_param) {
            setAdditionalCoachParam();
        } else if (v.getId() == R.id.revision_add_new_violation) {

            Intent intent = new Intent(requireContext(), ViolationListActivity.class);
            intent.putExtra("REVTYPE", sharedViewModel.getOrder().getRevisionType());
            launcher.launch(intent);

        } else if (v.getId() == R.id.revision_end_coach_revision) {

            String workerName = getWorkerName();
            if (workerName == null) {
                return;
            }

            String workerDepoName = getDepoName();
            if (workerDepoName != null) {
                coach.setCoachWorkerDep(workerDepoName);
            }

            CheckBox isTrailingCarCheckBox = getView().findViewById(R.id.trailing_car_checkbox);
            coach.setTrailingCar(isTrailingCarCheckBox.isChecked());
            coach.setCoachWorker(workerName);
            coach.setViolationList(violationList);
            if (coach.getRevisionTime() == null) {
                coach.setRevisionTime(revisionStart);
            }
            if (coach.getRevisionEndTime() == null) {
                coach.setRevisionEndTime(LocalDateTime.now());
            }
            sharedViewModel.setCoachOnRevision(coach);
            Navigation.findNavController(v).navigateUp();
        } else if (v.getId() == R.id.bck_img_bttn_coach_fragment) {
            Navigation.findNavController(v).navigateUp();
        }

    }

    @NonNull
    private String getDepoName() {
        TextInputLayout depoInputLayout = getView().findViewById(R.id.revision_depo_input_layout);

        return depoInputLayout.getEditText().getText().toString();

    }

    @Nullable
    private String getWorkerName() {

        TextInputLayout workerData = getView().findViewById(R.id.revision_coach_worker_text_view);
        String workerName = workerData.getEditText().getText().toString();
        if (workerName.isBlank()) {
            AppRev.showToast(requireContext(), "Введите данные работника");
            return null;
        } else if (!AppRev.getChecker().checkWorkerDataRegex(workerName)) {
            AppRev.showToast(requireContext(), "Неверный формат ввода данных работников");
            return null;
        } else {
            return workerName;
        }

    }

    private void setAdditionalCoachParam() {
        Spinner additionalParamsSpinner = getView().findViewById(R.id.revision_spinner_additional_nodes);
        RadioGroup additionalParamsRadioGroupe = getView().findViewById(R.id.revision_radio_group_status);
        TextView coachAdditionalParamsTextView = getView().findViewById(R.id.revision_coach_info_add_params);
        int selectedStatusAdditionalParam = additionalParamsRadioGroupe.getCheckedRadioButtonId();
        boolean status = selectedStatusAdditionalParam == getView().findViewById(R.id.revision_radio_good).getId();
        String selectedAdditionalParam = additionalParamsSpinner.getSelectedItem().toString();

        if (selectedAdditionalParam.equals(StatsCoachParams.SKUDOPP.getAdditionalParamTitle())) {
            coach.setCoachSkudopp(status);
        } else if (selectedAdditionalParam.equals(StatsCoachParams.AUTO_DOOR.getAdditionalParamTitle())) {
            coach.setCoachAutomaticDoor(status);
        } else if (selectedAdditionalParam.equals(StatsCoachParams.PROGRESS.getAdditionalParamTitle())) {
            coach.setCoachProgressive(status);
        }

        coachAdditionalParamsTextView.setText(getStringAdditionalParams(coach));
    }

    private void initData(CoachOnRevision coach) {

        initImgButtons();
        initTextView(coach);
        initAnotherViews(coach);

    }

    private void initAnotherViews(@NonNull CoachOnRevision coach) {

        RecyclerView violationRecyclerView = getView().findViewById(R.id.revision_violation_list_recycler);
        CheckBox isTrailingCar = getView().findViewById(R.id.trailing_car_checkbox);
        Spinner additionalParamsSpinner = getView().findViewById(R.id.revision_spinner_additional_nodes);
        AutoCompleteTextView depoNameTextView = getView().findViewById(R.id.revision_worker_depo_text_view);

        List<ViolationForCoach> coachViolations = coach.getViolationList();
        List<String> depoNamesStringList = AppRev.getDb().depoDao().getAlldeps().stream()
                .map(Deps::getName)
                .collect(Collectors.toList());


        if (coachViolations != null) {
            violationList = new ArrayList<>(coachViolations);
        } else {
            violationList = new ArrayList<>();
        }

        List<String> additionalParams = StatsCoachParams.getCoachParamsList();

        violationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        violationRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        violationAdapter = new ViolationAdapter(requireContext(), violationList);
        violationRecyclerView.setAdapter(violationAdapter);

        ArrayAdapter<String> additionalParamsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, additionalParams);
        additionalParamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        additionalParamsSpinner.setAdapter(additionalParamsAdapter);

        ArrayAdapter<String> depoNamesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, depoNamesStringList);
        depoNameTextView.setAdapter(depoNamesAdapter);

        isTrailingCar.setChecked(coach.isTrailingCar());

    }

    private void initTextView(CoachOnRevision coach) {

        TextInputLayout coachNumberTextLayout = getView().findViewById(R.id.revision_coach_number_text_view);
        TextInputLayout coachWorkerTextLayout = getView().findViewById(R.id.revision_coach_worker_text_view);
        AutoCompleteTextView coachWorkerDepoTextLayout = getView().findViewById(R.id.revision_worker_depo_text_view);
        TextView coachAdditionalParamsTextView = getView().findViewById(R.id.revision_coach_info_add_params);

        if (coach.getCoachNumber() != null) {
            coachNumberTextLayout.getEditText().setText(coach.getCoachNumber());
        }

        if (coach.getCoachWorker() != null) {
            coachWorkerTextLayout.getEditText().setText(coach.getCoachWorker());
        }

        if (coach.getCoachWorkerDep() != null) {
            coachWorkerDepoTextLayout.setText(coach.getCoachWorkerDep());
        }

        coachAdditionalParamsTextView.setText(getStringAdditionalParams(coach));

    }

    private String getStringAdditionalParams(CoachOnRevision coach) {
        StringBuilder res = new StringBuilder("Параметры:\n");
        String yes = "ДА";
        String no = "НЕТ";

        res.append("ПРОГРЕСС: ");
        if (coach.isCoachProgressive()) {
            res.append(yes).append("\n");
        } else {
            res.append(no).append("\n");
        }

        res.append("СКУДОПП: ");
        if (coach.isCoachSkudopp()) {
            res.append(yes).append("\n");
        } else {
            res.append(no).append("\n");
        }

        res.append("АВТ.ДВЕРИ: ");
        if (coach.isCoachAutomaticDoor()) {
            res.append(yes).append("\n");
        } else {
            res.append(no).append("\n");
        }

        return res.toString();
    }

    private void initImgButtons() {

        ImageButton addAdditionalParamButton = getView().findViewById(R.id.revision_save_additional_param);
        ImageButton addViolationButton = getView().findViewById(R.id.revision_add_new_violation);
        ImageButton saveCoachDataButton = getView().findViewById(R.id.revision_end_coach_revision);

        addAdditionalParamButton.setOnClickListener(this);
        addViolationButton.setOnClickListener(this);
        saveCoachDataButton.setOnClickListener(this);

    }

    private void updateRecycler(List<ViolationForCoach> updatedViolationList) {
        if (violationAdapter != null) {
            violationAdapter.updateData(updatedViolationList);
        }
    }
}