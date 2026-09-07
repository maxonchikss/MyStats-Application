package com.example.mystats.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mystats.R;
import com.example.mystats.utils.SharedPreferencesManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    private TextView tvUserName, tvUserEmail, tvWelcome;
    private SwitchMaterial swDarkMode;
    private TextView btnSave, btnLogout, btnBack;
    private LinearLayout layoutAbout;
    private SharedPreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = SharedPreferencesManager.getInstance(this);

        initViews();
        loadData();
        setupClickListeners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        swDarkMode = findViewById(R.id.swDarkMode);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);
        layoutAbout = findViewById(R.id.layoutAbout);
    }

    private void loadData() {
        String userName = prefs.getUserName();
        String userEmail = prefs.getUserEmail();

        tvWelcome.setText("Привет, " + userName + "!");
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        swDarkMode.setChecked(prefs.isDarkMode());
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveSettings());

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Выход из аккаунта")
                    .setMessage("Вы уверены, что хотите выйти?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        prefs.clear();
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        });

        layoutAbout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("О приложении")
                    .setMessage("Разработчик: Никишин Максим ИКБО-20-24")
                    .setPositiveButton("Закрыть", null)
                    .show();
        });
    }

    private void saveSettings() {
        prefs.saveDarkMode(swDarkMode.isChecked());

        if (swDarkMode.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
    }
}