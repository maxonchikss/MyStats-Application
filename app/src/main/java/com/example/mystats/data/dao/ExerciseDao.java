package com.example.mystats.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mystats.model.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    long insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    LiveData<List<Exercise>> getExercisesByWorkout(int workoutId);

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    List<Exercise> getExercisesForWorkout(int workoutId);

    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    void deleteAllExercisesForWorkout(int workoutId);

    @Query("SELECT COUNT(*) FROM exercises WHERE workoutId = :workoutId")
    int getExerciseCount(int workoutId);
}