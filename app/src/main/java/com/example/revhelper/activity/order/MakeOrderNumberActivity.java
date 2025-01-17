package com.example.revhelper.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.revhelper.R;
import com.example.revhelper.databinding.ActivityMakeOrderNumberBinding;
import com.example.revhelper.model.dto.OrderDtoParcelable;
import com.example.revhelper.model.enums.RevisionType;

import java.time.LocalDate;
import java.util.List;

public class MakeOrderNumberActivity extends AppCompatActivity {

    private ActivityMakeOrderNumberBinding binding;
    private OrderDtoParcelable order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMakeOrderNumberBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initSpinner();


    }

    private void initSpinner() {

        List<String> revTypes = List.of(RevisionType.IN_TRANSIT.getRevisionTypeTitle(),
                RevisionType.AT_START_POINT.getRevisionTypeTitle(),
                RevisionType.AT_TURNROUND_POINT.getRevisionTypeTitle());

        ArrayAdapter<String> revisionSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, revTypes);
        revisionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.orderSpinnerRevtype.setAdapter(revisionSpinnerAdapter);

    }

    private OrderDtoParcelable makeOrder(String number, LocalDate date, String route, String revisionType) {

        return new OrderDtoParcelable(number, date, route, revisionType);

    }


}