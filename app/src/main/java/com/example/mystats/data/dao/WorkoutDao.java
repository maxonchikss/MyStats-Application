package com.example.mystats.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mystats.model.Workout;
import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("SELECT * FROM workouts WHERE userId = :userId AND isPopular = 0 AND dayOfWeek IS NULL")
    LiveData<List<Workout>> getUserWorkouts(int userId);

    @Query("SELECT * FROM workouts WHERE isPopular = 1")
    LiveData<List<Workout>> getPopularWorkouts();

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    Workout getWorkoutById(int workoutId);

    @Query("SELECT * FROM workouts WHERE userId = :userId AND dayOfWeek = :day")
    LiveData<List<Workout>> getWorkoutsByDay(int userId, String day);

    @Query("SELECT * FROM workouts WHERE userId = :userId AND dayOfWeek = :day")
    List<Workout> getWorkoutsForDay(int userId, String day);
}