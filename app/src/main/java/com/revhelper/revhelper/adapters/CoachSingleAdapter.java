package com.revhelper.revhelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.model.dto.RevCoach;

import java.util.List;

public class CoachSingleAdapter extends RecyclerView.Adapter<CoachSingleAdapter.CoachViewHolder> {

    private List<RevCoach> coachList;
    private Context context;
    private final OnSingleCoachDeleteListener deleteListener;

    public CoachSingleAdapter(Context context, List<RevCoach> coachList, OnSingleCoachDeleteListener listener) {
        this.context = context;
        this.coachList = coachList;
        this.deleteListener = listener;
    }

    public interface OnSingleCoachDeleteListener {
        void onCoachDelete(RevCoach coach);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<RevCoach> updatedList) {
        this.coachList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coach_item_single_view, parent, false);
        return new CoachSingleAdapter.CoachViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CoachSingleAdapter.CoachViewHolder holder, int position) {
        RevCoach coach = coachList.get(position);
        holder.coachNumber.setText(coach.getCoachNumber());
        holder.menuButton.setOnClickListener(view -> showPopupMenu(view, position));
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    public static class CoachViewHolder extends RecyclerView.ViewHolder {
        TextView coachNumber;
        public ImageButton menuButton;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            coachNumber = itemView.findViewById(R.id.coach_single_number);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.item_menu_coach);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.delete_item) {
                if (deleteListener != null) {
                    deleteListener.onCoachDelete(coachList.get(position));
                }
                notifyItemRemoved(position);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
