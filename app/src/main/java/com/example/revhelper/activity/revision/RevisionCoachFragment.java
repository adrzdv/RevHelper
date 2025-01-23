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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.revhelper.R;
import com.example.revhelper.activity.CoachActivity;
import com.example.revhelper.activity.SharedViewModel;
import com.example.revhelper.activity.ViolationListActivity;
import com.example.revhelper.adapters.ViolationAdapter;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.ViolationForCoach;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.MainNodes;
import com.example.revhelper.model.enums.AdditionalParams;
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
    private ViolationAdapter adapter;
    private List<ViolationForCoach> violationList;
    private List<String> additionalParams;
    private LocalDateTime revisionStart = LocalDateTime.now();


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
                        ViolationForCoach violationForCoach = result.getData().getParcelableExtra("violation");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revision_coach, container, false);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.revision_save_additional_param) {
            setAdditionalCoachParam();
        } else if (v.getId() == R.id.revision_add_new_violation) {

            //open Activity

            Intent intent = new Intent(requireContext(), ViolationListActivity.class);
            intent.putExtra("REVTYPE", sharedViewModel.getOrder().getRevisionType());
            launcher.launch(intent);

        } else if (v.getId() == R.id.revision_end_coach_revision) {
            sharedViewModel.setCoachOnRevision(coach);
            Navigation.findNavController(v).navigateUp();
        }

    }

    private void setAdditionalCoachParam() {
        Spinner additionalParamsSpinner = getView().findViewById(R.id.revision_spinner_additional_nodes);
        RadioGroup additionalParamsRadioGroupe = getView().findViewById(R.id.revision_radio_group_status);
        int selectedStatusAdditionalParam = additionalParamsRadioGroupe.getCheckedRadioButtonId();
        boolean status = selectedStatusAdditionalParam == getView().findViewById(R.id.revision_radio_good).getId();
        String selectedAdditionalParam = additionalParamsSpinner.getSelectedItem().toString();

        if (selectedAdditionalParam.equals(AdditionalParams.SKUDOPP.getAdditionalParamTitle())) {
            coach.setCoachSkudopp(status);
        } else if (selectedAdditionalParam.equals(AdditionalParams.AUTO_DOOR.getAdditionalParamTitle())) {
            coach.setCoachAutomaticDoor(status);
        } else if (selectedAdditionalParam.equals(AdditionalParams.PROGRESS.getAdditionalParamTitle())) {
            coach.setCoachProgressive(status);
        }
    }

    private void initData(CoachOnRevision coach) {

        RecyclerView violationRecyclerView = getView().findViewById(R.id.revision_violation_list_recycler);
        CheckBox isTrailingCar = getView().findViewById(R.id.trailing_car_checkbox);
        TextInputLayout coachNumberTextLayout = getView().findViewById(R.id.revision_coach_number_text_view);
        TextInputLayout coachWorkerTextLayout = getView().findViewById(R.id.revision_coach_worker_text_view);

        ImageButton addAdditionalParamButton = getView().findViewById(R.id.revision_save_additional_param);
        ImageButton addViolationButton = getView().findViewById(R.id.revision_add_new_violation);
        ImageButton saveCoachDataButton = getView().findViewById(R.id.revision_end_coach_revision);

        List<ViolationForCoach> coachViolations = coach.getViolationList();

        if (coach.getCoachNumber() != null) {
            coachNumberTextLayout.getEditText().setText(coach.getCoachNumber());
        }

        if (coach.getCoachWorker() != null) {
            coachWorkerTextLayout.getEditText().setText(coach.getCoachNumber());
        }

        if (coachViolations != null) {
            violationList = new ArrayList<>(coachViolations);
        } else {
            violationList = new ArrayList<>();
        }

        additionalParams = new ArrayList<>(AppRev.getDb().mainNodesDao().getMainNodesList().stream()
                .map(MainNodes::getName)
                .collect(Collectors.toList()));

        violationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        violationRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        adapter = new ViolationAdapter(requireContext(), violationList);
        violationRecyclerView.setAdapter(adapter);

        ArrayAdapter<String> additionalParamsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, additionalParams);
        additionalParamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        isTrailingCar.setChecked(coach.isTrailingCar());

        addAdditionalParamButton.setOnClickListener(this);
        addViolationButton.setOnClickListener(this);
        saveCoachDataButton.setOnClickListener(this);

    }

    private void updateRecycler(List<ViolationForCoach> updatedViolationList) {
        if (adapter != null) {
            adapter.updateData(updatedViolationList);
        }
    }
}