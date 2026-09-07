package com.example.mystats.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystats.R;
import com.example.mystats.model.Exercise;
import com.example.mystats.model.Workout;
import com.example.mystats.viewmodel.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyWorkoutAdapter extends ListAdapter<Workout, MyWorkoutAdapter.ViewHolder> {

    private OnDeleteClickListener deleteListener;
    private WorkoutViewModel viewModel;
    private String selectedDay;
    private int currentUserId;

    public MyWorkoutAdapter(Context context, WorkoutViewModel viewModel, String selectedDay, int currentUserId) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
        this.selectedDay = selectedDay != null ? selectedDay : "Пн";
        this.currentUserId = currentUserId;
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

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivExpand, ivDelete;
        private LinearLayout layoutExercises;
        private RecyclerView rvExercises;
        private Button btnAddToMain;
        private boolean isExpanded;
        private ExerciseAdapter exerciseAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMyWorkoutName);
            ivExpand = itemView.findViewById(R.id.ivMyExpand);
            ivDelete = itemView.findViewById(R.id.ivMyDelete);
            layoutExercises = itemView.findViewById(R.id.layoutMyExercises);
            rvExercises = itemView.findViewById(R.id.rvMyExercises);
            btnAddToMain = itemView.findViewById(R.id.btnAddToMain);
            isExpanded = false;

            exerciseAdapter = new ExerciseAdapter();
            rvExercises.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvExercises.setAdapter(exerciseAdapter);

            ivExpand.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                if (isExpanded) {
                    layoutExercises.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.drawable.ic_minus);
                    loadExercises(getItem(getAdapterPosition()));
                } else {
                    layoutExercises.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.drawable.ic_add);
                }
            });

            ivDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(getItem(getAdapterPosition()));
                }
            });

            btnAddToMain.setOnClickListener(v -> {
                Workout workout = getItem(getAdapterPosition());

                List<Workout> existingWorkouts = viewModel.getWorkoutsForDay(currentUserId, selectedDay);

                boolean alreadyExists = false;
                for (Workout w : existingWorkouts) {
                    if (w.getName().equals(workout.getName())) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    Workout dayWorkout = new Workout(
                            workout.getName(),
                            workout.getCategory(),
                            currentUserId
                    );
                    dayWorkout.setDayOfWeek(selectedDay);
                    dayWorkout.setPopular(false);
                    viewModel.insertWorkout(dayWorkout);

                    Toast.makeText(itemView.getContext(),
                            "Добавлено на " + selectedDay,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(itemView.getContext(),
                            "Уже добавлено на " + selectedDay,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void loadExercises(Workout workout) {
            new Thread(() -> {
                List<Exercise> exercises = viewModel.getExercisesForWorkout(workout.getId());
                itemView.post(() -> {
                    if (exercises != null && !exercises.isEmpty()) {
                        exerciseAdapter.submitList(new ArrayList<>(exercises));
                    } else {
                        exerciseAdapter.submitList(new ArrayList<>());
                    }
                });
            }).start();
        }

        public void bind(Workout workout) {
            tvName.setText(workout.getName());
            layoutExercises.setVisibility(View.GONE);
            ivExpand.setImageResource(R.drawable.ic_add);
            isExpanded = false;
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Workout workout);
    }
}