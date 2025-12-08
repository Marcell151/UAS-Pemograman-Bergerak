package com.example.kantinkampus;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements MenuAdapter.OnMenuClickListener {

    private RecyclerView rvFavorites;
    private LinearLayout layoutEmpty;
    private CardView btnBack;

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private MenuAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        rvFavorites = findViewById(R.id.rvFavorites);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnBack = findViewById(R.id.btnBack);

        // Gunakan Grid 2 Kolom agar rapi
        rvFavorites.setLayoutManager(new GridLayoutManager(this, 2));

        btnBack.setOnClickListener(v -> finish());

        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data saat kembali (siapa tahu ada yang di-unlove dari detail)
        loadFavorites();
    }

    private void loadFavorites() {
        // Ambil data menu yang dilike user ini
        List<Menu> favoriteMenus = dbHelper.getFavoriteMenus(userId);

        if (favoriteMenus.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            // Reuse MenuAdapter yang sudah ada
            adapter = new MenuAdapter(this, favoriteMenus, this);
            rvFavorites.setAdapter(adapter);
        }
    }

    // Implementasi saat tombol "+ Tambah" diklik di halaman favorit
    @Override
    public void onAddToCart(Menu menu) {
        if (menu.isAvailable()) {
            dbHelper.addToCart(userId, menu.getId(), 1, null);
            Toast.makeText(this, "✅ Masuk keranjang", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Menu habis", Toast.LENGTH_SHORT).show();
        }
    }
}