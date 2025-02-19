package com.revhelper.revhelper.activity.information;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.revhelper.revhelper.databinding.ActivitySearchBinding;
import com.revhelper.revhelper.fragments.DialogFragmentSearchResult;
import com.revhelper.revhelper.services.CheckService;
import com.revhelper.revhelper.services.SearchService;
import com.revhelper.revhelper.exceptions.CustomException;
import com.revhelper.revhelper.sys.AppDatabase;
import com.revhelper.revhelper.sys.AppRev;
import com.revhelper.revhelper.R;
import com.revhelper.revhelper.model.entity.MainCoach;
import com.revhelper.revhelper.model.dto.TrainDto;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySearchBinding binding;
    private static SearchService searchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        AppDatabase appDb = AppRev.getDb();
        CheckService checkService = new CheckService();
        searchService = new SearchService(appDb, checkService);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.startButton.setOnClickListener(this);
        binding.backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.startButton) {
            TrainDto trainDto = null;
            MainCoach mainCoach = null;

            Editable searchText = binding.TrainTextInput.getText();
            String coachFromActivity = binding.CoachTextInput.getText().toString();

            if (binding.trainSwitch.isChecked() && binding.coachSwitch.isChecked()) {
                try {
                    trainDto = searchService.searchTrain(searchText);
                } catch (CustomException e) {
                    Toast toast = Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                try {
                    mainCoach = searchService.searchCoach(coachFromActivity);
                } catch (CustomException e) {
                    Toast toast = Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (trainDto == null || mainCoach == null) {
                    sendSearchError();
                    return;
                }

            } else if (binding.trainSwitch.isChecked() && !binding.coachSwitch.isChecked()) {

                try {
                    trainDto = searchService.searchTrain(searchText);
                } catch (CustomException e) {
                    Toast toast = Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }


            } else if (!binding.trainSwitch.isChecked() && binding.coachSwitch.isChecked()) {

                try {
                    mainCoach = searchService.searchCoach(coachFromActivity);
                } catch (CustomException e) {
                    Toast toast = Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (mainCoach == null) {
                    sendSearchError();
                    return;
                }

            } else {
                Toast toast = Toast.makeText(this, "Выберите режим проверок",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            DialogFragmentSearchResult dialog = new DialogFragmentSearchResult();
            Bundle args = new Bundle();
            if (mainCoach != null && binding.coachSwitch.isChecked()) {
                args.putString("mainCoach", "Да");
            } else if (mainCoach == null && binding.coachSwitch.isChecked()) {
                args.putString("mainCoach", "Нет");
            }
            if (trainDto != null) {
                args.putString("train", trainDto.toString());
            }

            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");

        } else if (v.getId() == R.id.backButton) {
            this.finish();
        }

    }

    private void sendSearchError() {
        Toast.makeText(this, "Ошибка поиска",
                Toast.LENGTH_LONG).show();
    }

}