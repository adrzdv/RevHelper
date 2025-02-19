package com.revhelper.revhelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.model.dto.ViolationAttribute;
import com.revhelper.revhelper.model.dto.ViolationForCoach;

import java.util.List;

public class ViolationAdapterForReinspection extends RecyclerView.Adapter<ViolationAdapterForReinspection.ViolationViewHolder> {

    private List<ViolationForCoach> violationList;

    public ViolationAdapterForReinspection(List<ViolationForCoach> violationList) {
        this.violationList = violationList;
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.violation_item_view_without_button, parent, false);
        return new ViolationAdapterForReinspection.ViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationAdapterForReinspection.ViolationViewHolder holder, int position) {
        ViolationForCoach violation = violationList.get(position);
        holder.violationCode.setText(String.valueOf(violation.getCode()));
        StringBuilder attribs = new StringBuilder();
        if (violation.getAttributes() != null && !violation.getAttributes().isEmpty()) {
            for (ViolationAttribute attrib : violation.getAttributes()) {
                attribs.append(attrib.getAttrib()).append(" ");
            }
            holder.violationName.setText(violation.getName() + "\n\nПризнаки:\n" + attribs);
        } else {
            holder.violationName.setText(violation.getName());
        }

    }

    @Override
    public int getItemCount() {
        return violationList.size();
    }

    public static class ViolationViewHolder extends RecyclerView.ViewHolder {
        TextView violationCode, violationName;

        public ViolationViewHolder(@NonNull View itemView) {
            super(itemView);
            violationCode = itemView.findViewById(R.id.violation_code);
            violationName = itemView.findViewById(R.id.violation_name);
        }
    }

    public void updateData(List<ViolationForCoach> newViolationList) {
        this.violationList = newViolationList;
        notifyDataSetChanged();
    }
}
