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
    private TextView tvMenuName, tvMenuKategori, tvMenuPrice, tvMenuDescription, tvMenuStatus;
    private TextView tvFavoriteIcon, tvAverageRating, tvRatingStars, tvTotalReviews, tvEmptyReviews;
    private TextView btnAddReview;
    private CardView btnBack, btnFavorite, btnAddToCart, cardStatus;
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

        // Get menu ID from intent
        menuId = getIntent().getIntExtra("menu_id", -1);
        if (menuId == -1) {
            Toast.makeText(this, "Error: Menu tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Load data
        loadMenuDetail();
        loadReviews();

        // Setup listeners
        setupListeners();
    }

    private void initViews() {
        tvMenuName = findViewById(R.id.tvMenuName);
        tvMenuKategori = findViewById(R.id.tvMenuKategori);
        tvMenuPrice = findViewById(R.id.tvMenuPrice);
        tvMenuDescription = findViewById(R.id.tvMenuDescription);
        tvMenuStatus = findViewById(R.id.tvMenuStatus);
        tvFavoriteIcon = findViewById(R.id.tvFavoriteIcon);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        tvRatingStars = findViewById(R.id.tvRatingStars);
        tvTotalReviews = findViewById(R.id.tvTotalReviews);
        tvEmptyReviews = findViewById(R.id.tvEmptyReviews);
        btnAddReview = findViewById(R.id.btnAddReview);

        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        cardStatus = findViewById(R.id.cardStatus);

        rvReviews = findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMenuDetail() {
        menu = dbHelper.getMenuById(menuId);
        if (menu == null) {
            Toast.makeText(this, "Menu tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set menu info
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        tvMenuName.setText(menu.getNama());
        tvMenuKategori.setText("🏷️ " + (menu.getKategori() != null ? menu.getKategori() : "Umum"));
        tvMenuPrice.setText(formatter.format(menu.getHarga()));
        tvMenuDescription.setText(menu.getDeskripsi() != null && !menu.getDeskripsi().isEmpty()
                ? menu.getDeskripsi()
                : "Tidak ada deskripsi untuk menu ini.");

        // Set status
        if (menu.isAvailable()) {
            tvMenuStatus.setText("✅ Tersedia");
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.success));
        } else {
            tvMenuStatus.setText("❌ Habis");
            cardStatus.setCardBackgroundColor(getResources().getColor(R.color.danger));
        }

        // Check if favorite
        isFavorite = dbHelper.isFavorite(userId, menuId);
        updateFavoriteIcon();
    }

    private void loadReviews() {
        List<Review> reviews = dbHelper.getMenuReviews(menuId);

        if (reviews.isEmpty()) {
            rvReviews.setVisibility(View.GONE);
            tvEmptyReviews.setVisibility(View.VISIBLE);
            tvAverageRating.setText("0.0");
            tvRatingStars.setText("☆☆☆☆☆");
            tvTotalReviews.setText("0 review");
        } else {
            rvReviews.setVisibility(View.VISIBLE);
            tvEmptyReviews.setVisibility(View.GONE);

            // Calculate average rating
            float totalRating = 0;
            for (Review review : reviews) {
                totalRating += review.getRating();
            }
            float averageRating = totalRating / reviews.size();

            // Update UI
            tvAverageRating.setText(String.format(Locale.getDefault(), "%.1f", averageRating));
            tvRatingStars.setText(getStarsFromRating(averageRating));
            tvTotalReviews.setText(reviews.size() + " review" + (reviews.size() > 1 ? "s" : ""));

            // Set adapter
            reviewAdapter = new ReviewAdapter(this, reviews);
            rvReviews.setAdapter(reviewAdapter);
        }
    }

    private String getStarsFromRating(float rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        boolean hasHalfStar = (rating - fullStars) >= 0.5;

        for (int i = 0; i < 5; i++) {
            if (i < fullStars) {
                stars.append("⭐");
            } else if (i == fullStars && hasHalfStar) {
                stars.append("⭐");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> toggleFavorite());

        btnAddToCart.setOnClickListener(v -> {
            if (menu.isAvailable()) {
                dbHelper.addToCart(userId, menuId, 1, null);
                Toast.makeText(this, "✅ " + menu.getNama() + " ditambahkan ke keranjang!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Menu tidak tersedia", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddReview.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDetailActivity.this, AddReviewActivity.class);
            intent.putExtra("menu_id", menuId);
            intent.putExtra("menu_name", menu.getNama());
            startActivity(intent);
        });
    }

    private void toggleFavorite() {
        if (isFavorite) {
            // Remove from favorites
            int result = dbHelper.removeFromFavorites(userId, menuId);
            if (result > 0) {
                isFavorite = false;
                updateFavoriteIcon();
                Toast.makeText(this, "💔 Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add to favorites
            long result = dbHelper.addToFavorites(userId, menuId);
            if (result > 0) {
                isFavorite = true;
                updateFavoriteIcon();
                Toast.makeText(this, "❤️ Ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateFavoriteIcon() {
        tvFavoriteIcon.setText(isFavorite ? "❤️" : "🤍");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReviews(); // Reload reviews when returning from AddReview
    }
}