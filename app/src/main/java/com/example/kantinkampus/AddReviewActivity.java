package com.example.kantinkampus;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AddReviewActivity extends AppCompatActivity {
    private TextView tvMenuName, tvRatingText;
    private TextView star1, star2, star3, star4, star5;
    private EditText etComment;
    private CardView btnSubmitReview;
    private TextView btnCancel;

    private DBHelper dbHelper;
    private SessionManager sessionManager;

    private int menuId;
    private int orderId;
    private String menuName;
    private int selectedRating = 0;
    private TextView[] stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review); // Pastikan layout ini ada

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        // Get data from intent
        menuId = getIntent().getIntExtra("menu_id", -1);
        orderId = getIntent().getIntExtra("order_id", -1); // Tambahan: ID Order
        menuName = getIntent().getStringExtra("menu_name");

        if (menuId == -1) {
            finish();
            return;
        }

        // Initialize views
        tvMenuName = findViewById(R.id.tvMenuName);
        tvRatingText = findViewById(R.id.tvRatingText);
        etComment = findViewById(R.id.etComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        btnCancel = findViewById(R.id.btnCancel);

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);

        stars = new TextView[]{star1, star2, star3, star4, star5};

        tvMenuName.setText(menuName);

        setupStarListeners();

        btnSubmitReview.setOnClickListener(v -> submitReview());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupStarListeners() {
        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnClickListener(v -> {
                selectedRating = rating;
                updateStarUI(selectedRating);
                updateRatingText(selectedRating);
            });
        }
    }

    private void updateStarUI(int rating) {
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setText("⭐");
                stars[i].setAlpha(1.0f);
            } else {
                stars[i].setText("☆");
                stars[i].setAlpha(0.5f);
            }
        }
    }

    private void updateRatingText(int rating) {
        String[] ratingTexts = {
                "Tap bintang untuk memberi rating",
                "😞 Sangat Buruk",
                "😕 Kurang",
                "😐 Cukup",
                "😊 Bagus",
                "🤩 Sangat Bagus!"
        };
        tvRatingText.setText(ratingTexts[rating]);
    }

    private void submitReview() {
        if (selectedRating == 0) {
            Toast.makeText(this, "Pilih rating terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = etComment.getText().toString().trim();
        int userId = sessionManager.getUserId();

        // Simpan ke Database
        long result = dbHelper.addReview(userId, menuId, orderId, selectedRating, comment);

        if (result > 0) {
            Toast.makeText(this, "✅ Review berhasil dikirim!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "❌ Gagal mengirim review", Toast.LENGTH_SHORT).show();
        }
    }
}