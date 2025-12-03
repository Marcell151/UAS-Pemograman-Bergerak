package com.example.kantinkampus;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private SessionManager sessionManager;
    private TextView tvTotal, btnCheckout;
    private LinearLayout tvEmptyCart, layoutCheckout;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize
        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);
        userId = sessionManager.getUserId();

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
                showPaymentMethodDialog();
            }
        });
    }

    private void loadCart() {
        List<CartItem> cartItems = dbHelper.getCartItems(userId);

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

    private void showPaymentMethodDialog() {
        List<CartItem> cartItems = dbHelper.getCartItems(userId);
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Keranjang kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get stand ID from first item (all items should be from same stand)
        int standId = cartItems.get(0).getMenu().getStandId();

        // Create payment method dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment_method, null);
        RadioGroup rgPaymentMethod = dialogView.findViewById(R.id.rgPaymentMethod);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("💳 Pilih Metode Pembayaran");
        builder.setView(dialogView);

        builder.setPositiveButton("Lanjutkan", (dialog, which) -> {
            int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadio = dialogView.findViewById(selectedId);
            String paymentMethod = selectedRadio.getText().toString();

            checkout(standId, paymentMethod);
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    private void checkout(int standId, String paymentMethod) {
        List<CartItem> cartItems = dbHelper.getCartItems(userId);

        // Calculate total
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        // Show confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✅ Konfirmasi Pesanan");

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String message = "Total: " + formatter.format(total) + "\n" +
                "Pembayaran: " + paymentMethod + "\n\n" +
                "Lanjutkan pesanan?";

        builder.setMessage(message);

        builder.setPositiveButton("Ya, Pesan! 🍽️", (dialog, which) -> {
            // Create order
            long orderId = dbHelper.createOrderFromCart(userId, standId, paymentMethod, null);

            if (orderId > 0) {
                // Show success message
                Toast.makeText(CartActivity.this,
                        "✅ Pesanan berhasil!\n" +
                                "Pesanan #" + orderId + " sedang diproses... 🍽️",
                        Toast.LENGTH_LONG).show();

                // Reload cart
                loadCart();

                // Close activity after delay
                new android.os.Handler().postDelayed(() -> finish(), 1500);
            } else {
                Toast.makeText(CartActivity.this,
                        "❌ Gagal membuat pesanan. Coba lagi.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }
}