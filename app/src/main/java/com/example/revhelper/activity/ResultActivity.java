package com.example.revhelper.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityCoachBinding;
import com.example.revhelper.databinding.ActivityResultBinding;
import com.example.revhelper.databinding.ActivityRevisionBinding;
import com.example.revhelper.model.MainNodesEnum;

import java.io.Serializable;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private Map<String, String> resMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState != null) {
            resMap = (Map<String, String>) savedInstanceState.getSerializable("resMap");
        } else {
            resMap = (Map<String, String>) getIntent().getSerializableExtra("resMap");
        }

        if (resMap != null) {
            updateUI(resMap);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (resMap != null) {
            outState.putSerializable("resMap", (Serializable) resMap);
        }
    }

    private void updateUI(Map<String, String> map) {
        StringBuilder resultText = new StringBuilder();

        resultText.append("ВСЕГО НАРУШЕНИЙ: ").append(map.get("TOTAL")).append("\n");

        resultText.append("АВТОМАТИЧЕСКИЕ ДВЕРИ: ")
                .append(map.get(MainNodesEnum.AUTO_DOOR.name()))
                .append("\n")
                .append("СКУДОПП: ")
                .append(map.get(MainNodesEnum.SKUDOPP.name()))
                .append("\n");

        for (String key : map.keySet()) {
            if (!(key.equals(MainNodesEnum.SKUDOPP.name())
                    || key.equals("TOTAL") || key.equals(MainNodesEnum.PROGRESS.name())
                    || key.equals(MainNodesEnum.AUTO_DOOR.name()))) {
                resultText.append("\n")
                        .append(key)
                        .append("\nВагоны:\n")
                        .append(map.get(key))
                        .append("\n");
            }
        }

        if (resMap.containsKey(MainNodesEnum.PROGRESS.name())) {
            resultText.append("ПРОГРЕССИВНЫЕ НОРМЫ:\n").append(map.get(MainNodesEnum.PROGRESS.name()))
                    .append("\n");
        }

        binding.resultTextView.setText(resultText.toString());
    }
}