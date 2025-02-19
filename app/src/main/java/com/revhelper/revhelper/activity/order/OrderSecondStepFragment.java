package com.revhelper.revhelper.activity.order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.sys.SharedViewModel;
import com.revhelper.revhelper.adapters.WorkerRecyclerViewAdapter;
import com.revhelper.revhelper.model.dto.Worker;
import com.revhelper.revhelper.model.dto.OrderDtoParcelable;
import com.revhelper.revhelper.model.enums.WorkerJob;
import com.revhelper.revhelper.sys.AppRev;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSecondStepFragment extends Fragment implements View.OnClickListener {

    private OrderDtoParcelable order;
    private Map<String, Worker> crewMap;
    private List<Worker> crewList;
    private WorkerRecyclerViewAdapter adapter;
    private SharedViewModel sharedViewModel;

    public OrderSecondStepFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_second_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        order = sharedViewModel.getOrder();
        crewMap = new HashMap<>();
        crewList = new ArrayList<>();

        if (order != null) {
            if (!order.getCrewLeaders().isEmpty()) {
                crewMap = order.getCrewLeaders();
                crewList.addAll(crewMap.values());
                updateRecycler(crewList);
            }
        }

        initSpinner();
        initAdapter();

        FloatingActionButton nextButton = getView().findViewById(R.id.to_step_three_order);
        ImageButton addCrewButton = getView().findViewById(R.id.add_crew);
        ImageButton bckButton = getView().findViewById(R.id.bck_img_bttn_make_order_second_step);
        nextButton.setOnClickListener(this);
        addCrewButton.setOnClickListener(this);
        bckButton.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (order != null) {
            order.setCrewLeaders(crewMap);
            sharedViewModel.setOrder(order);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.to_step_three_order) {
            addDataToOrder(v);
        } else if (v.getId() == R.id.bck_img_bttn_make_order_second_step) {
            sharedViewModel.setOrder(order);
            Navigation.findNavController(v).navigateUp();
        } else if (v.getId() == R.id.add_crew) {
            addCrew();
        }

    }

    private void initAdapter() {
        adapter = new WorkerRecyclerViewAdapter(getContext(), crewList, worker -> {
            crewMap.remove(worker.getJobTitle());
            crewList.remove(worker);
        });
        RecyclerView crewRecycler = getView().findViewById(R.id.order_crew_recycler_view);
        crewRecycler.setAdapter(adapter);
        crewRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void addCrew() {
        TextInputLayout crewSurnameLayout = getView().findViewById(R.id.order_surname_crew_input);

        if (!crewSurnameLayout.getEditText().getText().toString().equals("")) {
            String workerName = crewSurnameLayout.getEditText().getText().toString().trim();

            if (!AppRev.getChecker().checkWorkerDataRegex(workerName)) {
                AppRev.showToast(requireContext(), "Неверный формат ввода ФИО");
                return;
            }

            Spinner workerJob = getView().findViewById(R.id.order_crew_type_spinner);
            crewMap.put(workerJob.getSelectedItem().toString(),
                    new Worker(workerName, workerJob.getSelectedItem().toString()));
            crewList.clear();
            crewList.addAll(crewMap.values());
            updateRecycler(crewList);
            order.setCrewLeaders(crewMap);
            crewSurnameLayout.getEditText().setText("");
        } else {
            AppRev.showToast(requireContext(), "Данные работника не должны быть пустыми");
        }
    }

    private void updateRecycler(List<Worker> updatedCrewList) {
        if (adapter != null) {
            adapter.updateData(updatedCrewList);
        }
    }

    private void addDataToOrder(View v) {

        if (crewMap.isEmpty()) {
            AppRev.showToast(requireContext(), "Список поездной бригады не заполнен");
            return;
        } else if (!crewMap.containsKey(WorkerJob.LNP.getJobTitle())
                || !crewMap.containsKey(WorkerJob.PEM.getJobTitle())) {
            AppRev.showToast(requireContext(), "Не указаны ЛНП/ПЭМ");
            return;
        }

        order.setCrewLeaders(crewMap);
        CheckBox qualityPassportCheckBox = getView().findViewById(R.id.order_has_quality_passport);
        order.setQualityPassport(qualityPassportCheckBox.isChecked());
        sharedViewModel.setOrder(order);
        Navigation.findNavController(v).navigate(R.id.action_to_stepThree);
    }

    private void initSpinner() {

        List<String> crewTitles = WorkerJob.getJobListTitles();

        ArrayAdapter<String> crewTypeStringAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, crewTitles);
        crewTypeStringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner revTypeSpinner = getView().findViewById(R.id.order_crew_type_spinner);
        revTypeSpinner.setAdapter(crewTypeStringAdapter);
    }

}