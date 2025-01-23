package com.example.revhelper.activity.revision;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
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
import android.widget.TextView;

import com.example.revhelper.R;
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
    private ActivityResultLauncher<Intent> launcher;
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
            //RUN new Activity with resulting

            Intent intent = new Intent(requireContext(), NEW_ACTIVITY.class);
            intent.putExtra("ORDER", order);
            launcher.launch(intent);
            requireActivity().finish();

        } else if (v.getId() == R.id.bck_img_bttn_make_revision_main) {
            sharedViewModel.setOrder(order);
            sharedViewModel.setTrain(order.getTrain());
            Navigation.findNavController(v).navigateUp();
        }
    }

    @Override
    public void onItemClick(@NonNull CoachOnRevision coach) {
        sharedViewModel.setCoachOnRevision(coach);
        sharedViewModel.setOrder(order);
        sharedViewModel.setTrain(order.getTrain());
        Navigation.findNavController(getView()).navigate(R.id.action_to_coach_revision);
    }

    private void initData(@NonNull OrderDtoParcelable order) {
        coachList = new ArrayList<>(order.getCoachMap().values());
    }

    private void initUi(OrderDtoParcelable order) {
        TextView orderDataTextView = getView().findViewById(R.id.revision_order_data_cell);
        TextView orderTrainTextView = getView().findViewById(R.id.revision_train_data_cell);
        RecyclerView coachRecycler = getView().findViewById(R.id.revision_recycler_coach_view);
        ImageButton infoButton = getView().findViewById(R.id.revision_take_train_info);
        FloatingActionButton saveResultButton = getView().findViewById(R.id.revision_make_result);

        orderDataTextView.setText(makeOrderData(order));
        orderTrainTextView.setText(makeTrainData(order.getTrain()));

        coachAdapter = new CoachSingleAdapterWithoutButton(requireContext(), coachList, this);
        coachRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        coachRecycler.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        coachRecycler.setAdapter(coachAdapter);

        infoButton.setOnClickListener(this);

        saveResultButton.setOnClickListener(this);
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