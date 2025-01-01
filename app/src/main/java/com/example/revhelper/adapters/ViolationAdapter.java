package com.example.revhelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revhelper.R;
import com.example.revhelper.dto.CoachRepresentViewDto;
import com.example.revhelper.model.Violation;

import java.util.List;

public class ViolationAdapter extends RecyclerView.Adapter<ViolationAdapter.ViolationViewHolder> {

    private List<Violation> violationList;

    public ViolationAdapter(List<Violation> violationList) {
        this.violationList = violationList;
    }

    public interface OnItemClickListener {
        void onItemClick(String violationString);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Violation> updatedList) {
        this.violationList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.violation_item_view, parent, false);
        return new ViolationAdapter.ViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationAdapter.ViolationViewHolder holder, int position) {
        Violation violation = violationList.get(position);
        holder.violationName.setText(violation.getName());
    }

    @Override
    public int getItemCount() {

        return violationList.size();
    }

    public static class ViolationViewHolder extends RecyclerView.ViewHolder {
        TextView violationName;

        public ViolationViewHolder(@NonNull View itemView) {
            super(itemView);
            violationName = itemView.findViewById(R.id.violation_name);
        }
    }
}
