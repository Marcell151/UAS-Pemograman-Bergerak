package com.example.kantinkampus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity implements OrderAdapterAdmin.OnOrderClickListener {

    private RecyclerView rvOrders;
    private LinearLayout layoutEmpty;
    // Filter Buttons
    private CardView btnFilterAll, btnFilterPending, btnFilterProcessing, btnFilterReady, btnFilterCompleted;

    private OrderAdapterAdmin adapter;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private int myStandId;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        // Ambil Stand ID Penjual
        User user = sessionManager.getUserDetails();
        Stand stand = dbHelper.getStandByUserId(user.getId());

        if (stand == null) {
            Toast.makeText(this, "Error: Stand tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        myStandId = stand.getId();

        // Init Views
        rvOrders = findViewById(R.id.rvOrders);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        // Init Filter Buttons
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterPending = findViewById(R.id.btnFilterPending);
        btnFilterProcessing = findViewById(R.id.btnFilterProcessing);
        btnFilterReady = findViewById(R.id.btnFilterReady);
        btnFilterCompleted = findViewById(R.id.btnFilterCompleted);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        // Set Listeners untuk Filter
        setupFilterListeners();

        // Load Data Awal
        loadOrders();
    }

    private void setupFilterListeners() {
        btnFilterAll.setOnClickListener(v -> setFilter("all"));
        btnFilterPending.setOnClickListener(v -> setFilter("pending_payment")); // Gabungan pending_payment & verification
        btnFilterProcessing.setOnClickListener(v -> setFilter("cooking"));
        btnFilterReady.setOnClickListener(v -> setFilter("ready"));
        btnFilterCompleted.setOnClickListener(v -> setFilter("completed"));
    }

    private void setFilter(String status) {
        currentFilter = status;
        loadOrders();
        // Visual feedback untuk tombol aktif bisa ditambahkan di sini (opsional)
        Toast.makeText(this, "Filter: " + status, Toast.LENGTH_SHORT).show();
    }

    private void loadOrders() {
        // Ambil pesanan KHUSUS stand ini
        List<Order> orders = dbHelper.getOrdersByStand(myStandId, currentFilter);

        // Filter manual untuk tab "Pending" agar mencakup 'pending_payment' DAN 'pending_verification'
        if (currentFilter.equals("pending_payment")) {
            List<Order> pendingOrders = new ArrayList<>();
            List<Order> allOrders = dbHelper.getOrdersByStand(myStandId, "all");
            for (Order o : allOrders) {
                if (o.getStatus().equals("pending_payment") || o.getStatus().equals("pending_verification")) {
                    pendingOrders.add(o);
                }
            }
            orders = pendingOrders;
        }

        if (orders.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            // Pasang Adapter
            adapter = new OrderAdapterAdmin(this, orders, this);
            rvOrders.setAdapter(adapter);
        }
    }

    @Override
    public void onOrderClick(Order order) {
        showOrderActionDialog(order);
    }

    private void showOrderActionDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kelola Pesanan #" + order.getId());

        String[] options;

        // Logika Status Pesanan
        if (order.getStatus().equals("pending_payment")) {
            // Jika Cash -> Terima Pembayaran
            builder.setMessage("Metode: CASH\nTotal: Rp " + order.getTotal());
            builder.setPositiveButton("✅ Terima Uang & Masak", (dialog, which) -> {
                updateStatus(order.getId(), "cooking");
            });
            builder.setNegativeButton("❌ Batalkan", (dialog, which) -> {
                updateStatus(order.getId(), "cancelled");
            });

        } else if (order.getStatus().equals("pending_verification")) {
            // Jika Transfer -> Cek Bukti
            builder.setMessage("Metode: " + order.getPaymentMethod().toUpperCase() + "\nCek mutasi rekening Anda.");
            builder.setPositiveButton("✅ Uang Masuk (Masak)", (dialog, which) -> {
                updateStatus(order.getId(), "cooking");
            });
            builder.setNegativeButton("❌ Tidak Ada Uang", (dialog, which) -> {
                updateStatus(order.getId(), "cancelled");
            });
            builder.setNeutralButton("📷 Lihat Bukti", (dialog, which) -> {
                // Tampilkan bukti (Simplifikasi: Toast atau Intent View Image)
                if (order.getPaymentProofPath() != null) {
                    Toast.makeText(this, "Membuka bukti bayar...", Toast.LENGTH_SHORT).show();
                    // Kode untuk membuka gambar proof (jika ada file aslinya)
                } else {
                    Toast.makeText(this, "Tidak ada bukti terlampir", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (order.getStatus().equals("cooking")) {
            builder.setMessage("Pesanan sedang dimasak.");
            builder.setPositiveButton("🔔 Makanan Siap (Notif User)", (dialog, which) -> {
                updateStatus(order.getId(), "ready");
            });

        } else if (order.getStatus().equals("ready")) {
            builder.setMessage("Makanan sudah siap diambil.");
            builder.setPositiveButton("🤝 Sudah Diambil (Selesai)", (dialog, which) -> {
                updateStatus(order.getId(), "completed");
            });
        } else {
            builder.setMessage("Status: " + order.getStatus());
            builder.setPositiveButton("Tutup", null);
        }

        builder.show();
    }

    private void updateStatus(int orderId, String newStatus) {
        int result = dbHelper.updateOrderStatus(orderId, newStatus);
        if (result > 0) {
            Toast.makeText(this, "Status diperbarui: " + newStatus, Toast.LENGTH_SHORT).show();
            loadOrders(); // Refresh list
        } else {
            Toast.makeText(this, "Gagal update status", Toast.LENGTH_SHORT).show();
        }
    }
}