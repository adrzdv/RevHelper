package com.revhelper.revhelper.activity.revision;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.adapters.ViolationAdapterOnClick;
import com.revhelper.revhelper.databinding.ActivityViolationListBinding;
import com.revhelper.revhelper.mapper.ViolationMapper;
import com.revhelper.revhelper.model.dto.ViolationDto;
import com.revhelper.revhelper.model.dto.ViolationForCoach;
import com.revhelper.revhelper.model.entity.Violation;
import com.revhelper.revhelper.model.enums.RevisionType;
import com.revhelper.revhelper.sys.AppDatabase;
import com.revhelper.revhelper.sys.AppRev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViolationListActivity extends AppCompatActivity {

    private ActivityViolationListBinding binding;
    private AppDatabase appDb;
    ViolationAdapterOnClick adapter;
    private List<ViolationDto> filterdViolationList;
    private List<ViolationDto> violationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViolationListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initData();

        String revisionType = getIntent().getStringExtra("REVTYPE");
        if (revisionType != null) {
            if (!revisionType.isBlank()) {
                initViolationList(revisionType);
            }
        }

        binding.violationListRecyclerView.setAdapter(adapter);
        binding.violationListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {

        appDb = AppRev.getDb();
        filterdViolationList = new ArrayList<>();
        violationList = new ArrayList<>();

        initAdapters();
        initFilters();

    }

    private void initFilters() {
        // Настройка фильтрации
        EditText searchInput = binding.codeViolationSearch.getEditText();
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initAdapters() {

        adapter = new ViolationAdapterOnClick(violationList, violation -> {
            ViolationForCoach violationParce = ViolationMapper.fromDtoFromForCoach(violation);
            Intent intent = new Intent();
            intent.putExtra("VIOLATION", violationParce);
            setResult(RESULT_OK, intent);
            finish();
        });

    }

    private void initViolationList(@NonNull String revisionType) {
        List<Violation> violationsFromDb = appDb.violationDao().getAllViolations();
        if (revisionType.equals(RevisionType.IN_TRANSIT.getRevisionTypeTitle())) {
            violationList = violationsFromDb.stream()
                    .map(ViolationMapper::fromEntityToDto)
                    .filter(ViolationDto::isInTransit)
                    .collect(Collectors.toList());
        } else if (revisionType.equals(RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle())) {
            violationList = violationsFromDb.stream()
                    .map(ViolationMapper::fromEntityToDto)
                    .filter(ViolationDto::isAtTurnroundPoint)
                    .collect(Collectors.toList());
        } else if (revisionType.equals(RevisionType.AT_START_POINT.getRevisionTypeTitle())) {
            violationList = violationsFromDb.stream()
                    .map(ViolationMapper::fromEntityToDto)
                    .filter(ViolationDto::isAtStartPoint)
                    .collect(Collectors.toList());
        }
        Collections.sort(violationList);
        adapter.updateData(violationList);
    }

    private void filterData(@NonNull String query) {
        filterdViolationList.clear();
        if (query.isEmpty()) {
            filterdViolationList.addAll(violationList);
        } else {
            if (isNumeric(query)) {
                for (ViolationDto violation : violationList) {
                    if (String.valueOf(violation.getCode()).contains(query)) {
                        filterdViolationList.add(violation);
                    }
                }
            } else {
                for (ViolationDto violation : violationList) {
                    if (violation.getName().toLowerCase().contains(query.toLowerCase())) {
                        filterdViolationList.add(violation);
                    }
                }
            }
        }

        adapter.updateData(filterdViolationList);
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}