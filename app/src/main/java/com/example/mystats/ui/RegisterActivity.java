package com.example.mystats.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mystats.R;
import com.example.mystats.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private LoginViewModel viewModel;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private CheckBox cbPolicy;
    private TextView tvPolicyText;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new LoginViewModel(getApplication());
        initViews();
        setupPolicyLink();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbPolicy = findViewById(R.id.cbPolicy);
        tvPolicyText = findViewById(R.id.tvPolicyText);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupPolicyLink() {
        String fullText = "Я согласен с условием использования и политикой конфиденциальности";
        String linkText = "условием использования и политикой конфиденциальности";
        int start = fullText.indexOf(linkText);
        int end = start + linkText.length();

        SpannableString spannable = new SpannableString(fullText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showPolicyDialog();
            }
            @Override
            public void updateDrawState(android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#2196F3"));
                ds.setUnderlineText(false);
            }
        };
        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPolicyText.setText(spannable);
        tvPolicyText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showPolicyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Политика конфиденциальности")
                .setMessage("Мы собираем данные для улучшения работы приложения. Ваши данные защищены и не передаются третьим лицам.")
                .setPositiveButton("Понятно", null)
                .show();
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.email_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 8) {
                Toast.makeText(this, "Пароль минимум 8 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, R.string.passwords_mismatch, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cbPolicy.isChecked()) {
                Toast.makeText(this, R.string.policy_required, Toast.LENGTH_SHORT).show();
                return;
            }

            long result = viewModel.registerUser(name, email, password);
            if (result > 0) {
                Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.register_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}