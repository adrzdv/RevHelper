package com.revhelper.revhelper.activity.information;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.adapters.ViolationSingleCatalogViewAdapter;
import com.revhelper.revhelper.databinding.ActivityViolationCatalogBinding;
import com.revhelper.revhelper.fragments.DialogFragmentExitConfirmation;
import com.revhelper.revhelper.mapper.ViolationMapper;
import com.revhelper.revhelper.model.dto.ViolationDto;
import com.revhelper.revhelper.model.enums.RevisionType;
import com.revhelper.revhelper.sys.AppDatabase;
import com.revhelper.revhelper.sys.AppRev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViolationCatalogActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityViolationCatalogBinding binding;
    private AppDatabase appDatabase;
    private List<ViolationDto> filteredList;
    private List<ViolationDto> violationsFromDb;
    private List<ViolationDto> currentViolationList;
    private ViolationSingleCatalogViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViolationCatalogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText searchInput = binding.catalogCodeViolationSearchTextInput.getEditText();
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDataByString(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        binding.bckViolationCatalogButton.setOnClickListener(this);

        initDataForProcessing();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.bckViolationCatalogButton) {
            backFromActivity();
        }

    }

    private void initDataForProcessing() {

        appDatabase = AppRev.getDb();
        violationsFromDb = appDatabase.violationDao().getAllViolations().stream()
                .map(ViolationMapper::fromEntityToDto)
                .collect(Collectors.toList());
        Collections.sort(violationsFromDb);

        initSpinner();
        initRecycler();

    }

    private void initSpinner() {

        List<String> revTypes = List.of(RevisionType.ALL.getRevisionTypeTitle(),
                RevisionType.IN_TRANSIT.getRevisionTypeTitle(),
                RevisionType.AT_START_POINT.getRevisionTypeTitle(),
                RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle(),
                RevisionType.AT_TICKET_OFFICE.getRevisionTypeTitle());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, revTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.catalogSpinnerRevisionType.setAdapter(arrayAdapter);
        binding.catalogSpinnerRevisionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = revTypes.get(position);
                filterDataByRevisionType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterDataByRevisionType(RevisionType.ALL.getRevisionTypeTitle());
            }
        });

    }

    private void initRecycler() {

        filteredList = new ArrayList<>();
        currentViolationList = new ArrayList<>(violationsFromDb);
        adapter = new ViolationSingleCatalogViewAdapter(currentViolationList);
        RecyclerView violationListRecycler = binding.catalogViolationListRecyclerView;
        violationListRecycler.setAdapter(adapter);
        violationListRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void filterDataByRevisionType(String revType) {

        if (revType.equals(RevisionType.ALL.getRevisionTypeTitle())) {
            filteredList.clear();
            filteredList.addAll(violationsFromDb);
        } else if (revType.equals(RevisionType.IN_TRANSIT.getRevisionTypeTitle())) {
            filteredList.clear();
            currentViolationList.clear();
            filteredList = filterInTransit();
            currentViolationList.addAll(filteredList);
        } else if (revType.equals(RevisionType.AT_START_POINT.getRevisionTypeTitle())) {
            filteredList.clear();
            currentViolationList.clear();
            filteredList = filterAtStartPoint();
            currentViolationList.addAll(filteredList);
        } else if (revType.equals(RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle())) {
            filteredList.clear();
            currentViolationList.clear();
            filteredList = filterAtTurnRoundPoint();
            currentViolationList.addAll(filteredList);
        } else if (revType.equals(RevisionType.AT_TICKET_OFFICE.getRevisionTypeTitle())) {
            filteredList.clear();
            currentViolationList.clear();
            filteredList = filterAtTicketOffice();
            currentViolationList.addAll(filteredList);
        }

        adapter.updateData(filteredList);
    }

    private List<ViolationDto> filterInTransit() {
        return violationsFromDb.stream()
                .filter(ViolationDto::isInTransit)
                .collect(Collectors.toList());
    }

    private List<ViolationDto> filterAtStartPoint() {
        return violationsFromDb.stream()
                .filter(ViolationDto::isAtStartPoint)
                .collect(Collectors.toList());
    }

    private List<ViolationDto> filterAtTurnRoundPoint() {
        return violationsFromDb.stream()
                .filter(ViolationDto::isAtTurnroundPoint)
                .collect(Collectors.toList());
    }

    private List<ViolationDto> filterAtTicketOffice() {
        return violationsFromDb.stream()
                .filter(ViolationDto::isAtTicketOffice)
                .collect(Collectors.toList());
    }

    private void filterDataByString(@NonNull String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(currentViolationList);
        } else {
            if (isNumeric(query)) {
                for (ViolationDto violation : currentViolationList) {
                    if (String.valueOf(violation.getCode()).contains(query)) {
                        filteredList.add(violation);
                    }
                }
            } else {
                for (ViolationDto violation : currentViolationList) {
                    if (violation.getName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(violation);
                    }
                }
            }
        }

        adapter.updateData(filteredList);
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void backFromActivity() {
        DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
}