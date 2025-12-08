package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
    private int standId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);

        // Initialize
        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        tvStandName = findViewById(R.id.tvStandName);
        tvStandDesc = findViewById(R.id.tvStandDesc);
        rvMenu = findViewById(R.id.rvMenu);
        btnViewCart = findViewById(R.id.btnViewCart);

        // Get data from intent
        standId = getIntent().getIntExtra("stand_id", 0);
        String standNama = getIntent().getStringExtra("stand_nama");
        String standDeskripsi = getIntent().getStringExtra("stand_deskripsi");

        if (standId == 0) {
            Toast.makeText(this, "❌ Error: Stand tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvStandName.setText(standNama != null ? standNama : "Stand");
        tvStandDesc.setText(standDeskripsi != null ? standDeskripsi : "");

        rvMenu.setLayoutManager(new GridLayoutManager(this, 2));

        loadMenu();

        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuListActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload menu when returning (in case favorites changed)
        loadMenu();
    }

    private void loadMenu() {
        try {
            List<Menu> menus = dbHelper.getMenuByStandId(standId, userId);

            if (menus == null || menus.isEmpty()) {
                Toast.makeText(this, "ℹ️ Belum ada menu di stand ini",
                        Toast.LENGTH_SHORT).show();
            }

            menuAdapter = new MenuAdapter(this, menus, this);
            rvMenu.setAdapter(menuAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "❌ Error loading menu: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddToCart(Menu menu) {
        try {
            if (menu == null) {
                Toast.makeText(this, "❌ Error: Menu tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            if (menu.isAvailable()) {
                dbHelper.addToCart(userId, menu.getId(), 1, null);
                Toast.makeText(this, "✅ " + menu.getNama() + " ditambahkan ke keranjang!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ " + menu.getNama() + " sedang tidak tersedia",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}