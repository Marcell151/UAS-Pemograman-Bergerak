package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SellerDashboardActivity extends AppCompatActivity {

    private TextView tvSellerName, tvStandStatus;
    private CardView btnManageOrders, btnManageMenu, btnEditStand, btnLogout;
    private CardView btnCreateStand; // Tombol khusus jika belum punya stand
    private LinearLayout layoutDashboard, layoutNoStand; // Container untuk switch tampilan

    private SessionManager sessionManager;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard); // Pastikan XML sudah direname

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        // Cek Keamanan: Pastikan yang login adalah Seller
        if (!sessionManager.isLoggedIn() || !sessionManager.isSeller()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Init Views
        tvSellerName = findViewById(R.id.tvSellerName);
        tvStandStatus = findViewById(R.id.tvStandStatus);

        // Container
        layoutDashboard = findViewById(R.id.layoutDashboard);
        layoutNoStand = findViewById(R.id.layoutNoStand);

        // Buttons Dashboard
        btnManageOrders = findViewById(R.id.btnManageOrders);
        btnManageMenu = findViewById(R.id.btnManageMenu);
        btnEditStand = findViewById(R.id.btnEditStand);
        btnLogout = findViewById(R.id.btnLogout);

        // Button No Stand
        btnCreateStand = findViewById(R.id.btnCreateStand);

        // Set Data
        User user = sessionManager.getUserDetails();
        tvSellerName.setText("Halo, " + user.getName() + " 👋");

        // CEK STATUS STAND
        checkStandStatus();

        // Listeners
        setupListeners();
    }

    private void checkStandStatus() {
        // Ambil ID User dari session
        int userId = sessionManager.getUserDetails().getId();

        // Ambil data terbaru dari database menggunakan ID (Aman tanpa password)
        User currentUser = dbHelper.getUserById(userId);

        if (currentUser != null && currentUser.getStandId() != null && currentUser.getStandId() > 0) {
            // SUDAH PUNYA STAND -> Tampilkan Dashboard
            layoutDashboard.setVisibility(View.VISIBLE);
            layoutNoStand.setVisibility(View.GONE);

            // Update session jika perlu
            if (!sessionManager.hasStand()) {
                sessionManager.createLoginSession(currentUser);
            }

            Stand stand = dbHelper.getStandByUserId(currentUser.getId());
            if (stand != null) {
                tvStandStatus.setText("Stand: " + stand.getNama());
            }
        } else {
            // BELUM PUNYA STAND -> Tampilkan Halaman Buka Stand
            layoutDashboard.setVisibility(View.GONE);
            layoutNoStand.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        // 1. Tombol Buat Stand Baru
        btnCreateStand.setOnClickListener(v -> {
            // Arahkan ke activity atau dialog buat stand
            // Untuk simplifikasi, kita pakai Dialog Add Stand yang sudah ada, tapi dimodif sedikit
            showCreateStandDialog();
        });

        // 2. Kelola Pesanan
        btnManageOrders.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, ManageOrdersActivity.class);
            startActivity(intent);
        });

        // 3. Kelola Menu
        btnManageMenu.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, ManageMenusActivity.class);
            startActivity(intent);
        });

        // 4. Edit Profil Stand
        btnEditStand.setOnClickListener(v -> {
            Intent intent = new Intent(SellerDashboardActivity.this, ManageStandActivity.class);
            startActivity(intent);
        });

        // 5. Logout
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showCreateStandDialog() {
        // Kita arahkan ke ManageStandActivity tapi mode create
        // Karena ManageStandActivity nanti kita ubah jadi form edit/create
        Intent intent = new Intent(this, ManageStandActivity.class);
        intent.putExtra("is_first_time", true);
        startActivity(intent);
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Yakin ingin menutup toko?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    sessionManager.logoutUser();
                    Intent intent = new Intent(SellerDashboardActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStandStatus(); // Cek lagi saat kembali ke halaman ini
    }
}