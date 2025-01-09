package com.example.revhelper.activity;

import android.annotation.SuppressLint;
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
import com.example.revhelper.model.dto.ViolationDtoParce;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import java.util.ArrayList;
import java.util.List;

public class ViolationListActivity extends AppCompatActivity {

    private ActivityViolationListBinding binding;
    private AppDatabase appDb;
    ViolationAdapterOnClick adapter;
    private List<Violation> filterdViolationList = new ArrayList<>();
    private List<Violation> violationList;

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

        violationList = appDb.violationDao().getAllViolations();

        adapter = new ViolationAdapterOnClick(violationList, violation -> {
            ViolationDtoParce violationParce = ViolationMapper.fromEntityToParce(violation);
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
        binding.violationListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

        adapter.notifyDataSetChanged(); // Обновление списка
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