package com.example.mystats.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mystats.R;
import com.example.mystats.utils.SharedPreferencesManager;
import com.example.mystats.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new LoginViewModel(getApplication());

        initViews();
        setupObservers();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegisterLink);
    }

    private void setupObservers() {
        viewModel.getLoginSuccess().observe(this, success -> {
            if (success != null && success) {
                viewModel.getUserLiveData().observe(this, user -> {
                    if (user != null) {
                        SharedPreferencesManager.getInstance(this).saveUserId(user.getId());
                        SharedPreferencesManager.getInstance(this).saveUserName(user.getName());
                        SharedPreferencesManager.getInstance(this).saveUserEmail(user.getEmail());
                        SharedPreferencesManager.getInstance(this).setLoggedIn(true);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                });
            } else {
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.login(email, password);
        });

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}