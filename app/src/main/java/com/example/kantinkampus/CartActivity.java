package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
    private TextView tvTotal, btnCheckout;
    private LinearLayout tvEmptyCart, layoutCheckout;

    private CartAdapter cartAdapter;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private int userId;
    private List<CartItem> currentCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);
        userId = sessionManager.getUserId();

        // Init Views
        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotal);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        layoutCheckout = findViewById(R.id.layoutCheckout); // Pastikan ID ini ada di XML (LinearLayout pembungkus tombol checkout)
        btnCheckout = findViewById(R.id.btnCheckout);

        rvCart.setLayoutManager(new LinearLayoutManager(this));

        // Checkout Button Listener
        btnCheckout.setOnClickListener(v -> showPaymentDialog());

        loadCart();
    }

    private void loadCart() {
        currentCartItems = dbHelper.getCartItems(userId);

        if (currentCartItems.isEmpty()) {
            rvCart.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
            layoutCheckout.setVisibility(View.GONE); // Sembunyikan tombol checkout jika kosong
        } else {
            rvCart.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
            layoutCheckout.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(this, currentCartItems, this);
            rvCart.setAdapter(cartAdapter);

            calculateTotal();
        }
    }

    private void calculateTotal() {
        int total = 0;
        for (CartItem item : currentCartItems) {
            total += (item.getMenu().getHarga() * item.getQty());
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tvTotal.setText(formatter.format(total));
    }

    private void showPaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_payment_method, null);
        builder.setView(dialogView);

        RadioGroup rgPayment = dialogView.findViewById(R.id.rgPaymentMethod);

        AlertDialog dialog = builder.create();

        // Tambahkan tombol konfirmasi di dialog
        // Kita bisa tambahkan button programmatically karena di XML dialog mungkin belum ada
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Bayar & Pesan", (d, w) -> {
            int selectedId = rgPayment.getCheckedRadioButtonId();
            String paymentMethod = "cash"; // Default

            if (selectedId == R.id.rbOvo) paymentMethod = "ovo";
            else if (selectedId == R.id.rbGopay) paymentMethod = "gopay";
            else if (selectedId == R.id.rbBankTransfer) paymentMethod = "transfer";

            processCheckout(paymentMethod);
        });

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal", (d, w) -> dialog.dismiss());
        dialog.show();
    }

    private void processCheckout(String paymentMethod) {
        // Logic: Jika metode bukan cash, harusnya ada upload bukti.
        // Untuk simplifikasi tugas ini, kita anggap user sudah transfer dan lanjut.

        // Panggil method DBHelper yang sudah canggih (otomatis pecah order per stand)
        dbHelper.createOrderFromCart(userId, currentCartItems, paymentMethod, null);

        String msg = paymentMethod.equals("cash") ?
                "Pesanan dibuat! Silakan bayar di kasir." :
                "Pesanan dibuat! Menunggu verifikasi penjual.";

        Toast.makeText(this, "✅ " + msg, Toast.LENGTH_LONG).show();

        // Redirect ke History
        Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Agar tidak back ke cart
        startActivity(intent);
        finish();
    }

    @Override
    public void onQuantityChanged() {
        loadCart(); // Refresh cart jika qty berubah/dihapus
    }
}