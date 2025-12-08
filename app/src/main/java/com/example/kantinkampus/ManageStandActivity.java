package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ManageStandActivity extends AppCompatActivity {

    private EditText etStandName, etStandDesc, etOvo, etGopay;
    private CardView btnSaveStand;
    private TextView tvHeaderTitle, tvBtnSave;

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private Stand currentStand;
    private boolean isFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_stand); // Pastikan XML sudah direname

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        // Init Views
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        etStandName = findViewById(R.id.etStandName);
        etStandDesc = findViewById(R.id.etStandDesc);
        etOvo = findViewById(R.id.etOvo);
        etGopay = findViewById(R.id.etGopay);
        btnSaveStand = findViewById(R.id.btnSaveStand);
        tvBtnSave = findViewById(R.id.tvBtnSave);

        // Cek Intent apakah ini pembuatan stand pertama kali
        isFirstTime = getIntent().getBooleanExtra("is_first_time", false);

        if (isFirstTime) {
            tvHeaderTitle.setText("Buka Warung Baru");
            tvBtnSave.setText("🚀 Buka Warung Sekarang");
        } else {
            // Load data existing stand
            loadStandData();
        }

        btnSaveStand.setOnClickListener(v -> saveStand());
    }

    private void loadStandData() {
        int userId = sessionManager.getUserId();
        currentStand = dbHelper.getStandByUserId(userId);

        if (currentStand != null) {
            etStandName.setText(currentStand.getNama());
            etStandDesc.setText(currentStand.getDeskripsi());

            // Note: Stand.java perlu diupdate untuk support OVO/Gopay getter
            // Tapi sementara kita load dari DB jika ada, atau biarkan kosong jika model belum update
            // Agar tidak error, pastikan Stand.java nanti diupdate
        }
    }

    private void saveStand() {
        String name = etStandName.getText().toString().trim();
        String desc = etStandDesc.getText().toString().trim();
        String ovo = etOvo.getText().toString().trim();
        String gopay = etGopay.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etStandName.setError("Nama warung wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            etStandDesc.setError("Deskripsi wajib diisi");
            return;
        }

        int userId = sessionManager.getUserId();

        if (isFirstTime || currentStand == null) {
            // CREATE NEW STAND
            long result = dbHelper.createStand(userId, name, desc, ovo, gopay);
            if (result > 0) {
                Toast.makeText(this, "🎉 Selamat! Warung Anda berhasil dibuka.", Toast.LENGTH_LONG).show();

                // Update Session
                User user = sessionManager.getUserDetails();
                user.setStandId((int) result);
                sessionManager.createLoginSession(user);

                // Redirect ke Dashboard
                Intent intent = new Intent(ManageStandActivity.this, SellerDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "❌ Gagal membuat stand", Toast.LENGTH_SHORT).show();
            }
        } else {
            // UPDATE EXISTING STAND
            int result = dbHelper.updateStand(currentStand.getId(), name, desc, ovo, gopay);
            if (result > 0) {
                Toast.makeText(this, "✅ Informasi warung diperbarui", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke dashboard
            } else {
                Toast.makeText(this, "❌ Gagal update stand", Toast.LENGTH_SHORT).show();
            }
        }
    }
}