package com.example.mystats.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercises",
        foreignKeys = @ForeignKey(
                entity = Workout.class,
                parentColumns = "id",
                childColumns = "workoutId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "workoutId")}
)
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int workoutId;
    private String name;
    private int sets;
    private int reps;

    public Exercise(int workoutId, String name, int sets, int reps) {
        this.workoutId = workoutId;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getWorkoutId() { return workoutId; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
}