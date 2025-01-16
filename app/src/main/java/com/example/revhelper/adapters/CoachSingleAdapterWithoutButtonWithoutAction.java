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

public class CoachSingleAdapterWithoutButtonWithoutAction extends RecyclerView.Adapter<CoachSingleAdapterWithoutButtonWithoutAction.CoachViewHolder> {

    private List<CoachOnRevision> coachList;
    private Context context;

    public CoachSingleAdapterWithoutButtonWithoutAction(Context context, List<CoachOnRevision> coachList) {
        this.context = context;
        this.coachList = coachList;
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
        return new CoachSingleAdapterWithoutButtonWithoutAction.CoachViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CoachSingleAdapterWithoutButtonWithoutAction.CoachViewHolder holder, int position) {
        CoachOnRevision coach = coachList.get(position);
        holder.coachNumber.setText(coach.getCoachNumber());
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
