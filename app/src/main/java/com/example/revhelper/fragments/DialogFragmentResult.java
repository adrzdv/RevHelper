package com.example.revhelper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DialogFragmentResult extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String train = getArguments().getString("train");
        String coach = getArguments().getString("coach");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        boolean isTrain = train == null;
        boolean isCoach = coach == null;

        if (!isTrain && isCoach) {

            return builder
                    .setTitle("Результат поиска")
                    .setMessage(train + "\n")
                    .setPositiveButton("OK", null)
                    .create();

        } else if (isTrain && !isCoach) {

            return builder
                    .setTitle("Результат поиска")
                    .setMessage("Автоинформатор: " + coach)
                    .setPositiveButton("OK", null)
                    .create();

        }

        return builder
                .setTitle("Результат поиска")
                .setMessage(train + "\n" + "Автоинформатор: " + coach)
                .setPositiveButton("OK", null)
                .create();

    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_result_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("Результат поиска:");
    }
     */
}
