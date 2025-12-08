package com.example.kantinkampus;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPassword;
    private TextView tvHeaderName, tvHeaderRole;
    private CardView btnSave;

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        // Init Views
        tvHeaderName = findViewById(R.id.tvHeaderName);
        tvHeaderRole = findViewById(R.id.tvHeaderRole);
        etName = findViewById(R.id.etProfileName);
        etEmail = findViewById(R.id.etProfileEmail);
        etPhone = findViewById(R.id.etProfilePhone);
        etPassword = findViewById(R.id.etProfilePassword);
        btnSave = findViewById(R.id.btnSaveProfile);

        loadUserData();

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        int userId = sessionManager.getUserId();
        // Ambil data terbaru dari DB
        currentUser = dbHelper.getUserById(userId);

        if (currentUser != null) {
            tvHeaderName.setText(currentUser.getName());
            tvHeaderRole.setText(currentUser.getRole().toUpperCase());

            etName.setText(currentUser.getName());
            etEmail.setText(currentUser.getEmail()); // Read only
            etPhone.setText(currentUser.getPhone());
        }
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Nama wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("No HP wajib diisi");
            return;
        }

        // Update Database
        boolean success = dbHelper.updateUser(currentUser.getId(), name, phone, password);

        if (success) {
            Toast.makeText(this, "✅ Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show();

            // PENTING: Update Session agar nama di dashboard berubah
            // Kita harus mengambil ulang data lengkap user dari DB setelah update
            User updatedUser = dbHelper.getUserById(currentUser.getId());
            sessionManager.createLoginSession(updatedUser); // Timpa session lama

            finish(); // Kembali
        } else {
            Toast.makeText(this, "❌ Gagal update profil", Toast.LENGTH_SHORT).show();
        }
    }
}