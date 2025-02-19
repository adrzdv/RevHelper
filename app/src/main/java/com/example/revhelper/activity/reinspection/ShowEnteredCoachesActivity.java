package com.example.revhelper.activity.reinspection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revhelper.R;
import com.example.revhelper.adapters.CoachSingleAdapter;
import com.example.revhelper.databinding.ActivityShowEnteredCoachesBinding;
import com.example.revhelper.model.dto.RevCoach;

import java.util.ArrayList;
import java.util.List;

public class ShowEnteredCoachesActivity extends AppCompatActivity {

    private ActivityShowEnteredCoachesBinding binding;
    private CoachSingleAdapter adapter;
    private List<RevCoach> coachList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityShowEnteredCoachesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().getParcelableArrayListExtra("COACHES") != null) {
            coachList = getIntent().getParcelableArrayListExtra("COACHES");
        }

        adapter = new CoachSingleAdapter(this, coachList, coach -> {
            coachList.remove(coach);
        });
        RecyclerView recyclerView = binding.enteredCoachListRecycler;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra("NEW_COACHES", new ArrayList<>(coachList));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}