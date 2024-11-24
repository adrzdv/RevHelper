package com.example.revhelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.databinding.ActivityResultInfoBinding;
import com.example.revhelper.model.Train;

public class ResultInfo extends AppCompatActivity {

    private ActivityResultInfoBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityResultInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle args = getIntent().getExtras();

        Train train;

        if (args != null) {
            train = (Train) args.getSerializable(Train.class.getSimpleName());

            binding.resultView.setText("Number: " + train.getDirectNumber() + "/"
                    + train.getReversedNumber() + "\n"
                    +"Route: " + train.getRoute());
        }
    }
}