package com.example.revhelper.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;
import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityMainBinding;
import com.example.revhelper.model.Coach;
import com.example.revhelper.model.DialogFragmentResult;
import com.example.revhelper.model.TrainDto;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppDatabase appDb;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        appDb = AppRev.getDb();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button startButton = binding.startButton;
        startButton.setOnClickListener(this);
        binding.exitButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.startButton) {
            Editable searchText = binding.TrainTextInput.getText();

            if (searchText.toString().isEmpty()) {
                Toast toast = Toast.makeText(this, "Введите номер поезда",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            String search = searchText.toString();
            String coachFromActivity = binding.CoachTextInput.getText().toString();

            if (!checkSearchText(search)) {
                Toast toast = Toast.makeText(this, "Неверный формат ввода номера",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            if (coachFromActivity.equals("")) {
                Toast toast = Toast.makeText(this, "Введите номер штабного вагона",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            Coach coach = appDb.coachDao().findByCoach(coachFromActivity);

            TrainDto trainDto = appDb.trainDao().findByNumberWithDep(search);

            if (trainDto == null) {
                Toast toast = Toast.makeText(this, "Поезд не найден",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            DialogFragmentResult dialog = new DialogFragmentResult();
            Bundle args = new Bundle();
            if (coach != null) {
                args.putString("coach", "Да");
            } else {
                args.putString("coach", "Нет");
            }
            args.putString("train", trainDto.toString());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");

        } else if (v.getId() == R.id.exitButton) {
            this.finish();
        }

    }

    private boolean checkSearchText(String string) {

        char[] str = string.toCharArray();

        if (str.length != 4) {
            return false;
        }

        for (int i = 0; i < str.length - 2; i++) {
            if (!Character.isDigit(str[i])) {
                return false;
            }
        }

        if (!Character.isLetter(str[str.length - 1])) {
            return false;
        }

        return true;
    }


}