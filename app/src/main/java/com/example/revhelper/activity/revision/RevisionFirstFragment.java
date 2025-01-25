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
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.revhelper.R;
import com.example.revhelper.activity.information.ResultRevisionActivity;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.sys.SharedViewModel;
import com.example.revhelper.adapters.CoachSingleAdapterWithoutButton;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.sys.AppRev;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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

            if (addInfoInOrderAnd() == null) {
                return;
            }
            Intent intent = new Intent(requireContext(), ResultRevisionActivity.class);
            intent.putExtra("ORDER", order);
            startActivity(intent);
            requireActivity().finish();

        } else if (v.getId() == R.id.bck_img_bttn_make_revision_main) {
            DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
            dialog.show(requireActivity().getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onItemClick(@NonNull CoachOnRevision coach) {
        sharedViewModel.setCoachOnRevision(coach);
        sharedViewModel.setOrder(order);
        sharedViewModel.setTrain(order.getTrain());
        Navigation.findNavController(getView()).navigate(R.id.action_to_coach_revision);
    }

    private OrderDtoParcelable addInfoInOrderAnd() {

        if (addPriceToOrder() == null) {
            return null;
        }
        if (addRadiodeviceToOrder() == null) {
            return null;
        }
        if (addAudioinformToOrder() == null) {
            return null;
        }

        return order;

    }

    private OrderDtoParcelable addAudioinformToOrder() {
        RadioGroup audioRadioGroup = getView().findViewById(R.id.revision_radio_group_audioinform);

        int selectedAudioStatus = audioRadioGroup.getCheckedRadioButtonId();
        if (selectedAudioStatus == -1) {
            AppRev.showToast(requireContext(),
                    "Не проведена проверка аудиоинформирования");
            return null;
        }

        Boolean statusAudioinform;
        if (selectedAudioStatus == getView().findViewById(R.id.revision_audioinform_radio_good).getId()) {
            statusAudioinform = true;
        } else if ((selectedAudioStatus == getView().findViewById(R.id.revision_audioinform_radio_faulty).getId())) {
            statusAudioinform = false;
        } else {
            statusAudioinform = null;
        }
        order.setIsInform(statusAudioinform);
        return order;
    }

    private OrderDtoParcelable addRadiodeviceToOrder() {

        RadioGroup radiodeviceRadioGroup = getView().findViewById(R.id.revision_radio_group_radio);

        int selectedRadiodeviceStatus = radiodeviceRadioGroup.getCheckedRadioButtonId();
        if (selectedRadiodeviceStatus == -1) {
            AppRev.showToast(requireContext(),
                    "Не проведена проверка наличия радиоустановки");
            return null;
        }
        boolean statusRadiodevice = selectedRadiodeviceStatus == getView().findViewById(R.id.revision_radiodevice_radio_good).getId();
        order.setRadio(statusRadiodevice);
        return order;
    }

    private OrderDtoParcelable addPriceToOrder() {
        RadioGroup priceRadioGroup = getView().findViewById(R.id.revision_radio_group_price);
        int selectedPriceStatus = priceRadioGroup.getCheckedRadioButtonId();
        if (selectedPriceStatus == -1) {
            AppRev.showToast(requireContext(),
                    "Не проведена проверка наличия полного перечня товаров");
            return null;
        }
        Boolean statusPrice;
        if (selectedPriceStatus == getView().findViewById(R.id.revision_price_radio_good).getId()) {
            statusPrice = true;
        } else if ((selectedPriceStatus == getView().findViewById(R.id.revision_price_radio_faulty).getId())) {
            statusPrice = false;
        } else {
            statusPrice = null;
        }
        order.setPrice(statusPrice);
        return order;
    }

    private void initData(@NonNull OrderDtoParcelable order) {
        coachList = new ArrayList<>(order.getCoachMap().values());
    }

    private void initUi(OrderDtoParcelable order) {
        TextView orderDataTextView = getView().findViewById(R.id.revision_order_data_cell);
        TextView orderTrainTextView = getView().findViewById(R.id.revision_train_data_cell);
        RecyclerView coachRecycler = getView().findViewById(R.id.revision_recycler_coach_view);
        ImageButton infoButton = getView().findViewById(R.id.revision_take_train_info);
        ImageButton bckImage = getView().findViewById(R.id.bck_img_bttn_make_revision_main);
        FloatingActionButton saveResultButton = getView().findViewById(R.id.revision_make_result);

        orderDataTextView.setText(makeOrderData(order));
        orderTrainTextView.setText(makeTrainData(order.getTrain()));

        coachAdapter = new CoachSingleAdapterWithoutButton(requireContext(), coachList, this);
        coachRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        coachRecycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        coachRecycler.setAdapter(coachAdapter);

        infoButton.setOnClickListener(this);
        saveResultButton.setOnClickListener(this);
        bckImage.setOnClickListener(this);
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