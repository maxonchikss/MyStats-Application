package com.example.mystats.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystats.R;
import com.example.mystats.adapter.MyWorkoutAdapter;
import com.example.mystats.model.Workout;
import com.example.mystats.utils.SharedPreferencesManager;
import com.example.mystats.viewmodel.WorkoutViewModel;

import java.util.ArrayList;

public class MyWorkoutsFragment extends Fragment {

    private RecyclerView rvMyWorkouts;
    private MyWorkoutAdapter adapter;
    private WorkoutViewModel viewModel;
    private String selectedDay = "Пн";
    private int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (requireActivity().getIntent() != null) {
            selectedDay = requireActivity().getIntent().getStringExtra("SELECTED_DAY");
            if (selectedDay == null) selectedDay = "Пн";
        }

        currentUserId = SharedPreferencesManager.getInstance(requireContext()).getUserId();

        rvMyWorkouts = view.findViewById(R.id.rvMyWorkouts);
        viewModel = new WorkoutViewModel(requireActivity().getApplication());

        adapter = new MyWorkoutAdapter(requireContext(), viewModel, selectedDay, currentUserId);
        adapter.setOnDeleteClickListener(workout -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Удалить тренировку")
                    .setMessage("Вы уверены, что хотите удалить \"" + workout.getName() + "\"?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        viewModel.deleteWorkout(workout);
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        rvMyWorkouts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyWorkouts.setAdapter(adapter);

        loadMyWorkouts();
    }

    private void loadMyWorkouts() {
        viewModel.getUserWorkouts(currentUserId).observe(getViewLifecycleOwner(), workouts -> {
            if (workouts != null) {
                adapter.submitList(new ArrayList<>(workouts));
            }
        });
    }
}