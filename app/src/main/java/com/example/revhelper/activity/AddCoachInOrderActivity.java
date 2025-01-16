package com.example.revhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityAddCoachInOrderBinding;
import com.example.revhelper.databinding.ActivityOrderBinding;
import com.example.revhelper.services.CheckService;

public class AddCoachInOrderActivity extends AppCompatActivity {

    private ActivityAddCoachInOrderBinding binding;
    private CheckService cheker = new CheckService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddCoachInOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.saveCoach.setOnClickListener((v -> {

            if (binding.coachNumberInput.getText() != null) {
                String coachNumber = binding.coachNumberInput.getText().toString();
                if (!cheker.checkCoachRegex(coachNumber)) {
                    Toast toast = Toast.makeText(this, "Неверный номер вагона",
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("COACH", coachNumber);
                setResult(RESULT_OK, resultIntent);
                finish();

            } else {
                Toast toast = Toast.makeText(this, "Введите номер вагона",
                        Toast.LENGTH_LONG);
                toast.show();
            }

        }));
    }
}