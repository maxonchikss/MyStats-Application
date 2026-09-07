package com.example.mystats.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mystats.data.repository.WorkoutRepository;
import com.example.mystats.model.Exercise;
import com.example.mystats.model.Workout;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;
    private LiveData<List<Workout>> popularWorkouts;
    private LiveData<List<Workout>> userWorkouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
    }

    public LiveData<List<Workout>> getPopularWorkouts() {
        if (popularWorkouts == null) {
            popularWorkouts = repository.getPopularWorkouts();
        }
        return popularWorkouts;
    }

    public LiveData<List<Workout>> getUserWorkouts(int userId) {
        if (userWorkouts == null) {
            userWorkouts = repository.getUserWorkouts(userId);
        }
        return userWorkouts;
    }

    public LiveData<List<Workout>> getWorkoutsByDay(int userId, String day) {
        return repository.getWorkoutsByDay(userId, day);
    }

    public List<Workout> getWorkoutsForDay(int userId, String day) {
        return repository.getWorkoutsForDay(userId, day);
    }

    public long insertWorkoutSync(Workout workout) {
        return repository.insertWorkoutSync(workout);
    }

    public void insertWorkout(Workout workout) {
        repository.insertWorkout(workout);
    }

    public void insertExercise(Exercise exercise) {
        repository.insertExercise(exercise);
    }

    public void updateWorkout(Workout workout) {
        repository.updateWorkout(workout);
    }

    public void deleteWorkout(Workout workout) {
        repository.deleteWorkout(workout);
    }

    public List<Exercise> getExercisesForWorkout(int workoutId) {
        return repository.getExercisesForWorkout(workoutId);
    }
}