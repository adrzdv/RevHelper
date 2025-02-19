package com.revhelper.revhelper.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.revhelper.revhelper.R;
import com.revhelper.revhelper.mapper.AttributeMapper;
import com.revhelper.revhelper.model.dto.ViolationAttribute;
import com.revhelper.revhelper.model.dto.ViolationForCoach;
import com.revhelper.revhelper.sys.AppRev;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViolationAdapter extends RecyclerView.Adapter<ViolationAdapter.ViolationViewHolder> {

    private List<ViolationForCoach> violationList;
    private Context context;

    public ViolationAdapter(Context context, List<ViolationForCoach> violationList) {
        this.context = context;
        this.violationList = violationList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ViolationForCoach> updatedList) {
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
        ViolationForCoach violation = violationList.get(position);
        holder.violationCode.setText(String.valueOf(violation.getCode()));
        holder.violationName.setText(violation.getName());
        holder.menuButton.setOnClickListener(v -> showPopupMenu(v, position));
    }

    @Override
    public int getItemCount() {

        return violationList.size();
    }

    public static class ViolationViewHolder extends RecyclerView.ViewHolder {
        TextView violationCode, violationName;
        public ImageButton menuButton;

        public ViolationViewHolder(@NonNull View itemView) {
            super(itemView);
            violationCode = itemView.findViewById(R.id.violation_code);
            violationName = itemView.findViewById(R.id.violation_name);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.item_menu);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.delete_item) {
                violationList.remove(position);
                notifyItemRemoved(position);
                return true;
            } else if (menuItem.getItemId() == R.id.add_amount) {
                showAmountInputDialog(position);
                return true;
            } else if (menuItem.getItemId() == R.id.add_resolved) {
                violationList.get(position).setResolved(true);
            } else if (menuItem.getItemId() == R.id.add_attrib) {
                showAttributesDialog(position);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showAttributesDialog(int position) {

        List<ViolationAttribute> attribs = AppRev.getDb().attributeDao()
                .getAttribsForViolation(violationList.get(position).getId())
                .stream()
                .map(AttributeMapper::fromEntityToDto)
                .collect(Collectors.toList());

        if (attribs.isEmpty()) {
            AppRev.showToast(context, "Признаки отсутствуют");
            return;
        }

        boolean[] selectedAttributes = new boolean[attribs.size()];
        String[] attributeNames = attribs.stream().map(ViolationAttribute::getAttrib)
                .toArray(String[]::new);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Выберите признаки")
                .setMultiChoiceItems(attributeNames, selectedAttributes, (dialog, which, isChecked) -> {
                    selectedAttributes[which] = isChecked;
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    List<ViolationAttribute> selectedList = new ArrayList<>();

                    for (int i = 0; i < attribs.size(); i++) {
                        if (selectedAttributes[i]) {
                            selectedList.add(attribs.get(i));
                        }
                    }
                    violationList.get(position).setAttributes(selectedList);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showAmountInputDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Введите количество");

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String amountText = input.getText().toString();
            if (!amountText.isEmpty()) {
                try {
                    int amount = Integer.parseInt(amountText);
                    updateViolationAmount(position, amount);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Некорректные данные", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateViolationAmount(int position, int amount) {

        ViolationForCoach violation = violationList.get(position);
        violation.setAmount(amount);
        notifyItemChanged(position);
    }
}
