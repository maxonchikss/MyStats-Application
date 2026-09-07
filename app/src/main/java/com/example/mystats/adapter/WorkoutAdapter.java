package com.example.mystats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystats.R;
import com.example.mystats.model.Workout;

public class WorkoutAdapter extends ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder> {
    private OnWorkoutClickListener listener;
    private OnDeleteClickListener deleteListener;

    public WorkoutAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Workout> DIFF_CALLBACK = new DiffUtil.ItemCallback<Workout>() {
        @Override
        public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getCategory().equals(newItem.getCategory());
        }
    };

    public void setOnWorkoutClickListener(OnWorkoutClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = getItem(position);
        holder.bind(workout);
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWorkoutName;
        private TextView tvWorkoutDetails;
        private Button btnExpand;
        private ImageButton btnDelete;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            tvWorkoutDetails = itemView.findViewById(R.id.tvWorkoutDetails);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Workout workout) {
            tvWorkoutName.setText(workout.getName());
            tvWorkoutDetails.setText(workout.getCategory());

            btnExpand.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onWorkoutClick(workout);
                }
            });

            if (btnDelete != null && deleteListener != null) {
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(v -> {
                    if (deleteListener != null) {
                        deleteListener.onDeleteClick(workout);
                    }
                });
            } else if (btnDelete != null) {
                btnDelete.setVisibility(View.GONE);
            }
        }
    }

    public interface OnWorkoutClickListener {
        void onWorkoutClick(Workout workout);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Workout workout);
    }
}