package com.example.revhelper.activity.order;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.revhelper.R;
import com.example.revhelper.activity.AddCoachInOrderActivity;
import com.example.revhelper.sys.SharedViewModel;
import com.example.revhelper.activity.revision.RevisionHostActivity;
import com.example.revhelper.adapters.CoachSingleAdapter;
import com.example.revhelper.mapper.TrainMapper;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.dto.TrainDto;
import com.example.revhelper.model.dto.TrainDtoParcelable;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.Train;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderThirdStepFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private OrderDtoParcelable order;
    private TrainDtoParcelable train;
    private ArrayAdapter<Train> trainAdapter;
    private ArrayAdapter<Coach> crewCoachAdapter;
    private ActivityResultLauncher<Intent> launcher;
    private CoachSingleAdapter coachRecyclerAdapter;
    private List<CoachOnRevision> coachList;
    private AppDatabase appDb;
    private SharedViewModel sharedViewModel;

    public OrderThirdStepFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initData();
        if (order != null) {
            initUi(view);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_third_step, container, false);
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.order_add_new_coach) {
            Intent intent = new Intent(requireContext(), AddCoachInOrderActivity.class);
            launcher.launch(intent);
        } else if (v.getId() == R.id.to_start_revision_process) {
            checkAndSaveOrder();
            Intent intent = new Intent(requireContext(), RevisionHostActivity.class);
            intent.putExtra("ORDER", order);
            launcher.launch(intent);
            requireActivity().finish();
        } else if (v.getId() == R.id.order_clear_coach_list) {
            coachList.clear();
            updateCoachRecyclerView(coachList);
        } else if (v.getId() == R.id.bck_img_bttn_make_order_third_step) {
            sharedViewModel.setOrder(order);
            Navigation.findNavController(v).navigateUp();
        }
    }

    @Override
    public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {

        Object object = parent.getItemAtPosition(position);
        if (object.getClass() == Train.class) {
            Train selectedTrain = (Train) parent.getItemAtPosition(position);
            if (selectedTrain != null) {
                TrainDto trainFromDb = appDb.trainDao().findByNumberWithDep(selectedTrain.getNumber());
                train = TrainMapper.toParceFromDto(trainFromDb);
                order.setTrain(train);
                sharedViewModel.setOrder(order);
                sharedViewModel.setTrain(train);
                updatePreResultView(view, TrainMapper.toParceFromDto(trainFromDb), null);
            }
        } else if (object.getClass() == Coach.class) {
            Coach informator = (Coach) parent.getItemAtPosition(position);
            if (informator != null) {
                sharedViewModel.setInformator(informator);
                updatePreResultView(view, null, informator);
            }
        }
    }

    private void setCoachTextView(Coach coach) {
        if (coach != null) {
            TextView informatorCell = getView().findViewById(R.id.train_informator_cell);
            informatorCell.setText("ДА");
        } else {
            TextView informatorCell = getView().findViewById(R.id.train_informator_cell);
            informatorCell.setText("НЕТ");
        }
    }

    private void initData() {
        appDb = AppRev.getDb();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        order = sharedViewModel.getOrder();
        coachList = new ArrayList<>();
        if (order.getTrain() != null) {
            train = order.getTrain();
            coachList.addAll(order.getCoachMap().values());
        }
        coachRecyclerAdapter = new CoachSingleAdapter(requireContext(), coachList, coach -> {
            coachList.remove(coach);
            order.getCoachMap().remove(coach.getCoachNumber());
        });

        initAutocompliteFields(appDb);
        initRecyclerView();

        ImageButton addCoachButton = getView().findViewById(R.id.order_add_new_coach);
        FloatingActionButton saveOrderButton = getView().findViewById(R.id.to_start_revision_process);
        ImageButton clearCoachList = getView().findViewById(R.id.order_clear_coach_list);
        addCoachButton.setOnClickListener(this);
        saveOrderButton.setOnClickListener(this);
        clearCoachList.setOnClickListener(this);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String coachNumber = result.getData().getStringExtra("COACH");
                        if (coachNumber != null) {
                            CoachOnRevision newCoach = new CoachOnRevision.Builder().setCoachNumber(coachNumber)
                                    .build();
                            order.getCoachMap().put(coachNumber, newCoach);
                            if (!coachList.contains(newCoach)) {
                                coachList.add(newCoach);
                            }
                            updateCoachRecyclerView(coachList);
                        }
                    }
                }
        );
    }

    private void initRecyclerView() {
        RecyclerView coachRecyclerView = getView().findViewById(R.id.order_coach_list_recycler_view);
        coachRecyclerView.setAdapter(coachRecyclerAdapter);
        coachRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        coachRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

    }

    private void initAutocompliteFields(@NonNull AppDatabase appDb) {

        List<Train> trainList = appDb.trainDao().getAllTrains();
        List<Coach> mainCoachList = appDb.coachDao().getAllCoaches();

        trainAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, trainList);
        crewCoachAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, mainCoachList);

        AutoCompleteTextView trainTextView = getView().findViewById(R.id.order_train_number_input);
        AutoCompleteTextView crewCoachTextView = getView().findViewById(R.id.order_coach_number_input);

        trainTextView.setAdapter(trainAdapter);
        trainTextView.setOnItemClickListener(this);
        crewCoachTextView.setAdapter(crewCoachAdapter);
        crewCoachTextView.setOnItemClickListener(this);
    }

    private void initUi(@NonNull View view) {
        TextView dateCell = view.findViewById(R.id.order_date_cell);
        TextView numberCell = view.findViewById(R.id.order_number_cell);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        dateCell.setText(order.getDate().format(formatter));
        numberCell.setText(order.getNumber());
        updateCoachRecyclerView(coachList);

        if (order.getTrain() != null) {
            initUiTrain(order.getTrain());
        }

        setCoachTextView(sharedViewModel.getInformator());
    }

    private void initUiTrain(@NonNull TrainDtoParcelable train) {

        TextView progressCell = getView().findViewById(R.id.train_progress_cell);
        TextView videoCell = getView().findViewById(R.id.train_video_cell);
        TextView portalCell = getView().findViewById(R.id.train_portal_cell);

        progressCell.setText(getStringFromInt(train.getHasProgressive()));
        videoCell.setText(getStringFromInt(train.getHasRegistrator()));
        portalCell.setText(getStringFromInt(train.getHasPortal()));

    }

    private void updatePreResultView(View view, TrainDtoParcelable train, Coach coach) {
        if (train == null) {
            setCoachTextView(coach);
        } else if (coach == null) {
            initUiTrain(train);
        }
    }

    private String getStringFromInt(int integer) {
        if (integer == 1) {
            return "ДА";
        }
        return "НЕТ";
    }

    private void updateCoachRecyclerView(List<CoachOnRevision> updatedCoachList) {
        if (coachRecyclerAdapter != null) {
            coachRecyclerAdapter.updateData(updatedCoachList);
        }
    }

    private void checkAndSaveOrder() {
        if (order.getTrain() == null) {
            AppRev.showToast(requireContext(), "Не введен поезд");
        } else if (order.getCoachMap().isEmpty() || order.getCoachMap() == null) {
            AppRev.showToast(requireContext(), "Список вагонов пуст");
        }

        if (sharedViewModel.getInformator() == null) {
            order.setAutoinformator(false);
        }
        order.setTrain(train);

    }

}