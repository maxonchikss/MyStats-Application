package com.example.mystats.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mystats.R;
import com.example.mystats.model.Exercise;
import com.example.mystats.model.Workout;
import com.example.mystats.utils.Constants;
import com.example.mystats.utils.SharedPreferencesManager;
import com.example.mystats.viewmodel.WorkoutViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class CreateWorkoutActivity extends AppCompatActivity {
    private WorkoutViewModel viewModel;
    private TextInputEditText etWorkoutName;
    private AutoCompleteTextView actvCategory;
    private LinearLayout layoutExercises;
    private TextView btnAddExercise, btnSave;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        viewModel = new WorkoutViewModel(getApplication());
        currentUserId = SharedPreferencesManager.getInstance(this).getUserId();

        initViews();
        setupCategorySelector();
        setupClickListeners();
    }

    private void initViews() {
        etWorkoutName = findViewById(R.id.etWorkoutName);
        actvCategory = findViewById(R.id.actvCategory);
        layoutExercises = findViewById(R.id.layoutExercises);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnSave = findViewById(R.id.btnSave);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupCategorySelector() {
        String[] categories = Constants.CATEGORIES;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnAddExercise.setOnClickListener(v -> addExerciseField());
        btnSave.setOnClickListener(v -> saveWorkout());
    }

    private void addExerciseField() {
        View exerciseView = getLayoutInflater().inflate(R.layout.item_exercise_create, layoutExercises, false);

        TextInputEditText etExerciseName = exerciseView.findViewById(R.id.etExerciseNameCreate);
        TextInputEditText etSets = exerciseView.findViewById(R.id.etSetsCreate);
        TextInputEditText etReps = exerciseView.findViewById(R.id.etRepsCreate);
        TextView btnRemove = exerciseView.findViewById(R.id.btnDeleteExerciseCreate);

        btnRemove.setOnClickListener(v -> layoutExercises.removeView(exerciseView));

        layoutExercises.addView(exerciseView);
    }

    private void saveWorkout() {
        String workoutName = etWorkoutName.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();

        if (workoutName.isEmpty()) {
            Toast.makeText(this, "Введите название тренировки", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Выберите категорию", Toast.LENGTH_SHORT).show();
            return;
        }

        if (layoutExercises.getChildCount() == 0) {
            Toast.makeText(this, "Добавьте хотя бы одно упражнение", Toast.LENGTH_SHORT).show();
            return;
        }

        Workout workout = new Workout(workoutName, category, currentUserId);
        workout.setPopular(false);
        workout.setDayOfWeek(null);

        long workoutId = viewModel.insertWorkoutSync(workout);

        for (int i = 0; i < layoutExercises.getChildCount(); i++) {
            View exerciseView = layoutExercises.getChildAt(i);

            TextInputEditText etExerciseName = exerciseView.findViewById(R.id.etExerciseNameCreate);
            TextInputEditText etSets = exerciseView.findViewById(R.id.etSetsCreate);
            TextInputEditText etReps = exerciseView.findViewById(R.id.etRepsCreate);

            String exerciseName = etExerciseName.getText().toString().trim();
            String setsStr = etSets.getText().toString().trim();
            String repsStr = etReps.getText().toString().trim();

            if (!exerciseName.isEmpty()) {
                int sets = 3;
                int reps = 10;

                try {
                    if (!setsStr.isEmpty()) sets = Integer.parseInt(setsStr);
                    if (!repsStr.isEmpty()) reps = Integer.parseInt(repsStr);
                } catch (NumberFormatException e) {
                }

                Exercise exercise = new Exercise((int) workoutId, exerciseName, sets, reps);
                viewModel.insertExercise(exercise);
            }
        }

        Toast.makeText(this, "Тренировка сохранена", Toast.LENGTH_SHORT).show();
        finish();
    }
}