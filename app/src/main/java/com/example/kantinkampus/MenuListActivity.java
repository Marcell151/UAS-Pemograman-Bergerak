package com.example.kantinkampus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MenuListActivity extends AppCompatActivity implements MenuAdapter.OnMenuClickListener {
    private RecyclerView rvMenu;
    private MenuAdapter menuAdapter;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private TextView tvStandName, tvStandDesc;
    private RelativeLayout btnViewCart;
    private EditText etSearchMenu;
    private LinearLayout layoutCategories;

    private int standId;
    private int userId;
    private String currentCategory = "Semua"; // Default filter

    // Daftar Kategori (Sesuai dengan yang ada di DB/ManageMenu)
    private String[] categories = {"Semua", "Makanan Berat", "Minuman", "Snack", "Dessert", "Lainnya"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        tvStandName = findViewById(R.id.tvStandName);
        tvStandDesc = findViewById(R.id.tvStandDesc);
        rvMenu = findViewById(R.id.rvMenu);
        btnViewCart = findViewById(R.id.btnViewCart);
        etSearchMenu = findViewById(R.id.etSearchMenu);
        layoutCategories = findViewById(R.id.layoutCategories);

        standId = getIntent().getIntExtra("stand_id", -1);
        String name = getIntent().getStringExtra("stand_nama");
        String desc = getIntent().getStringExtra("stand_deskripsi");

        if (standId == -1) {
            finish();
            return;
        }

        tvStandName.setText(name);
        tvStandDesc.setText(desc);

        rvMenu.setLayoutManager(new GridLayoutManager(this, 2));

        // Setup Fitur
        setupCategoryChips();
        setupSearch();
        setupCartButton();

        // Load awal (Semua Menu)
        loadMenu("Semua", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu(currentCategory, etSearchMenu.getText().toString());
    }

    // --- 1. SETUP TOMBOL KATEGORI (CHIPS) ---
    private void setupCategoryChips() {
        for (String cat : categories) {
            CardView card = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 16, 0); // Margin kanan
            card.setLayoutParams(params);
            card.setRadius(50); // Bulat
            card.setCardElevation(0);

            // Set warna awal (Semua = Selected)
            if (cat.equals("Semua")) {
                card.setCardBackgroundColor(getResources().getColor(R.color.primary));
            } else {
                card.setCardBackgroundColor(Color.parseColor("#F0F0F0")); // Abu muda
            }

            TextView tv = new TextView(this);
            tv.setText(cat);
            tv.setPadding(32, 16, 32, 16);
            tv.setTextSize(14);
            tv.setGravity(Gravity.CENTER);

            if (cat.equals("Semua")) tv.setTextColor(Color.WHITE);
            else tv.setTextColor(Color.DKGRAY);

            card.addView(tv);

            // Listener Klik
            card.setOnClickListener(v -> {
                updateCategorySelection(cat);
                currentCategory = cat;
                etSearchMenu.setText(""); // Reset search saat ganti kategori
                loadMenu(cat, "");
            });

            layoutCategories.addView(card);
        }
    }

    private void updateCategorySelection(String selectedCat) {
        for (int i = 0; i < layoutCategories.getChildCount(); i++) {
            CardView card = (CardView) layoutCategories.getChildAt(i);
            TextView tv = (TextView) card.getChildAt(0);
            String catName = tv.getText().toString();

            if (catName.equals(selectedCat)) {
                card.setCardBackgroundColor(getResources().getColor(R.color.primary));
                tv.setTextColor(Color.WHITE);
            } else {
                card.setCardBackgroundColor(Color.parseColor("#F0F0F0"));
                tv.setTextColor(Color.DKGRAY);
            }
        }
    }

    // --- 2. SETUP PENCARIAN ---
    private void setupSearch() {
        etSearchMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Saat mengetik, abaikan kategori (cari di semua)
                // Atau bisa juga cari dalam kategori (tergantung preferensi).
                // Di sini kita cari global di stand ini.
                updateCategorySelection("Semua"); // Reset visual kategori
                currentCategory = "Semua";
                loadMenu("Search", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // --- 3. LOAD DATA (FILTER LOGIC) ---
    private void loadMenu(String category, String keyword) {
        List<Menu> menus;

        if (!keyword.isEmpty()) {
            // Mode SEARCH
            menus = dbHelper.searchMenus(standId, keyword, userId);
        } else if (!category.equals("Semua") && !category.equals("Search")) {
            // Mode FILTER KATEGORI
            menus = dbHelper.getMenusByCategory(standId, category, userId);
        } else {
            // Mode ALL
            menus = dbHelper.getMenuByStandId(standId, userId);
        }

        if (menus == null || menus.isEmpty()) {
            // Bisa tambahkan tampilan "Menu tidak ditemukan" jika mau
        }

        menuAdapter = new MenuAdapter(this, menus, this);
        rvMenu.setAdapter(menuAdapter);
    }

    private void setupCartButton() {
        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(MenuListActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onAddToCart(Menu menu) {
        if (menu.isAvailable()) {
            dbHelper.addToCart(userId, menu.getId(), 1, null);
            Toast.makeText(this, "✅ " + menu.getNama() + " masuk keranjang", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Menu habis", Toast.LENGTH_SHORT).show();
        }
    }
}