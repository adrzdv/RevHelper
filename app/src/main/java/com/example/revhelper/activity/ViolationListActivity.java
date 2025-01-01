package com.example.revhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.revhelper.R;
import com.example.revhelper.adapters.ViolationAdapter;
import com.example.revhelper.adapters.ViolationAdapterOnClick;
import com.example.revhelper.databinding.ActivityOrderBinding;
import com.example.revhelper.databinding.ActivityViolationListBinding;
import com.example.revhelper.dto.ViolationDtoParce;
import com.example.revhelper.mapper.ViolationMapper;
import com.example.revhelper.model.Violation;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

import java.util.Arrays;
import java.util.List;

public class ViolationListActivity extends AppCompatActivity {

    private ActivityViolationListBinding binding;
    private AppDatabase appDb;

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

        List<Violation> violationList = appDb.violationDao().getAllViolations();

        ViolationAdapterOnClick adapter = new ViolationAdapterOnClick(violationList, violation -> {
            ViolationDtoParce violationParce = ViolationMapper.fromEntityToParce(violation);
            Intent intent = new Intent();
            intent.putExtra("violation", violationParce);
            setResult(RESULT_OK, intent);
            finish();
        });

        binding.violationListRecyclerView.setAdapter(adapter);
        binding.violationListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.violationListRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}