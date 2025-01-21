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
import com.example.revhelper.model.Worker;

import java.util.List;

public class WorkerRecyclerViewAdapter extends RecyclerView.Adapter<WorkerRecyclerViewAdapter.WorkerViewHolder> {

    private Context context;
    private List<Worker> workerList;
    private OnItemClickListener onItemClickListener;
    private OnItemDeleteListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(Worker worker);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Worker worker);
    }

    public WorkerRecyclerViewAdapter(Context context, List<Worker> workerList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.workerList = workerList;
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Worker> updatedList) {
        this.workerList = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.worker_item_view, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerRecyclerViewAdapter.WorkerViewHolder holder, int position) {
        Worker worker = workerList.get(position);
        holder.workerName.setText(worker.getName());
        holder.workerJob.setText(worker.getJobTitle());
        holder.itemView.setOnClickListener(v -> showPopupMenu(v, position));
    }

    @Override
    public int getItemCount() {
        return workerList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView workerName, workerJob;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            workerName = itemView.findViewById(R.id.worker_name);
            workerJob = itemView.findViewById(R.id.worker_job);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.item_menu_coach);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.delete_item) {
                if (deleteListener != null) {
                    deleteListener.onItemDelete(workerList.get(position));
                }
                workerList.remove(position);
                notifyItemRemoved(position);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

}
