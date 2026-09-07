package com.example.mystats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystats.R;
import com.example.mystats.model.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new DiffUtil.ItemCallback<Exercise>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getSets() == newItem.getSets()
                    && oldItem.getReps() == newItem.getReps();
        }
    };

    public ExerciseAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = getItem(position);
        holder.tvName.setText(exercise.getName());
        holder.tvSets.setText(exercise.getSets() + " × " + exercise.getReps());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSets;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExerciseName);
            tvSets = itemView.findViewById(R.id.tvExerciseSets);
        }
    }
}