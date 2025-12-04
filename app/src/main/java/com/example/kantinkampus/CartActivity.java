package com.example.kantinkampus;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemListener {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private DBHelper dbHelper;
    private TextView tvTotal, btnCheckout;
    private LinearLayout tvEmptyCart, layoutCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DBHelper(this);

        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotal);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        layoutCheckout = findViewById(R.id.layoutCheckout);

        rvCart.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });
    }

    private void loadCart() {
        List<CartItem> cartItems = dbHelper.getCartItems();

        if (cartItems.isEmpty()) {
            rvCart.setVisibility(View.GONE);
            layoutCheckout.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
        } else {
            rvCart.setVisibility(View.VISIBLE);
            layoutCheckout.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);

            cartAdapter = new CartAdapter(this, cartItems, this);
            rvCart.setAdapter(cartAdapter);

            updateTotal(cartItems);
        }
    }

    private void updateTotal(List<CartItem> cartItems) {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tvTotal.setText(formatter.format(total));
    }

    @Override
    public void onQuantityChanged() {
        loadCart();
    }

    private void checkout() {
        List<CartItem> cartItems = dbHelper.getCartItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Keranjang kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total
        int total = 0;
        StringBuilder items = new StringBuilder();
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
            items.append(item.getMenu().getNama())
                    .append(" (")
                    .append(item.getQty())
                    .append("x)\n");
        }

        // Show confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✅ Konfirmasi Pesanan");
        builder.setMessage("Total: " + NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(total) + "\n\nLanjutkan pesanan?");

        final int finalTotal = total;
        final String finalItems = items.toString();

        builder.setPositiveButton("Ya, Pesan! 🍽️", (dialog, which) -> {
            // Save to history
            dbHelper.addToHistory(finalItems, finalTotal);

            // Clear cart
            dbHelper.clearCart();

            // Show success message
            Toast.makeText(CartActivity.this, "✅ Pesanan berhasil! Menunggu diproses... 🍽️", Toast.LENGTH_LONG).show();

            // Reload cart
            loadCart();

            // Close activity after delay
            new android.os.Handler().postDelayed(() -> finish(), 1500);
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }
}