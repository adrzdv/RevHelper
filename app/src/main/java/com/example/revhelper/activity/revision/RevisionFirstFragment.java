package com.example.revhelper.activity.revision;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.revhelper.R;
import com.example.revhelper.activity.information.ResultRevisionActivity;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.model.entity.TempStatsParameter;
import com.example.revhelper.sys.SharedViewModel;
import com.example.revhelper.adapters.CoachSingleAdapterWithoutButton;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.sys.AppRev;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RevisionFirstFragment extends Fragment implements CoachSingleAdapterWithoutButton.OnItemClickListener, View.OnClickListener {

    private SharedViewModel sharedViewModel;
    private OrderDtoParcelable order;
    private List<CoachOnRevision> coachList;
    private CoachSingleAdapterWithoutButton coachAdapter;


    public RevisionFirstFragment() {
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        order = sharedViewModel.getOrder();
        if (order != null) {
            initData(order);
            initUi(order);
        }
        if (sharedViewModel.getCoachOnRevision() != null) {
            CoachOnRevision coach = sharedViewModel.getCoachOnRevision();
            order.getCoachMap().put(coach.getCoachNumber(), coach);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_revision_first, container, false);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.revision_take_train_info) {
            AppRev.showToast(requireContext(), "Не реализовано");
        } else if (v.getId() == R.id.revision_make_result) {

            if (!checkTempParams()) {
                return;
            }
            Intent intent = new Intent(requireContext(), ResultRevisionActivity.class);
            intent.putExtra("ORDER", order);
            startActivity(intent);
            requireActivity().finish();

        } else if (v.getId() == R.id.revision_add_temp_param) {
            addTempParamInOrder();
        } else if (v.getId() == R.id.bck_img_bttn_make_revision_main) {
            DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
            dialog.show(requireActivity().getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (order != null) {
            sharedViewModel.setOrder(order);
        }
    }

    @Override
    public void onItemClick(@NonNull CoachOnRevision coach) {
        sharedViewModel.setCoachOnRevision(coach);
        sharedViewModel.setOrder(order);
        sharedViewModel.setTrain(order.getTrain());
        Navigation.findNavController(getView()).navigate(R.id.action_to_coach_revision);
    }

    private boolean checkTempParams() {

        List<String> tempParamsList = AppRev.getDb().templeParametersDao().getAllTempParameters().stream()
                .map(TempStatsParameter::getName)
                .collect(Collectors.toList());

        for (String tempParam : tempParamsList) {
            if (!order.getTempParams().containsKey(tempParam)) {
                AppRev.showToast(requireContext(), "Не внесен параметр " + tempParam);
                return false;
            }
        }
        return true;
    }

    private void addTempParamInOrder() {

        RadioGroup radioGroup = getView().findViewById(R.id.revision_radio_group_temp_params);
        Spinner tempParamSpinner = getView().findViewById(R.id.revision_temp_params_spinner);

        int selectedStatus = radioGroup.getCheckedRadioButtonId();
        String tempParam = tempParamSpinner.getSelectedItem().toString();

        if (selectedStatus == -1) {
            AppRev.showToast(requireContext(),
                    "Не выбран критерий оценки параметра");
            return;
        }

        Boolean status;

        if (selectedStatus == getView().findViewById(R.id.revision_temp_param_radio_good).getId()) {
            status = true;
        } else if (selectedStatus == getView().findViewById(R.id.revision_temp_param_radio_faulty).getId()) {
            status = false;
        } else {
            status = null;
        }

        order.getTempParams().put(tempParam, status);

    }

    private void initData(@NonNull OrderDtoParcelable order) {
        coachList = new ArrayList<>(order.getCoachMap().values());
    }

    private void initUi(OrderDtoParcelable order) {

        initSpinner();
        initRecycler();

        TextView orderDataTextView = getView().findViewById(R.id.revision_order_data_cell);
        TextView orderTrainTextView = getView().findViewById(R.id.revision_train_data_cell);

        ImageButton infoButton = getView().findViewById(R.id.revision_take_train_info);
        ImageButton bckImage = getView().findViewById(R.id.bck_img_bttn_make_revision_main);
        FloatingActionButton saveResultButton = getView().findViewById(R.id.revision_make_result);
        MaterialButton addTempParameterButton = getView().findViewById(R.id.revision_add_temp_param);

        orderDataTextView.setText(makeOrderData(order));
        orderTrainTextView.setText(makeTrainData(order.getTrain()));

        infoButton.setOnClickListener(this);
        saveResultButton.setOnClickListener(this);
        bckImage.setOnClickListener(this);
        addTempParameterButton.setOnClickListener(this);
    }

    private void initRecycler() {

        RecyclerView coachRecycler = getView().findViewById(R.id.revision_recycler_coach_view);
        coachAdapter = new CoachSingleAdapterWithoutButton(requireContext(), coachList, this);
        coachRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        coachRecycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        coachRecycler.setAdapter(coachAdapter);

    }

    private void initSpinner() {

        Spinner tempParamsSpinner = getView().findViewById(R.id.revision_temp_params_spinner);
        List<String> tempParamsList = AppRev.getDb().templeParametersDao().getAllTempParameters().stream()
                .map(TempStatsParameter::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> tempParamSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tempParamsList);
        tempParamSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tempParamsSpinner.setAdapter(tempParamSpinnerAdapter);
    }

    @NonNull
    private String makeOrderData(@NonNull OrderDtoParcelable order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "№ " + order.getNumber() + " от " + order.getDate().format(formatter);
    }

    private String makeTrainData(TrainDtoParcelable train) {
        return "№ " + train.getNumber() + " " + train.getRoute() +
                "\n" + train.getDepName() + "\n" + train.getBranchName();
    }

    private void updateCoachRecyclerView(List<CoachOnRevision> updatedCoachList) {
        if (coachAdapter != null) {
            coachAdapter.updateData(updatedCoachList);
        }
    }

}