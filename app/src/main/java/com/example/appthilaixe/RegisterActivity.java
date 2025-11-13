package com.example.appthilaixe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    // ------------------------------
    // Khai báo các view
    // ------------------------------
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private CheckBox cbTerms;
    private TextView tvTerms, tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        
        // Initialize views
        initViews();
        
        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        cbTerms = findViewById(R.id.cb_terms);
        tvTerms = findViewById(R.id.tv_terms);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setClickListeners() {
        btnRegister.setOnClickListener(v -> handleRegister());
        
        tvTerms.setOnClickListener(v -> {
            // TODO: Navigate to terms and conditions screen
            Toast.makeText(RegisterActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
        
        tvLogin.setOnClickListener(v -> {
            // Navigate back to login screen
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ và tên");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản và điều kiện", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement actual registration logic with backend
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

        // Sau khi đăng ký, quay về màn hình Login
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // ------------------------------
    // Kiểm tra email hợp lệ
    // ------------------------------
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

