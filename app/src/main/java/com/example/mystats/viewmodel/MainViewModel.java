package com.example.mystats.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mystats.data.repository.WorkoutRepository;
import com.example.mystats.model.Exercise;
import com.example.mystats.model.Workout;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
    }

    public LiveData<List<Workout>> getWorkoutsByDay(int userId, String day) {
        return repository.getWorkoutsByDay(userId, day);
    }

    public List<Workout> getWorkoutsForDaySync(int userId, String day) {
        return repository.getWorkoutsForDay(userId, day);
    }

    public void deleteWorkout(Workout workout) {
        repository.deleteWorkout(workout);
    }

    public void insertWorkout(Workout workout) {
        repository.insertWorkout(workout);
    }
}