package com.revhelper.revhelper.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.model.dto.ViolationDto;

import java.util.List;

public class ViolationSingleCatalogViewAdapter extends RecyclerView.Adapter<ViolationSingleCatalogViewAdapter.ViolationViewHolder> {

    private List<ViolationDto> violationList;

    public ViolationSingleCatalogViewAdapter(List<ViolationDto> violationList) {
        this.violationList = violationList;
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.violation_item_view_without_button, parent, false);
        return new ViolationSingleCatalogViewAdapter.ViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationSingleCatalogViewAdapter.ViolationViewHolder holder, int position) {
        ViolationDto violation = violationList.get(position);
        holder.violationCode.setText(String.valueOf(violation.getCode()));
        holder.violationName.setText(violation.getName());
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

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ViolationDto> newViolationList) {
        this.violationList = newViolationList;
        notifyDataSetChanged();
    }
}
