package com.example.revhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.revhelper.R;
import com.example.revhelper.adapters.ViolationAdapterOnClick;
import com.example.revhelper.databinding.ActivityViolationListBinding;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.dto.OrderParcelable;
import com.example.revhelper.model.dto.ViolationForCoach;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.model.enums.RevisionType;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViolationListActivity extends AppCompatActivity {

    private ActivityViolationListBinding binding;
    private AppDatabase appDb;
    ViolationAdapterOnClick adapter;
    private List<Violation> filterdViolationList = new ArrayList<>();
    private List<Violation> violationList = new ArrayList<>();
    private List<Violation> violationsFromDb;
    private OrderParcelable order = new OrderParcelable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViolationListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        appDb = AppRev.getDb();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().getParcelableExtra("ORDER") != null) {
            order = getIntent().getParcelableExtra("ORDER");
        }

        String revisionType = order.getRevisionType();

        //позже поменять алгоритм, в сущность и таблицу добавить доп поля КАССА/ПУТЬ/ПФ/ПО,
        // типа int, чтоб разбить на типы проверок
        violationsFromDb = appDb.violationDao().getAllViolations();
        List<Integer> revisionTypes = getRevisionType(revisionType);
        if (revisionTypes != null) {
            for (int type : revisionTypes) {
                List<Violation> tempList = violationsFromDb.stream()
                        .filter(violation -> violation.getRevisionType() == type)
                        .collect(Collectors.toList());
                violationList.addAll(tempList);
            }
        }

        Collections.sort(violationList);

        adapter = new ViolationAdapterOnClick(violationList, violation -> {
            ViolationForCoach violationParce = ViolationMapper.fromEntityToForCouch(violation);
            Intent intent = new Intent();
            intent.putExtra("violation", violationParce);
            setResult(RESULT_OK, intent);
            finish();
        });

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

        binding.violationListRecyclerView.setAdapter(adapter);
        binding.violationListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void filterData(String query) {
        filterdViolationList.clear();
        if (query.isEmpty()) {
            filterdViolationList.addAll(violationList);
        } else {
            if (isNumeric(query)) {
                for (Violation violation : violationList) {
                    if (violation.getCode() == Integer.parseInt(query)) {
                        filterdViolationList.add(violation);
                    }
                }
            } else {
                for (Violation violation : violationList) {
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

    private List<Integer> getRevisionType(String string) {
        if (string.equals(RevisionType.IN_TRANSIT.getRevisionTypeTitle())) {
            return new ArrayList<>(List.of(1, 3, 5, 6, 7));
        } else if (string.equals(RevisionType.AT_START_POINT.getRevisionTypeTitle())) {
            return new ArrayList<>(List.of(2, 3, 5, 6, 8));
        } else if (string.equals(RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle())) {
            return new ArrayList<>(List.of(2, 3, 5, 7));
        }
        return null;
    }

}