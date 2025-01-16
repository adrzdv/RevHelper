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
import com.example.revhelper.model.dto.CoachOnRevision;

import java.util.List;

public class CoachSingleAdapterWithoutButton extends RecyclerView.Adapter<CoachSingleAdapterWithoutButton.CoachViewHolder> {

    private List<CoachOnRevision> coachList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CoachSingleAdapterWithoutButton(Context context, List<CoachOnRevision> coachList,  OnItemClickListener onItemClickListener) {
        this.context = context;
        this.coachList = coachList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CoachOnRevision coach);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<CoachOnRevision> updatedList) {
        this.coachList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coach_item_single_view_without_bttn, parent, false);
        return new CoachSingleAdapterWithoutButton.CoachViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CoachSingleAdapterWithoutButton.CoachViewHolder holder, int position) {
        CoachOnRevision coach = coachList.get(position);
        holder.coachNumber.setText(coach.getCoachNumber());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(coach));
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    public static class CoachViewHolder extends RecyclerView.ViewHolder {
        TextView coachNumber;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            coachNumber = itemView.findViewById(R.id.coach_single_number);
        }
    }
}
