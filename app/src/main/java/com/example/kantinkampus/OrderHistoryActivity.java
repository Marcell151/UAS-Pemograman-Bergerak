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
        // BUKA HALAMAN DETAIL
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order_id", order.getId());
        startActivity(intent);
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