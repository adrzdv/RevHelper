package com.example.revhelper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.activity.services.CheckService;
import com.example.revhelper.activity.services.ParseXml;
import com.example.revhelper.activity.services.SearchService;
import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.model.Train;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;
import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityMainBinding;
import com.example.revhelper.model.Coach;
import com.example.revhelper.fragments.DialogFragmentResult;
import com.example.revhelper.model.TrainDto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppDatabase appDb;
    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private static CheckService checkService;
    private static SearchService searchService;
    private static ParseXml xmlParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        appDb = AppRev.getDb();
        checkService = new CheckService();
        searchService = new SearchService(appDb, checkService);
        xmlParser = new ParseXml();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Инициализация обработчика для всплывающей активити
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            readFile(fileUri, view);
                        }
                    }
                }
        );

        Button startButton = binding.startButton;
        startButton.setOnClickListener(this);
        binding.exitButton.setOnClickListener(this);
        binding.importData.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.startButton) {

            TrainDto trainDto = null;
            Coach coach = null;

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
                    coach = searchService.searchCoach(coachFromActivity);
                } catch (CustomException e) {
                    Toast toast = Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (trainDto == null || coach == null) {
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
                    coach = searchService.searchCoach(coachFromActivity);
                } catch (CustomException e) {
                    Toast toast = Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (coach == null) {
                    sendSearchError();
                    return;
                }

            } else {
                Toast toast = Toast.makeText(this, "Выберите режим проверок",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            DialogFragmentResult dialog = new DialogFragmentResult();
            Bundle args = new Bundle();
            if (coach != null && binding.coachSwitch.isChecked()) {
                args.putString("coach", "Да");
            } else if (coach == null && binding.coachSwitch.isChecked()) {
                args.putString("coach", "Нет");
            }
            if (trainDto != null) {
                args.putString("train", trainDto.toString());
            }

            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "custom");

        } else if (v.getId() == R.id.importData) {

            importData();

        } else if (v.getId() == R.id.exitButton) {
            this.finish();
        }

    }

    private void importData() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Указываем MIME-тип файла. Например, "image/*" для изображений.
        String[] mimeTypes = {"application/xml", "text/xml"}; //добавляем возможность выбора xml разных типов
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            filePickerLauncher.launch(Intent.createChooser(intent, "Выберите файл"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Установите файловый менеджер.", Toast.LENGTH_SHORT).show();
        }

    }

    private void readFile(Uri fileUri, View v) {
        List<Train> newTrainList = new ArrayList<>();
        List<Coach> newCoachList = new ArrayList<>();

        String filePath = fileUri.getPath(); // Это может быть путь URI, не всегда файловая система
        Toast.makeText(this, "Выбран файл: " + filePath, Toast.LENGTH_LONG).show();

        ProgressBar progressBar = findViewById(R.id.progressBar);

        try {
            xmlParser.parseXml(getApplicationContext(), fileUri, appDb, progressBar, v);
        } catch (CustomException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

//        try (InputStream inputStream = getContentResolver().openInputStream(fileUri);
//             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//            }
//
//            String fileContent = stringBuilder.toString();
//            Toast.makeText(this, "Файл прочитан:\n" + fileContent, Toast.LENGTH_LONG).show();
//
//    } catch(Exception e)
//    {
//        e.printStackTrace();
//        Toast.makeText(this, "Ошибка чтения файла: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//    }
    }

    private void sendSearchError() {
        Toast.makeText(this, "Ошибка поиска",
                Toast.LENGTH_LONG).show();
    }

}