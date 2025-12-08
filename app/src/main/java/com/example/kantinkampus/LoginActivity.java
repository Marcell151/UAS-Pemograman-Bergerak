package com.example.kantinkampus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText etEmail, etPassword;
    private View btnLogin; // Ubah ke View (atau TextView) agar aman untuk XML Anda
    private TextView tvRegister;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SessionManager and DBHelper
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

        // PERBAIKAN 1: Menggunakan View agar cocok baik itu CardView maupun TextView
        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);

        // Login Button Click
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show();
            } else {
                performLogin(email, password);
            }
        });

        // Register Link Click
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin(String email, String password) {
        try {
            // Cek login via database
            User user = dbHelper.loginUser(email, password);

            if (user != null) {
                // Login sukses, simpan sesi
                sessionManager.createLoginSession(user);
                Toast.makeText(this, "✅ Login Berhasil! Selamat datang, " + user.getName(), Toast.LENGTH_SHORT).show();
                redirectToHome();
            } else {
                Toast.makeText(this, "❌ Email atau password salah!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error login: " + e.getMessage());
            Toast.makeText(this, "Terjadi kesalahan sistem", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToHome() {
        Intent intent;
        // LOGIKA BARU: Cek Seller atau Buyer
        if (sessionManager.isSeller()) {
            // Jika Penjual -> Masuk ke Dashboard Penjual
            intent = new Intent(LoginActivity.this, SellerDashboardActivity.class);
        } else {
            // Jika Pembeli -> Masuk ke Halaman Utama Customer
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }

        // Clear back stack agar tidak bisa back ke login
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // PERBAIKAN 2: Tambahkan SuppressLint agar tidak merah
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Disable back button (Minimize app)
        moveTaskToBack(true);
    }
}