package com.example.kantinkampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvStatusEmoji, tvOrderStatus, tvOrderId;
    private TextView tvCustomerInfo, tvStandInfo, tvDateInfo;
    private TextView tvTotalAmount, tvPaymentMethod;
    private RecyclerView rvOrderItems;

    private CardView cardPaymentProof, btnUploadProof;
    private ImageView ivPaymentProof;
    private TextView tvBtnUpload;

    private CardView btnMainAction, btnSecondaryAction;
    private TextView tvBtnMainAction, tvBtnSecondaryAction;
    private LinearLayout layoutActions, layoutStatusHeader;

    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private int orderId;
    private Order currentOrder;
    private boolean isSeller;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        isSeller = sessionManager.isSeller();

        orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId == -1) {
            finish();
            return;
        }

        initViews();
        setupImagePicker();
        loadOrderData();
    }

    private void initViews() {
        tvStatusEmoji = findViewById(R.id.tvStatusEmoji);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderId = findViewById(R.id.tvOrderId);

        tvCustomerInfo = findViewById(R.id.tvCustomerInfo);
        tvStandInfo = findViewById(R.id.tvStandInfo);
        tvDateInfo = findViewById(R.id.tvDateInfo);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);

        rvOrderItems = findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));

        layoutStatusHeader = findViewById(R.id.layoutStatusHeader);

        cardPaymentProof = findViewById(R.id.cardPaymentProof);
        ivPaymentProof = findViewById(R.id.ivPaymentProof);
        btnUploadProof = findViewById(R.id.btnUploadProof);
        tvBtnUpload = findViewById(R.id.tvBtnUpload);

        layoutActions = findViewById(R.id.layoutActions);
        btnMainAction = findViewById(R.id.btnMainAction);
        btnSecondaryAction = findViewById(R.id.btnSecondaryAction);
        tvBtnMainAction = findViewById(R.id.tvBtnMainAction);
        tvBtnSecondaryAction = findViewById(R.id.tvBtnSecondaryAction);

        btnUploadProof.setOnClickListener(v -> openGallery());
    }

    // --- PERBAIKAN 1: LOGIC UPLOAD (Copy ke Internal Storage) ---
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            // SALIN FILE DARI GALERI KE APLIKASI
                            String localPath = FormatHelper.copyImageToInternalStorage(this, uri);

                            if (localPath != null) {
                                // Simpan PATH LOKAL ke Database
                                dbHelper.updateOrderProof(orderId, localPath);
                                Toast.makeText(this, "✅ Bukti terupload!", Toast.LENGTH_SHORT).show();
                                loadOrderData();
                            } else {
                                Toast.makeText(this, "Gagal menyalin gambar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        // Permission flags tetap disertakan untuk jaga-jaga
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    // --- PERBAIKAN 2: LOGIC TAMPIL (Baca File Lokal) ---
    private void displayProofImage(String path) {
        if (path == null || path.isEmpty()) return;

        // Langsung baca file path (tanpa ContentResolver / URI)
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        if (bitmap != null) {
            ivPaymentProof.setImageBitmap(bitmap);
            ivPaymentProof.setPadding(0,0,0,0);
            ivPaymentProof.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            // Fallback jika file rusak/terhapus
            ivPaymentProof.setImageResource(android.R.drawable.ic_menu_report_image);
            ivPaymentProof.setPadding(40,40,40,40);
        }
    }

    private void loadOrderData() {
        currentOrder = dbHelper.getOrderById(orderId);
        List<OrderItem> items = dbHelper.getOrderItems(orderId);

        if (currentOrder == null) return;

        tvOrderId.setText("Order #" + currentOrder.getId());
        // Gunakan Format Waktu Cantik
        tvDateInfo.setText("📅 " + FormatHelper.formatWaktu(currentOrder.getCreatedAt()));

        tvCustomerInfo.setText(isSeller ? "👤 Pembeli: " + currentOrder.getUserName() : "👤 Anda (" + sessionManager.getUserDetails().getName() + ")");
        tvStandInfo.setText(isSeller ? "🏠 Stand Anda" : "🏠 Warung: " + currentOrder.getStandName());

        // Gunakan Format Rupiah
        tvTotalAmount.setText(FormatHelper.formatRupiah(currentOrder.getTotal()));

        String method = currentOrder.getPaymentMethod().toUpperCase();
        tvPaymentMethod.setText("Metode: " + method);

        boolean isCompleted = "completed".equals(currentOrder.getStatus());
        boolean isBuyer = !isSeller;

        OrderDetailProductAdapter adapter = new OrderDetailProductAdapter(this, items, isCompleted, isBuyer);
        rvOrderItems.setAdapter(adapter);

        updateStatusUI(currentOrder.getStatus());
        setupProofSection(currentOrder);
        setupActionButtons();
    }

    private void setupProofSection(Order order) {
        boolean isCash = "cash".equals(order.getPaymentMethod());

        if (isCash) {
            cardPaymentProof.setVisibility(View.GONE);
            return;
        }

        cardPaymentProof.setVisibility(View.VISIBLE);
        String proofPath = order.getPaymentProofPath();

        if (proofPath != null && !proofPath.isEmpty()) {
            displayProofImage(proofPath); // Panggil Helper Tampil
            btnUploadProof.setVisibility(isSeller ? View.GONE : View.VISIBLE);
            tvBtnUpload.setText("Ganti Bukti Transfer");
        } else {
            ivPaymentProof.setImageResource(android.R.drawable.ic_menu_camera);
            ivPaymentProof.setPadding(40,40,40,40);

            if (isSeller) {
                btnUploadProof.setVisibility(View.GONE);
            } else {
                btnUploadProof.setVisibility(View.VISIBLE);
                tvBtnUpload.setText("📤 Upload Bukti Transfer Sekarang");
            }
        }
    }

    private void updateStatusUI(String status) {
        String emoji = "📦";
        String text = status;
        int color = R.color.gray;

        switch (status) {
            case "pending_payment":
                emoji = "💵"; text = "Menunggu Pembayaran"; color = R.color.warning; break;
            case "pending_verification":
                emoji = "⏳"; text = "Verifikasi Pembayaran"; color = R.color.info; break;
            case "cooking":
                emoji = "👨‍🍳"; text = "Sedang Dimasak"; color = R.color.primary; break;
            case "ready":
                emoji = "🥡"; text = "Siap Diambil"; color = R.color.success; break;
            case "completed":
                emoji = "✅"; text = "Selesai"; color = R.color.secondary; break;
            case "cancelled":
                emoji = "❌"; text = "Dibatalkan"; color = R.color.danger; break;
            case "paid":
                emoji = "💰"; text = "Sudah Dibayar"; color = R.color.success; break;
        }

        tvStatusEmoji.setText(emoji);
        tvOrderStatus.setText(text);
        layoutStatusHeader.setBackgroundColor(getResources().getColor(color));
    }

    private void setupActionButtons() {
        String status = currentOrder.getStatus();
        btnMainAction.setVisibility(View.GONE);
        btnSecondaryAction.setVisibility(View.GONE);

        boolean hasProof = currentOrder.getPaymentProofPath() != null;

        if (isSeller) {
            if (status.equals("pending_payment")) {
                showMainBtn("✅ Terima Uang & Masak", v -> updateStatus("cooking"));
                showSecBtn("❌ Tolak Pesanan", v -> updateStatus("cancelled"));
            }
            else if (status.equals("pending_verification")) {
                if (hasProof) {
                    showMainBtn("✅ Bukti Valid (Masak)", v -> updateStatus("cooking"));
                    showSecBtn("❌ Bukti Invalid (Tolak)", v -> updateStatus("cancelled"));
                } else {
                    btnSecondaryAction.setVisibility(View.VISIBLE);
                    tvBtnSecondaryAction.setText("Batalkan (Belum bayar)");
                    btnSecondaryAction.setOnClickListener(v -> updateStatus("cancelled"));
                }
            }
            else if (status.equals("cooking")) {
                showMainBtn("🔔 Makanan Siap", v -> updateStatus("ready"));
            }
            else if (status.equals("ready")) {
                showMainBtn("🤝 Selesai (Diambil)", v -> updateStatus("completed"));
            }
        } else {
            if (status.equals("pending_payment") || status.equals("pending_verification")) {
                showSecBtn("Batalkan Pesanan", v -> updateStatus("cancelled"));
            }
        }
    }

    private void showMainBtn(String text, View.OnClickListener listener) {
        btnMainAction.setVisibility(View.VISIBLE);
        tvBtnMainAction.setText(text);
        btnMainAction.setOnClickListener(listener);
    }

    private void showSecBtn(String text, View.OnClickListener listener) {
        btnSecondaryAction.setVisibility(View.VISIBLE);
        tvBtnSecondaryAction.setText(text);
        btnSecondaryAction.setOnClickListener(listener);
    }

    private void updateStatus(String newStatus) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Update status pesanan?")
                .setPositiveButton("Ya", (d, w) -> {
                    dbHelper.updateOrderStatus(orderId, newStatus);
                    Toast.makeText(this, "Status diperbarui", Toast.LENGTH_SHORT).show();
                    loadOrderData();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}