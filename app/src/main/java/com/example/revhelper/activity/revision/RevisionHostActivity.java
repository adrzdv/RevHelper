package com.example.revhelper.activity.revision;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.revhelper.R;
import com.example.revhelper.activity.SharedViewModel;
import com.example.revhelper.fragments.DialogFragmentExitConfirmation;
import com.example.revhelper.model.dto.OrderDtoParcelable;

public class RevisionHostActivity extends AppCompatActivity {

    private OrderDtoParcelable order;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_host);

        if (getIntent().getParcelableExtra("ORDER") != null) {
            order = getIntent().getParcelableExtra("ORDER");
            sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
            sharedViewModel.setOrder(order);
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_on_revision);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (navController.getCurrentDestination() != null &&
                            navController.getCurrentDestination().getId() == R.id.revisionFirstFragment) {
                        DialogFragmentExitConfirmation dialog = new DialogFragmentExitConfirmation();
                        dialog.show(getSupportFragmentManager(), "dialog");
                    } else {
                        navController.navigateUp();
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_on_revision);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}