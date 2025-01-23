package com.example.revhelper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.R;
import com.example.revhelper.activity.order.OrderHostActivity;
import com.example.revhelper.activity.reinspection.ReinspectionActivity;
import com.example.revhelper.databinding.ActivityStartBinding;
import com.example.revhelper.exceptions.CustomException;
import com.example.revhelper.services.CheckService;
import com.example.revhelper.services.ParseXml;
import com.example.revhelper.services.SearchService;
import com.example.revhelper.sys.AppDatabase;
import com.example.revhelper.sys.AppRev;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityStartBinding binding;
    private AppDatabase appDb;
    private static CheckService checkService;
    private static SearchService searchService;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private static ParseXml xmlParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        appDb = AppRev.getDb();
        xmlParser = new ParseXml();
        checkService = new CheckService();
        searchService = new SearchService(appDb, checkService);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //обработчик всплываюшщей активити
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            readFile(fileUri);
                        }
                    }
                }
        );

        binding.exitButton.setOnClickListener(this);
        binding.serviceMenu.setOnClickListener(this);
        binding.loadData.setOnClickListener(this);
        binding.revisionButton.setOnClickListener(this);
        binding.reinspectionButton.setOnClickListener(this);
        binding.openViolationCatalog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.loadData) {
            Intent intent = new Intent(StartActivity.this, SearchActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.revisionButton) {
            Intent intent = new Intent(StartActivity.this, OrderHostActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.serviceMenu) {
            importData();
        } else if (v.getId() == R.id.exitButton) {
            this.finish();
        } else if (v.getId() == R.id.reinspection_button) {
            Intent intent = new Intent(StartActivity.this, ReinspectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.open_violation_catalog) {
            Intent intent = new Intent(StartActivity.this, ViolationCatalogActivity.class);
            startActivity(intent);
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

    private void readFile(Uri fileUri) {

        String filePath = fileUri.getPath(); // Это может быть путь URI, не всегда файловая система
        Toast.makeText(this, "Выбран файл: " + filePath, Toast.LENGTH_LONG).show();

        try {
            xmlParser.parseXml(getApplicationContext(), fileUri, appDb);
        } catch (CustomException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}