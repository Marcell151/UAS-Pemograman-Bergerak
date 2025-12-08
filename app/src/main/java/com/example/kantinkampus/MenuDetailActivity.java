package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MenuDetailActivity extends AppCompatActivity {
    private TextView tvMenuName, tvMenuPrice, tvMenuKategori, tvMenuDescription, tvMenuStatus;
    private TextView tvFavoriteIcon, tvAverageRating, tvRatingStars, tvTotalReviews;
    private TextView tvEmptyReviews, btnAddReview;
    private CardView btnFavorite, btnAddToCart, cardStatus, btnBack;
    private RecyclerView rvReviews;

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private ReviewAdapter reviewAdapter;

    private int menuId;
    private int userId;
    private Menu menu;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);
        userId = sessionManager.getUserId();

        // Get Intent Data
        menuId = getIntent().getIntExtra("menu_id", -1);
        if (menuId == -1) {
            finish();
            return;
        }

        initViews();
        loadMenuData();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload saat kembali (misal setelah memberi review atau login)
        loadReviews();
        updateFavoriteStatus();
    }

    private void initViews() {
        tvMenuName = findViewById(R.id.tvMenuName);
        tvMenuPrice = findViewById(R.id.tvMenuPrice);
        tvMenuKategori = findViewById(R.id.tvMenuKategori);
        tvMenuDescription = findViewById(R.id.tvMenuDescription);
        tvMenuStatus = findViewById(R.id.tvMenuStatus);

        // Rating Views
        tvAverageRating = findViewById(R.id.tvAverageRating);
        tvRatingStars = findViewById(R.id.tvRatingStars);
        tvTotalReviews = findViewById(R.id.tvTotalReviews);
        tvEmptyReviews = findViewById(R.id.tvEmptyReviews);
        rvReviews = findViewById(R.id.rvReviews);

        // Buttons
        btnFavorite = findViewById(R.id.btnFavorite);
        tvFavoriteIcon = findViewById(R.id.tvFavoriteIcon);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        cardStatus = findViewById(R.id.cardStatus);
        btnBack = findViewById(R.id.btnBack);

        // Tombol Add Review (Opsional, biasanya lewat Order History)
        btnAddReview = findViewById(R.id.btnAddReview);
        if (btnAddReview != null) btnAddReview.setVisibility(View.GONE); // Sembunyikan default

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setNestedScrollingEnabled(false);
    }

    private void loadMenuData() {
        // Ambil detail menu
        menu = dbHelper.getMenuById(menuId);
        if (menu == null) return;

        // Set Text Info
        tvMenuName.setText(menu.getNama());
        tvMenuKategori.setText(menu.getKategori());
        tvMenuDescription.setText(menu.getDeskripsi());

        tvMenuPrice.setText(FormatHelper.formatRupiah(menu.getHarga()));

        // Status Badge
        if (menu.isAvailable()) {
            tvMenuStatus.setText("Tersedia");
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.success));
        } else {
            tvMenuStatus.setText("Habis");
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.danger));
        }

        // Cek Favorit
        updateFavoriteStatus();

        // Load Reviews
        loadReviews();
    }

    private void updateFavoriteStatus() {
        isFavorite = dbHelper.isFavorite(userId, menuId);
        tvFavoriteIcon.setText(isFavorite ? "❤️" : "🤍");
    }

    private void loadReviews() {
        // 1. Ambil List Review dari Database
        List<Review> reviews = dbHelper.getMenuReviews(menuId);

        // 2. Hitung Rata-rata Rating
        if (reviews != null && !reviews.isEmpty()) {
            float totalRating = 0;
            for (Review r : reviews) {
                totalRating += r.getRating();
            }
            float average = totalRating / reviews.size();

            // Update UI Header Rating
            tvAverageRating.setText(String.format(Locale.US, "%.1f", average));
            tvTotalReviews.setText("(" + reviews.size() + " Ulasan)");

            // Generate Bintang (misal 4.5 -> ⭐⭐⭐⭐)
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < Math.round(average); i++) {
                stars.append("⭐");
            }
            tvRatingStars.setText(stars.toString());

            // Tampilkan List Komentar
            rvReviews.setVisibility(View.VISIBLE);
            tvEmptyReviews.setVisibility(View.GONE);

            reviewAdapter = new ReviewAdapter(this, reviews);
            rvReviews.setAdapter(reviewAdapter);

        } else {
            // Jika Belum Ada Review
            tvAverageRating.setText("0.0");
            tvRatingStars.setText("☆☆☆☆☆");
            tvTotalReviews.setText("(0 Ulasan)");

            rvReviews.setVisibility(View.GONE);
            tvEmptyReviews.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            if (isFavorite) {
                dbHelper.removeFromFavorites(userId, menuId);
                Toast.makeText(this, "💔 Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addToFavorites(userId, menuId);
                Toast.makeText(this, "💖 Masuk daftar favorit!", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteStatus(); // Refresh icon
        });

        btnAddToCart.setOnClickListener(v -> {
            if (sessionManager.isSeller()) {
                Toast.makeText(this, "Penjual tidak bisa membeli menu sendiri 😅", Toast.LENGTH_SHORT).show();
                return;
            }

            if (menu.isAvailable()) {
                dbHelper.addToCart(userId, menuId, 1, null);
                Toast.makeText(this, "🛒 Masuk keranjang!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Yah, stoknya habis.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}