package com.example.mystats.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mystats.R;
import com.example.mystats.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TimerActivity extends AppCompatActivity {
    private TextView tvTimer, tvNextExercise;
    private Button btnStart, btnPause, btnReset, btnAdd30, btnSkip;
    private BottomNavigationView bottomNavigation;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        int restTime = SharedPreferencesManager.getInstance(this).getRestTime() * 1000;
        timeLeftInMillis = restTime;

        initViews();
        updateTimerDisplay();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tvTimer);
        tvNextExercise = findViewById(R.id.tvNextExercise);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnAdd30 = findViewById(R.id.btnAdd30);
        btnSkip = findViewById(R.id.btnSkip);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Получаем название тренировки
        String workoutName = getIntent().getStringExtra("workout_name");
        if (workoutName != null) {
            tvNextExercise.setText("Тренировка: " + workoutName);
        } else {
            tvNextExercise.setText("Отдых между подходами");
        }
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        btnAdd30.setOnClickListener(v -> add30Seconds());
        btnSkip.setOnClickListener(v -> skipRest());
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_timer);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                if (isRunning) pauseTimer();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_timer) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                if (isRunning) pauseTimer();
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void startTimer() {
        if (!isRunning) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerDisplay();
                }

                @Override
                public void onFinish() {
                    timeLeftInMillis = 0;
                    updateTimerDisplay();
                    isRunning = false;
                    btnStart.setEnabled(true);
                    btnPause.setEnabled(false);
                    showRestCompleteDialog();
                }
            }.start();
            isRunning = true;
            btnStart.setEnabled(false);
            btnPause.setEnabled(true);
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
            btnStart.setEnabled(true);
            btnPause.setEnabled(false);
        }
    }

    private void resetTimer() {
        pauseTimer();
        int restTime = SharedPreferencesManager.getInstance(this).getRestTime() * 1000;
        timeLeftInMillis = restTime;
        updateTimerDisplay();
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
    }

    private void add30Seconds() {
        timeLeftInMillis += 30000;
        updateTimerDisplay();
        Toast.makeText(this, "+30 секунд", Toast.LENGTH_SHORT).show();
    }

    private void skipRest() {
        if (isRunning) {
            pauseTimer();
        }
        new AlertDialog.Builder(this)
                .setTitle("Завершить отдых?")
                .setMessage("Вы уверены, что хотите пропустить отдых?")
                .setPositiveButton("Да", (dialog, which) -> {
                    Toast.makeText(this, "Продолжайте тренировку!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    private void showRestCompleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("⏰ Отдых завершён!")
                .setMessage("Время отдыха закончилось. Готовы продолжить?")
                .setPositiveButton("Продолжить", (dialog, which) -> finish())
                .setNeutralButton("Добавить ещё 30 сек", (dialog, which) -> {
                    add30Seconds();
                    startTimer();
                })
                .show();
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}