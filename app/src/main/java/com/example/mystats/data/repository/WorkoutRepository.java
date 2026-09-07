package com.example.mystats.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mystats.data.dao.ExerciseDao;
import com.example.mystats.data.dao.UserDao;
import com.example.mystats.data.dao.WorkoutDao;
import com.example.mystats.data.database.AppDatabase;
import com.example.mystats.model.Exercise;
import com.example.mystats.model.User;
import com.example.mystats.model.Workout;

import java.util.List;

public class WorkoutRepository {
    private final WorkoutDao workoutDao;
    private final ExerciseDao exerciseDao;
    private final UserDao userDao;

    public WorkoutRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        workoutDao = database.workoutDao();
        exerciseDao = database.exerciseDao();
        userDao = database.userDao();
    }

    public LiveData<List<Workout>> getUserWorkouts(int userId) {
        return workoutDao.getUserWorkouts(userId);
    }

    public LiveData<List<Workout>> getPopularWorkouts() {
        return workoutDao.getPopularWorkouts();
    }

    public LiveData<List<Exercise>> getExercises(int workoutId) {
        return exerciseDao.getExercisesByWorkout(workoutId);
    }

    public LiveData<List<Workout>> getWorkoutsByDay(int userId, String day) {
        return workoutDao.getWorkoutsByDay(userId, day);
    }

    public List<Workout> getWorkoutsForDay(int userId, String day) {
        return workoutDao.getWorkoutsForDay(userId, day);
    }

    public long insertWorkoutSync(Workout workout) {
        return workoutDao.insert(workout);
    }

    public void insertWorkout(Workout workout) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long workoutId = workoutDao.insert(workout);
            workout.setId((int) workoutId);
        });
    }

    public void insertExercise(Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.insert(exercise);
        });
    }

    public void updateWorkout(Workout workout) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDao.update(workout);
        });
    }

    public void deleteWorkout(Workout workout) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.deleteAllExercisesForWorkout(workout.getId());
            workoutDao.delete(workout);
        });
    }

    public void updateUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
        });
    }

    public User login(String email, String password) {
        return userDao.login(email, password);
    }

    public long registerUser(User user) {
        return userDao.insert(user);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public List<Exercise> getExercisesForWorkout(int workoutId) {
        return exerciseDao.getExercisesForWorkout(workoutId);
    }
}