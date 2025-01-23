package com.example.revhelper.activity.deprecated;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityAddMasterBinding;
import com.example.revhelper.model.enums.WorkerJob;
import com.example.revhelper.sys.AppRev;

@Deprecated
public class AddMasterActivity extends AppCompatActivity {

    private ActivityAddMasterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMasterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String[] masterTypes = {WorkerJob.LNP.getJobTitle(),
                WorkerJob.PEM.getJobTitle(),
                WorkerJob.DVR.getJobTitle(),
                WorkerJob.MVB.getJobTitle()};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, masterTypes);
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        binding.masterTypeSpinner.setAdapter(adapter);

        binding.saveMaster.setOnClickListener((v -> {
            String worker = binding.masterTypeSpinner.getSelectedItem().toString();
            String surname = binding.lastNameInput.getText().toString();

            if (surname.isEmpty() || surname.isBlank()) {
                Toast toast = Toast.makeText(this, "Введите ФИО работника",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (!AppRev.getChecker().checkWorkerDataRegex(surname)) {
                Toast toast = Toast.makeText(this, "Неверный формат ФИО",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("WORKER", worker);
            resultIntent.putExtra("SURNAME", surname);
            setResult(RESULT_OK, resultIntent);
            finish();
        }));

    }
}