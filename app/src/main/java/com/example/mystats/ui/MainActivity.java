package com.example.mystats.ui;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystats.R;
import com.example.mystats.adapter.WorkoutMainAdapter;
import com.example.mystats.data.repository.WorkoutRepository;
import com.example.mystats.model.Workout;
import com.example.mystats.utils.Constants;
import com.example.mystats.utils.SharedPreferencesManager;
import com.example.mystats.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private LinearLayout layoutDays;
    private RecyclerView rvWorkouts;
    private TextView tvEmpty, tvSeries;
    private ImageView ivSettings;
    private WorkoutMainAdapter adapter;
    private String selectedDay = "Пн";
    private int currentUserId;
    private List<String> daysWithWorkouts = new ArrayList<>();
    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    private final ActivityResultLauncher<Intent> createWorkoutLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshUI();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new MainViewModel(getApplication());
        currentUserId = SharedPreferencesManager.getInstance(this).getUserId();

        initViews();
        setupDays();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();
        loadWorkoutsForDay();

        new android.os.Handler(getMainLooper()).postDelayed(() -> {
            checkAndShowNotification();
        }, 1000);
    }

    private void initViews() {
        layoutDays = findViewById(R.id.layoutDays);
        rvWorkouts = findViewById(R.id.rvWorkouts);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvSeries = findViewById(R.id.tvSeries);
        ivSettings = findViewById(R.id.ivSettings);
    }

    private void setupDays() {
        String[] days = Constants.DAYS_OF_WEEK;
        layoutDays.removeAllViews();

        refreshDaysData();
        updateSeries();

        for (int i = 0; i < days.length; i++) {
            View dayView = getLayoutInflater().inflate(R.layout.item_day, layoutDays, false);
            ImageView ivDay = dayView.findViewById(R.id.ivDay);
            TextView tvDay = dayView.findViewById(R.id.tvDayName);

            tvDay.setText(days[i]);

            if (daysWithWorkouts.contains(days[i])) {
                ivDay.setImageResource(R.drawable.ic_circle_green);
            } else {
                ivDay.setImageResource(R.drawable.ic_circle);
            }

            final String currentDay = days[i];
            ivDay.setOnClickListener(v -> {
                selectedDay = currentDay;
                loadWorkoutsForDay();
                updateActiveDayUI(currentDay);
            });

            layoutDays.addView(dayView);
        }

        updateActiveDayUI(selectedDay);
    }

    private void refreshDaysData() {
        daysWithWorkouts.clear();
        String[] days = Constants.DAYS_OF_WEEK;
        for (String day : days) {
            List<Workout> workouts = viewModel.getWorkoutsForDaySync(currentUserId, day);
            if (workouts != null && !workouts.isEmpty()) {
                daysWithWorkouts.add(day);
            }
        }
    }

    private void updateActiveDayUI(String activeDay) {
        for (int i = 0; i < layoutDays.getChildCount(); i++) {
            View dayView = layoutDays.getChildAt(i);
            ImageView ivDay = dayView.findViewById(R.id.ivDay);
            TextView tvDay = dayView.findViewById(R.id.tvDayName);
            String dayName = tvDay.getText().toString();

            if (dayName.equals(activeDay)) {
                ivDay.setColorFilter(ContextCompat.getColor(this, R.color.primary));
            } else {
                ivDay.clearColorFilter();
            }
        }
    }

    private void updateSeries() {
        int series = calculateSeries();
        if (series > 0) {
            tvSeries.setText("Вы в ударе!");
        } else {
            tvSeries.setText("Начните тренироваться!");
        }
    }

    private int calculateSeries() {
        if (daysWithWorkouts.isEmpty()) return 0;

        Calendar calendar = Calendar.getInstance();
        int currentDayIndex = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (currentDayIndex < 0) currentDayIndex = 0;
        if (currentDayIndex > 6) currentDayIndex = 6;

        String[] days = Constants.DAYS_OF_WEEK;
        int series = 0;

        for (int i = currentDayIndex; i >= 0; i--) {
            if (daysWithWorkouts.contains(days[i])) {
                series++;
            } else {
                break;
            }
        }
        return series;
    }

    private void setupRecyclerView() {
        adapter = new WorkoutMainAdapter();
        adapter.setOnStartWorkoutListener(workout -> {
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra("workout_id", workout.getId());
            intent.putExtra("workout_name", workout.getName());
            startActivity(intent);
        });

        adapter.setOnDeleteWorkoutListener(workout -> {
            new AlertDialog.Builder(this)
                    .setTitle("Удалить тренировку")
                    .setMessage("Удалить \"" + workout.getName() + "\" с этого дня?")
                    .setPositiveButton("Удалить", (dialog, which) -> {
                        viewModel.deleteWorkout(workout);
                        new android.os.Handler(getMainLooper()).postDelayed(() -> {
                            refreshUI();
                        }, 300);
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        adapter.setRepository(new WorkoutRepository(getApplication()));

        rvWorkouts.setLayoutManager(new LinearLayoutManager(this));
        rvWorkouts.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getWorkoutsByDay(currentUserId, selectedDay).observe(this, workouts -> {
            if (workouts != null && !workouts.isEmpty()) {
                tvEmpty.setVisibility(View.GONE);
                rvWorkouts.setVisibility(View.VISIBLE);
                adapter.submitList(new ArrayList<>(workouts));
            } else {
                tvEmpty.setVisibility(View.VISIBLE);
                rvWorkouts.setVisibility(View.GONE);
                adapter.submitList(new ArrayList<>());
            }
        });
    }

    private void loadWorkoutsForDay() {
        viewModel.getWorkoutsByDay(currentUserId, selectedDay).observe(this, workouts -> {
            if (workouts != null && !workouts.isEmpty()) {
                tvEmpty.setVisibility(View.GONE);
                rvWorkouts.setVisibility(View.VISIBLE);
                adapter.submitList(new ArrayList<>(workouts));
            } else {
                tvEmpty.setVisibility(View.VISIBLE);
                rvWorkouts.setVisibility(View.GONE);
                adapter.submitList(new ArrayList<>());
            }
        });
    }

    private void setupClickListeners() {
        ivSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        findViewById(R.id.btnAddWorkout).setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutsActivity.class);
            intent.putExtra("SELECTED_DAY", selectedDay);
            createWorkoutLauncher.launch(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    private void refreshUI() {
        setupDays();
        loadWorkoutsForDay();
    }

    private void checkAndShowNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                sendNotification();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        } else {
            sendNotification();
        }
    }

    private void sendNotification() {
        String today = getTodayName();
        List<Workout> todayWorkouts = viewModel.getWorkoutsForDaySync(currentUserId, today);
        int count = todayWorkouts != null ? todayWorkouts.size() : 0;

        if (count == 0) return;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "workout_reminder",
                    "Напоминания о тренировках",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Уведомления о плановых тренировках");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "workout_reminder")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("💪 План на сегодня")
                .setContentText("Сегодня в плане " + count + " " + getDeclension(count) + ". Успейте выполнить!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    private String getTodayName() {
        String[] days = Constants.DAYS_OF_WEEK;
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeek < 0) dayOfWeek = 6;
        if (dayOfWeek >= 7) dayOfWeek = 0;
        return days[dayOfWeek];
    }

    private String getDeclension(int number) {
        int mod = number % 100;
        if (mod > 10 && mod < 20) return "тренировок";
        if (number % 10 == 1) return "тренировка";
        if (number % 10 >= 2 && number % 10 <= 4) return "тренировки";
        return "тренировок";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendNotification();
            }
        }
    }
}