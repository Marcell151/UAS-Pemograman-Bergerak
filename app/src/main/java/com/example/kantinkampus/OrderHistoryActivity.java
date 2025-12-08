package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity implements OrderAdapterCustomer.OnOrderClickListener {

    private RecyclerView rvOrders;
    private LinearLayout layoutEmpty;

    private OrderAdapterCustomer adapter;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);
        userId = sessionManager.getUserId();

        rvOrders = findViewById(R.id.rvOrders);
        layoutEmpty = findViewById(R.id.layoutEmpty); // Pastikan ID ini ada di XML

        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = dbHelper.getOrdersByUser(userId);

        if (orders.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            adapter = new OrderAdapterCustomer(this, orders, this);
            rvOrders.setAdapter(adapter);
        }
    }

    @Override
    public void onOrderClick(Order order) {
        // Tampilkan Detail Status atau Opsi Batalkan
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Status Pesanan #" + order.getId());

        String statusMsg = "";
        boolean canCancel = false;

        switch (order.getStatus()) {
            case "pending_payment":
                statusMsg = "Menunggu pembayaran tunai di kasir.";
                canCancel = true;
                break;
            case "pending_verification":
                statusMsg = "Menunggu penjual memverifikasi pembayaran Anda.";
                canCancel = true;
                break;
            case "cooking":
                statusMsg = "Pesanan sedang dimasak. Mohon tunggu.";
                break;
            case "ready":
                statusMsg = "Pesanan SUDAH SIAP! Silakan ambil di stand.";
                break;
            case "completed":
                statusMsg = "Pesanan selesai. Terima kasih!";
                break;
            case "cancelled":
                statusMsg = "Pesanan ini telah dibatalkan.";
                break;
        }

        builder.setMessage(statusMsg);

        if (canCancel) {
            builder.setNegativeButton("❌ Batalkan Pesanan", (dialog, which) -> {
                confirmCancel(order.getId());
            });
        }

        // Fitur Review (Jika Completed)
        if (order.getStatus().equals("completed")) {
            builder.setPositiveButton("⭐ Beri Review", (dialog, which) -> {
                // Untuk simplifikasi, kita anggap review per menu nanti via MenuDetail
                Toast.makeText(this, "Silakan buka menu makanan untuk memberi review", Toast.LENGTH_LONG).show();
            });
        }

        builder.setNeutralButton("Tutup", null);
        builder.show();
    }

    private void confirmCancel(int orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Yakin ingin membatalkan pesanan ini?")
                .setPositiveButton("Ya", (d, w) -> {
                    dbHelper.updateOrderStatus(orderId, "cancelled");
                    Toast.makeText(this, "Pesanan dibatalkan", Toast.LENGTH_SHORT).show();
                    loadOrders();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}