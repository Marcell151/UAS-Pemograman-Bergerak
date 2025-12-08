package com.example.kantinkampus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        // Delay 2 detik (2000 ms) sebelum pindah
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkLoginStatus();
        }, 2000);
    }

    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // JIKA SUDAH LOGIN -> Cek Role
            Intent intent;
            if (sessionManager.isSeller()) {
                intent = new Intent(SplashActivity.this, SellerDashboardActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
        } else {
            // JIKA BELUM LOGIN -> Ke Login Screen
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // Tutup Splash agar tidak bisa di-back
        finish();
    }
}