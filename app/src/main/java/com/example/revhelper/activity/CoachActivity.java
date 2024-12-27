package com.example.revhelper.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityCoachBinding;
import com.example.revhelper.databinding.ActivityStartBinding;
import com.example.revhelper.dto.CoachOnRevisionParce;
import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.CoachOnRevision;
import com.example.revhelper.services.CheckService;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoachActivity extends AppCompatActivity {

    private final CheckService checkService = new CheckService();
    private ActivityCoachBinding binding;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCoachBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText coachNumberEditText = findViewById(R.id.coachNumber);
        EditText coachWorkerEditText = findViewById(R.id.coachWorker);
        CheckBox skudoppCheckBox = findViewById(R.id.checkBoxSkudopp);
        CheckBox automaticDoorsBox = findViewById(R.id.checkBoxAutomaticDoor);
        Button saveButton = findViewById(R.id.saveCoachInfo);
        Button backButton = findViewById(R.id.returnBack);

        if (getIntent().getParcelableExtra("coach") != null) {
            CoachOnRevisionParce coach = getIntent().getParcelableExtra("coach");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String revTime = coach.getRevisionTime().format(formatter);
            binding.coachNumber.setText(coach.getCoachNumber());
            binding.coachWorker.setText(coach.getCoachWorker());
            binding.checkBoxSkudopp.setChecked(coach.isCoachSkudopp());
            binding.checkBoxAutomaticDoor.setChecked(coach.isCoachAutomaticDoor());
            binding.timestampTextView.setText(revTime);

        }

        saveButton.setOnClickListener(v -> {
            // Читаем данныe
            String coachNumber = coachNumberEditText.getText().toString();
            String coachWorker = coachWorkerEditText.getText().toString();

            if (coachNumber.isEmpty() || coachWorker.isEmpty()) {
                Toast toast = Toast.makeText(this, "ФИО или номер вагона пустые",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            if (!checkService.checkCoachRegex(coachNumber)) {
                Toast toast = Toast.makeText(this, "Неверный формат номера вагона",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            boolean skudopp = skudoppCheckBox.isChecked();
            boolean automaticDoors = automaticDoorsBox.isChecked();

            // Создаём объект
            CoachOnRevisionParce coachOnRevision = new CoachOnRevisionParce(coachNumber, coachWorker, skudopp, automaticDoors,
                    LocalDateTime.now());

            // Возвращаем объект в предыдущую активити
            Intent resultIntent = new Intent();
            resultIntent.putExtra("coach", coachOnRevision);
            setResult(RESULT_OK, resultIntent);

            // Закрываем SecondActivity
            finish();
        });

        backButton.setOnClickListener(v -> {
            finish();
        });

    }
}