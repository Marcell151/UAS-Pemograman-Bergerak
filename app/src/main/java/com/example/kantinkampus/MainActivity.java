package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeMessage, tvUserName, tvCartBadge;
    private CardView cardCart, btnPilihStand, btnKeranjang, btnRiwayat, btnTentang, btnFavorit, btnLogout; // Tambah btnFavorit
    private SessionManager sessionManager;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        // Cek Login (Backup)
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Redirect jika Seller
        if (sessionManager.isSeller()) {
            Intent intent = new Intent(this, SellerDashboardActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        initViews();
        setupUser();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // Update badge saat kembali ke halaman ini
    }

    private void initViews() {
        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        tvUserName = findViewById(R.id.tvUserName);
        tvCartBadge = findViewById(R.id.tvCartBadge);

        cardCart = findViewById(R.id.cardCart);
        btnPilihStand = findViewById(R.id.btnPilihStand);
        btnKeranjang = findViewById(R.id.btnKeranjang);
        btnRiwayat = findViewById(R.id.btnRiwayat);
        btnTentang = findViewById(R.id.btnTentang);
        btnFavorit = findViewById(R.id.btnFavorit); // Init Tombol Favorit
        btnLogout = findViewById(R.id.btnLogoutCustomer);
    }

    private void setupUser() {
        User user = sessionManager.getUserDetails();
        tvUserName.setText(user.getName());

        // Set greeting berdasarkan tipe user
        if ("dosen".equals(user.getType())) {
            tvWelcomeMessage.setText("Selamat Datang, Bapak/Ibu Dosen! 🎓");
        } else {
            tvWelcomeMessage.setText("Halo, Mahasiswa! 👋");
        }
    }

    private void updateCartBadge() {
        int count = dbHelper.getCartCount(sessionManager.getUserId());
        if (count > 0) {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        } else {
            tvCartBadge.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        // 1. Pilih Stand
        btnPilihStand.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StandListActivity.class);
            startActivity(intent);
        });

        // 2. Keranjang
        View.OnClickListener cartListener = v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        };
        btnKeranjang.setOnClickListener(cartListener);
        cardCart.setOnClickListener(cartListener);

        // 3. Riwayat
        btnRiwayat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // 4. FAVORIT (BARU)
        btnFavorit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // 5. Tentang
        btnTentang.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // 6. Edit Profil (Klik Nama)
        tvUserName.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // 7. Logout
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Yakin ingin keluar?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        sessionManager.logoutUser();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }
}