package com.example.mystats.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mystats.R;
import com.example.mystats.ui.fragments.MyWorkoutsFragment;
import com.example.mystats.ui.fragments.PopularWorkoutsFragment;

public class WorkoutsActivity extends AppCompatActivity {
    private TextView btnBack, tvTitle;
    private Button btnPopular, btnMyWorkouts;
    private boolean isPopularTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        initViews();
        setupTabs();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        btnPopular = findViewById(R.id.btnPopular);
        btnMyWorkouts = findViewById(R.id.btnMyWorkouts);
    }

    private void setupTabs() {
        loadFragment(new PopularWorkoutsFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isPopular) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();

        isPopularTab = isPopular;
        updateTabStyles();
        updateTitle();
    }

    private void updateTabStyles() {
        if (isPopularTab) {
            btnPopular.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(R.color.primary)));
            btnPopular.setTextColor(getColor(android.R.color.white));
            btnMyWorkouts.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(android.R.color.white)));
            btnMyWorkouts.setTextColor(getColor(R.color.primary));
        } else {
            btnPopular.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(android.R.color.white)));
            btnPopular.setTextColor(getColor(R.color.primary));
            btnMyWorkouts.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(R.color.primary)));
            btnMyWorkouts.setTextColor(getColor(android.R.color.white));
        }
    }

    private void updateTitle() {
        if (tvTitle != null) {
            tvTitle.setText(isPopularTab ? "Популярное" : "Мои тренировки");
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPopular.setOnClickListener(v -> {
            if (!isPopularTab) {
                loadFragment(new PopularWorkoutsFragment(), true);
            }
        });

        btnMyWorkouts.setOnClickListener(v -> {
            if (isPopularTab) {
                loadFragment(new MyWorkoutsFragment(), false);
            }
        });

        findViewById(R.id.btnCreate).setOnClickListener(v ->
                startActivity(new Intent(this, CreateWorkoutActivity.class)));
    }
}