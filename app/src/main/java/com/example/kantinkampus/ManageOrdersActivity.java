package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity implements OrderAdapterAdmin.OnOrderClickListener {
    private RecyclerView rvOrders;
    private LinearLayout layoutEmpty;
    private CardView btnFilterAll, btnFilterPending, btnFilterProcessing, btnFilterReady, btnFilterCompleted;

    private OrderAdapterAdmin adapter;
    private DBHelper dbHelper;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        dbHelper = new DBHelper(this);

        // Initialize views
        rvOrders = findViewById(R.id.rvOrders);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterPending = findViewById(R.id.btnFilterPending);
        btnFilterProcessing = findViewById(R.id.btnFilterProcessing);
        btnFilterReady = findViewById(R.id.btnFilterReady);
        btnFilterCompleted = findViewById(R.id.btnFilterCompleted);

        // Setup RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        // Setup filter buttons
        setupFilterButtons();

        // Load orders
        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void setupFilterButtons() {
        btnFilterAll.setOnClickListener(v -> filterOrders("all", btnFilterAll));
        btnFilterPending.setOnClickListener(v -> filterOrders("pending", btnFilterPending));
        btnFilterProcessing.setOnClickListener(v -> filterOrders("processing", btnFilterProcessing));
        btnFilterReady.setOnClickListener(v -> filterOrders("ready", btnFilterReady));
        btnFilterCompleted.setOnClickListener(v -> filterOrders("completed", btnFilterCompleted));
    }

    private void filterOrders(String filter, CardView selectedButton) {
        currentFilter = filter;

        // Reset all buttons
        resetFilterButtons();

        // Highlight selected button
        selectedButton.setCardBackgroundColor(getResources().getColor(R.color.white));

        // Load filtered orders
        loadOrders();
    }

    private void resetFilterButtons() {
        int defaultColor = getResources().getColor(R.color.light_gray);
        btnFilterAll.setCardBackgroundColor(defaultColor);
        btnFilterPending.setCardBackgroundColor(defaultColor);
        btnFilterProcessing.setCardBackgroundColor(defaultColor);
        btnFilterReady.setCardBackgroundColor(defaultColor);
        btnFilterCompleted.setCardBackgroundColor(defaultColor);
    }

    private void loadOrders() {
        List<Order> orders;

        if ("all".equals(currentFilter)) {
            orders = dbHelper.getAllOrders();
        } else {
            orders = dbHelper.getOrdersByStatus(currentFilter);
        }

        if (orders.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new OrderAdapterAdmin(this, orders, this);
                rvOrders.setAdapter(adapter);
            } else {
                adapter.updateOrders(orders);
            }
        }
    }

    @Override
    public void onOrderClick(Order order) {
        showOrderDetailDialog(order);
    }

    private void showOrderDetailDialog(Order order) {
        // Get order items
        List<OrderItem> items = dbHelper.getOrderItems(order.getId());

        StringBuilder itemsText = new StringBuilder();
        for (OrderItem item : items) {
            itemsText.append("• ")
                    .append(item.getMenuName())
                    .append(" (")
                    .append(item.getQty())
                    .append("x)\n");
        }

        String message = "Customer: " + order.getUserName() + "\n" +
                "Stand: " + order.getStandName() + "\n" +
                "Waktu: " + order.getCreatedAt() + "\n\n" +
                "Items:\n" + itemsText.toString() + "\n" +
                "Payment: " + order.getPaymentMethod() + "\n" +
                "Status: " + order.getStatusText();

        if (order.getNotes() != null && !order.getNotes().isEmpty()) {
            message += "\n\nCatatan: " + order.getNotes();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📦 Detail Pesanan #" + order.getId());
        builder.setMessage(message);

        // Show status update options based on current status
        if (order.isPending()) {
            builder.setPositiveButton("✅ Terima & Proses", (dialog, which) -> {
                updateOrderStatus(order.getId(), "processing");
            });
            builder.setNegativeButton("❌ Tolak", (dialog, which) -> {
                updateOrderStatus(order.getId(), "cancelled");
            });
        } else if (order.isProcessing()) {
            builder.setPositiveButton("✅ Siap Diambil", (dialog, which) -> {
                updateOrderStatus(order.getId(), "ready");
            });
            builder.setNegativeButton("Tutup", null);
        } else if (order.isReady()) {
            builder.setPositiveButton("🎉 Selesai", (dialog, which) -> {
                updateOrderStatus(order.getId(), "completed");
            });
            builder.setNegativeButton("Tutup", null);
        } else {
            builder.setPositiveButton("Tutup", null);
        }

        builder.show();
    }

    private void updateOrderStatus(int orderId, String newStatus) {
        int result = dbHelper.updateOrderStatus(orderId, newStatus);

        if (result > 0) {
            String statusText = "";
            switch (newStatus) {
                case "processing":
                    statusText = "Pesanan sedang diproses";
                    break;
                case "ready":
                    statusText = "Pesanan siap diambil! Customer akan mendapat notifikasi.";
                    break;
                case "completed":
                    statusText = "Pesanan selesai";
                    break;
                case "cancelled":
                    statusText = "Pesanan dibatalkan";
                    break;
            }

            Toast.makeText(this, "✅ " + statusText, Toast.LENGTH_SHORT).show();

            // Reload orders
            loadOrders();
        } else {
            Toast.makeText(this, "❌ Gagal update status", Toast.LENGTH_SHORT).show();
        }
    }
}