package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private TextView btnLogin, tvRegister;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize
        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        // Check if user already logged in
        if (sessionManager.isLoggedIn()) {
            redirectToHome();
            return;
        }

        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Set listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        // Reset errors
        etEmail.setError(null);
        etPassword.setError(null);

        // Get values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            focusView = etPassword;
            cancel = true;
        } else if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            focusView = etPassword;
            cancel = true;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email tidak boleh kosong");
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError("Format email tidak valid");
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            performLogin(email, password);
        }
    }

    private void performLogin(String email, String password) {
        User user = dbHelper.loginUser(email, password);

        if (user != null) {
            // Save session
            sessionManager.createLoginSession(user);

            // Show success message
            Toast.makeText(this, "✅ Login berhasil! Selamat datang, " + user.getName(),
                    Toast.LENGTH_SHORT).show();

            // Redirect based on role
            redirectToHome();
            finish();
        } else {
            Toast.makeText(this, "❌ Email atau password salah!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToHome() {
        Intent intent;
        if (sessionManager.isAdmin()) {
            // Redirect to Admin Dashboard
            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        } else {
            // Redirect to Customer Home
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    @Override
    public void onBackPressed() {
        // Disable back button on login screen
        moveTaskToBack(true);
    }
}