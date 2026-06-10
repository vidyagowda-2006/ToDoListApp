package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> taskList;
    private Runnable saveCallback;
    private OnTaskLongClickListener longClickListener;
    public interface OnTaskLongClickListener{
        void onTaskLongClick(int position);
    }

    public TaskAdapter(ArrayList<Task> taskList, Runnable saveCallback,OnTaskLongClickListener longClickListener) {
        this.taskList = taskList;
        this.saveCallback=saveCallback;
        this.longClickListener=longClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBox.setText(taskList.get(position).getTitle());
        holder.checkBox.setChecked(taskList.get(position).isCompleted());
        holder.checkBox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                taskList.get(holder.getBindingAdapterPosition()).setCompleted(isChecked);
                saveCallback.run();

            }

        });
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onTaskLongClick(holder.getBindingAdapterPosition());
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxTask);
        }
    }
}


