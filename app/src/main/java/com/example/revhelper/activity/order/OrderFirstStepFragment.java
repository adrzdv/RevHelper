package com.example.revhelper.activity.order;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.revhelper.R;
import com.example.revhelper.sys.SharedViewModel;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.enums.RevisionType;
import com.example.revhelper.sys.AppRev;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderFirstStepFragment extends Fragment implements View.OnClickListener {

    private OrderDtoParcelable order;
    private ArrayAdapter<String> spinnerAdapter;
    private SharedViewModel sharedViewModel;

    public OrderFirstStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_first_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        order = sharedViewModel.getOrder();

        if (order != null) {
            sharedViewModel.setOrder(order);
            initUi(spinnerAdapter);
        }

        spinnerAdapter = initSpinnerAdapter();
        initSpinner(spinnerAdapter);

        TextInputEditText inputOrderDate = getView().findViewById(R.id.order_input_data_edit_text);
        inputOrderDate.setOnClickListener((v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (View, year1, month1, dayOfMonth) -> {
                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                inputOrderDate.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        }));

        FloatingActionButton nextButton = view.findViewById(R.id.to_step_two_order);
        ImageButton bckButton = view.findViewById(R.id.bck_img_bttn_make_order_first_step);

        nextButton.setOnClickListener(this);
        bckButton.setOnClickListener(this);
    }

    private void initUi(@NonNull ArrayAdapter<String> spinnerAdapter) {

        TextInputLayout orderNumber = getView().findViewById(R.id.order_input_number);
        TextInputLayout orderDate = getView().findViewById(R.id.order_input_data);
        TextInputLayout orderRoute = getView().findViewById(R.id.order_input_route);
        Spinner orderRevType = getView().findViewById(R.id.order_spinner_revtype_stepOne);
        orderNumber.getEditText().setText(order.getNumber());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        orderDate.getEditText().setText(order.getDate().format(formatter));
        orderRoute.getEditText().setText(order.getRoute());
        int position = spinnerAdapter.getPosition(order.getRevisionType());
        orderRevType.setSelection(position);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.to_step_two_order) {

            order = goNext();

            if (order == null) {
                AppRev.showToast(requireContext(), "Не все поля заполнены");
                return;
            }

            sharedViewModel.setOrder(order);
            Navigation.findNavController(v).navigate(R.id.action_to_stepTwo);

        } else if (v.getId() == R.id.bck_img_bttn_make_order_first_step) {
            Navigation.findNavController(v).navigateUp();
        }
    }

    @Nullable
    private OrderDtoParcelable goNext() {

        TextInputLayout orderNumberLayout = getView().findViewById(R.id.order_input_number);
        TextInputLayout orderDateLayout = getView().findViewById(R.id.order_input_data);
        TextInputLayout orderRouteLayout = getView().findViewById(R.id.order_input_route);
        Spinner orderRevTypeSpinner = getView().findViewById(R.id.order_spinner_revtype_stepOne);

        if (!orderRouteLayout.getEditText().getText().toString().equals("") &&
                !orderDateLayout.getEditText().getText().toString().equals("") &&
                !orderNumberLayout.getEditText().getText().toString().equals("")) {

            String orderNumber = orderNumberLayout.getEditText().getText().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String orderDateString = orderDateLayout.getEditText().getText().toString();
            LocalDate orderDate = LocalDate.parse(orderDateString, formatter);
            String orderRoute = orderRouteLayout.getEditText().getText().toString();
            String revType = orderRevTypeSpinner.getSelectedItem().toString();

            if (order != null) {
                order.setNumber(orderNumber);
                order.setDate(orderDate);
                order.setRoute(orderRoute);
                order.setRevisionType(revType);
                return order;
            }

            return new OrderDtoParcelable(orderNumber, orderDate, orderRoute, revType);
        }
        return null;
    }

    private void initSpinner(@NonNull ArrayAdapter<String> adapter) {

        Spinner revTypeSpinner = getView().findViewById(R.id.order_spinner_revtype_stepOne);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        revTypeSpinner.setAdapter(adapter);

    }

    @NonNull
    private ArrayAdapter<String> initSpinnerAdapter() {

        List<String> revisionTypes = List.of(RevisionType.IN_TRANSIT.getRevisionTypeTitle(),
                RevisionType.AT_START_POINT.getRevisionTypeTitle(),
                RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle());

        return new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, revisionTypes);

    }

}