package com.example.revhelper.adapters;

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

import com.example.revhelper.R;
import com.example.revhelper.model.dto.CoachRepresentViewDto;

import java.util.List;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder> {

    private List<CoachRepresentViewDto> coachListForRepresent;
    private OnItemClickListener listener;
    private Context context;
    private final OnCoachDeleteListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(CoachRepresentViewDto coach);
    }

    public interface OnCoachDeleteListener {
        void onCoachDelete(CoachRepresentViewDto coach);
    }

    public CoachAdapter(Context context, List<CoachRepresentViewDto> coachList, OnItemClickListener listener, OnCoachDeleteListener deleteListener) {
        this.context = context;
        this.coachListForRepresent = coachList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<CoachRepresentViewDto> updatedList) {
        this.coachListForRepresent = updatedList;
        notifyDataSetChanged();  // Обновление данных
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coach_item_view, parent, false);
        return new CoachViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CoachAdapter.CoachViewHolder holder, int position) {
        CoachRepresentViewDto coach = coachListForRepresent.get(position);
        holder.coachNumber.setText(coach.getCoachNumber());
        holder.coachWorker.setText(coach.getCoachWorker());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(coach));
        holder.menuButton.setOnClickListener(view -> showPopupMenu(view, position));
    }

    @Override
    public int getItemCount() {
        return coachListForRepresent.size();
    }

    public static class CoachViewHolder extends RecyclerView.ViewHolder {
        TextView coachNumber, coachWorker;
        public ImageButton menuButton;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            coachNumber = itemView.findViewById(R.id.coachNumber);
            coachWorker = itemView.findViewById(R.id.coachWorker);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.item_menu_coach); // Подключаем меню из res/menu/item_menu.xml

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.delete_item) {
                // Удаляем элемент из списка
                if (deleteListener != null) {
                    deleteListener.onCoachDelete(coachListForRepresent.get(position));
                }
                coachListForRepresent.remove(position);
                notifyItemRemoved(position);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
