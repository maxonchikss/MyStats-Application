package com.example.mystats.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystats.R;
import com.example.mystats.data.repository.WorkoutRepository;
import com.example.mystats.model.Exercise;
import com.example.mystats.model.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutMainAdapter extends ListAdapter<Workout, WorkoutMainAdapter.ViewHolder> {

    private OnStartWorkoutListener startListener;
    private OnDeleteWorkoutListener deleteListener;
    private WorkoutRepository repository;

    public WorkoutMainAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Workout> DIFF_CALLBACK = new DiffUtil.ItemCallback<Workout>() {
        @Override
        public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    public void setOnStartWorkoutListener(OnStartWorkoutListener listener) {
        this.startListener = listener;
    }

    public void setOnDeleteWorkoutListener(OnDeleteWorkoutListener listener) {
        this.deleteListener = listener;
    }

    public void setRepository(WorkoutRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workout workout = getItem(position);
        holder.bind(workout, repository);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvCategory;
        private ImageView ivExpand;
        private LinearLayout layoutExercises;
        private RecyclerView rvExercises;
        private Button btnStart;
        private ImageButton btnDelete;
        private ExerciseAdapter exerciseAdapter;
        private boolean isExpanded;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvWorkoutTitle);
            tvCategory = itemView.findViewById(R.id.tvWorkoutCategory);
            ivExpand = itemView.findViewById(R.id.ivExpand);
            layoutExercises = itemView.findViewById(R.id.layoutExercises);
            rvExercises = itemView.findViewById(R.id.rvExercises);
            btnStart = itemView.findViewById(R.id.btnStartWorkout);
            btnDelete = itemView.findViewById(R.id.btnDeleteWorkout);
            isExpanded = false;

            exerciseAdapter = new ExerciseAdapter();
            rvExercises.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvExercises.setAdapter(exerciseAdapter);

            ivExpand.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                if (isExpanded) {
                    layoutExercises.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.drawable.ic_minus);
                } else {
                    layoutExercises.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.drawable.ic_add);
                }
            });

            btnStart.setOnClickListener(v -> {
                if (startListener != null) {
                    startListener.onStartWorkout(getItem(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteWorkout(getItem(getAdapterPosition()));
                }
            });
        }

        public void bind(Workout workout, WorkoutRepository repository) {
            tvTitle.setText(workout.getName());
            tvCategory.setText(workout.getCategory());

            layoutExercises.setVisibility(View.GONE);
            ivExpand.setImageResource(R.drawable.ic_add);
            isExpanded = false;

            if (repository != null) {
                new Thread(() -> {
                    List<Exercise> exercises = repository.getExercisesForWorkout(workout.getId());
                    itemView.post(() -> {
                        exerciseAdapter.submitList(new ArrayList<>(exercises));
                    });
                }).start();
            }
        }
    }

    public interface OnStartWorkoutListener {
        void onStartWorkout(Workout workout);
    }

    public interface OnDeleteWorkoutListener {
        void onDeleteWorkout(Workout workout);
    }
}