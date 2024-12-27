package com.example.revhelper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class DialogFragmentExitConfirmation extends DialogFragment {

    private AppCompatActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder
                .setTitle("Завершение ревизии")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("OK", (dialog, which) -> activity.finish())
                .setNegativeButton("Отмена", null)
                .create();
    }

}
