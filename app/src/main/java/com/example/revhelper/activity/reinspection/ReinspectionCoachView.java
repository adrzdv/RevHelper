package com.example.revhelper.activity.reinspection;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.revhelper.R;
import com.example.revhelper.adapters.ViolationAdapterForReinspection;
import com.example.revhelper.databinding.ActivityReinspectionCoachViewBinding;
import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.ViolationForCoach;

import java.util.Collections;
import java.util.List;

public class ReinspectionCoachView extends AppCompatActivity {

    private ViolationAdapterForReinspection adapter;
    private List<ViolationForCoach> violationList;
    private ActivityReinspectionCoachViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityReinspectionCoachViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().getParcelableExtra("COACH") != null) {
            CoachOnRevision coach = getIntent().getParcelableExtra("COACH");
            violationList = coach.getViolationList();
            Collections.sort(violationList);
        }

        adapter = new ViolationAdapterForReinspection(violationList);
        binding.coachViolationRecyclerViewReinspection.setLayoutManager(new LinearLayoutManager(this));
        binding.coachViolationRecyclerViewReinspection.setAdapter(adapter);
    }
}