package com.example.revhelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revhelper.R;
import com.example.revhelper.model.Violation;

import java.util.List;

public class ViolationAdapterOnClick extends RecyclerView.Adapter<ViolationAdapterOnClick.ViolationViewHolder> {

    private List<Violation> violationList;
    private ViolationAdapterOnClick.OnItemClickListener listener;

    public ViolationAdapterOnClick(List<Violation> violationList, ViolationAdapterOnClick.OnItemClickListener listener) {
        this.violationList = violationList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Violation violationString);
    }

    @NonNull
    @Override
    public ViolationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.violation_item_view, parent, false);
        return new ViolationAdapterOnClick.ViolationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViolationAdapterOnClick.ViolationViewHolder holder, int position) {
        Violation violation = violationList.get(position);
        holder.violationName.setText(violation.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(violation));
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
