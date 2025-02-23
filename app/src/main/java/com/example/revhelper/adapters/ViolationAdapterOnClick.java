package com.example.revhelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revhelper.R;
import com.example.revhelper.model.dto.ViolationDto;

import java.util.List;

public class ViolationAdapterOnClick extends RecyclerView.Adapter<ViolationAdapterOnClick.ViolationViewHolder> {

    private List<ViolationDto> violationList;
    private ViolationAdapterOnClick.OnItemClickListener listener;

    public ViolationAdapterOnClick(List<ViolationDto> violationList, ViolationAdapterOnClick.OnItemClickListener listener) {
        this.violationList = violationList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ViolationDto violationString);
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.violation_item_view_without_button, parent, false);
        return new ViolationAdapterOnClick.ViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationAdapterOnClick.ViolationViewHolder holder, int position) {
        ViolationDto violation = violationList.get(position);
        holder.violationCode.setText(String.valueOf(violation.getCode()));
        holder.violationName.setText(violation.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(violation));
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

    public void updateData(List<ViolationDto> newViolationList) {
        this.violationList = newViolationList;
        notifyDataSetChanged();
    }
}
