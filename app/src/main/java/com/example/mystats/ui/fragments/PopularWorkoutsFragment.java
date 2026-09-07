package com.example.mystats.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystats.R;
import com.example.mystats.model.Workout;
import com.example.mystats.utils.SharedPreferencesManager;
import com.example.mystats.viewmodel.WorkoutViewModel;

import java.util.List;

public class PopularWorkoutsFragment extends Fragment {

    private WorkoutViewModel viewModel;
    private int currentUserId;
    private String selectedDay = "Пн";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workouts_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (requireActivity().getIntent() != null) {
            selectedDay = requireActivity().getIntent().getStringExtra("SELECTED_DAY");
            if (selectedDay == null) selectedDay = "Пн";
        }

        viewModel = new WorkoutViewModel(requireActivity().getApplication());
        currentUserId = SharedPreferencesManager.getInstance(requireContext()).getUserId();

        setupWorkoutCard(view, 1, R.id.ivExpand1, R.id.layoutExercises1,
                R.id.btnAddToMain1, R.id.btnAddToMy1, "Популярная тренировка на грудь");
        setupWorkoutCard(view, 2, R.id.ivExpand2, R.id.layoutExercises2,
                R.id.btnAddToMain2, R.id.btnAddToMy2, "Тренировка Тома Плаца");
        setupWorkoutCard(view, 3, R.id.ivExpand3, R.id.layoutExercises3,
                R.id.btnAddToMain3, R.id.btnAddToMy3, "Тренировка ног Рони Колемана");
    }

    private void setupWorkoutCard(View view, int number, int expandIconId, int layoutId,
                                  int btnAddMainId, int btnAddMyId, String workoutName) {
        ImageView ivExpand = view.findViewById(expandIconId);
        LinearLayout layoutExercises = view.findViewById(layoutId);
        Button btnAddToMain = view.findViewById(btnAddMainId);
        Button btnAddToMy = view.findViewById(btnAddMyId);

        ivExpand.setOnClickListener(v -> {
            boolean isVisible = layoutExercises.getVisibility() == View.VISIBLE;
            layoutExercises.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            ivExpand.setImageResource(isVisible ? R.drawable.ic_add : R.drawable.ic_minus);
        });

        btnAddToMain.setOnClickListener(v -> {
            List<Workout> existingWorkouts = viewModel.getWorkoutsForDay(currentUserId, selectedDay);

            boolean alreadyExists = false;
            for (Workout w : existingWorkouts) {
                if (w.getName().equals(workoutName)) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                Workout workout = new Workout(workoutName, "Популярная", currentUserId);
                workout.setPopular(true);
                workout.setDayOfWeek(selectedDay);
                viewModel.insertWorkout(workout);
            }

            Toast.makeText(getContext(), "Добавлено на " + selectedDay, Toast.LENGTH_SHORT).show();
        });

        btnAddToMy.setOnClickListener(v -> {
            Workout workout = new Workout(workoutName, "Популярная", currentUserId);
            workout.setPopular(false);
            workout.setDayOfWeek(null);
            viewModel.insertWorkout(workout);
            Toast.makeText(getContext(), "Добавлено в мои тренировки", Toast.LENGTH_SHORT).show();
        });
    }
}