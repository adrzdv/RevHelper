package com.revhelper.revhelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revhelper.revhelper.R;

import java.util.List;

public class DepoNameAdapter extends RecyclerView.Adapter<DepoNameAdapter.DepoViewHolder> {

    private List<String> depoStringList;
    private Context context;

    public DepoNameAdapter(Context context, List<String> depoStringList) {
        this.context = context;
        this.depoStringList = depoStringList;
    }


    @NonNull
    @Override
    public DepoNameAdapter.DepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.depo_item_view, parent, false);
        return new DepoNameAdapter.DepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepoNameAdapter.DepoViewHolder holder, int position) {
        String depoNameString = depoStringList.get(position);
        holder.depoName.setText(depoNameString);
    }

    @Override
    public int getItemCount() {
        return depoStringList.size();
    }

    public static class DepoViewHolder extends RecyclerView.ViewHolder {
        TextView depoName;

        public DepoViewHolder(@NonNull View itemView) {
            super(itemView);
            depoName = itemView.findViewById(R.id.depo_view_name);
        }
    }
}
