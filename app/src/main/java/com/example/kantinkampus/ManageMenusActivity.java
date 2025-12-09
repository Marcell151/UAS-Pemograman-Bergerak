package com.example.kantinkampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout; // Import Penting
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ManageMenusActivity extends AppCompatActivity implements MenuAdapterAdmin.OnMenuActionListener {

    private RecyclerView rvMenus;
    private LinearLayout layoutEmpty;
    private CardView btnAddMenu;

    private MenuAdapterAdmin adapter;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private int myStandId = -1;

    // Variabel Gambar
    private ImageView ivPreviewInDialog;
    private TextView tvUploadHint;
    private Uri selectedImageUri = null; // Akan menyimpan Path File Lokal
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private String[] kategoriOptions = {"Makanan Berat", "Minuman", "Snack", "Dessert", "Lainnya"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menus);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        // Init Views
        rvMenus = findViewById(R.id.rvMenus);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnAddMenu = findViewById(R.id.btnAddMenu);

        rvMenus.setLayoutManager(new LinearLayoutManager(this));

        // Setup Image Launcher
        setupImagePicker();

        // 1. Ambil Stand ID User
        int userId = sessionManager.getUserId();
        Stand myStand = dbHelper.getStandByUserId(userId);

        if (myStand == null) {
            Toast.makeText(this, "Error: Stand tidak ditemukan!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myStandId = myStand.getId();

        // 2. Load List Menu
        loadMenus();

        // 3. Listener Tombol Tambah
        btnAddMenu.setOnClickListener(v -> showMenuDialog(null));
    }

    // --- LOGIC IMAGE PICKER (SOLUSI GAMBAR ABU-ABU) ---
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri sourceUri = result.getData().getData();
                        if (sourceUri != null) {
                            // 1. SALIN File dari Galeri ke Penyimpanan Internal Aplikasi
                            // Ini kunci agar gambar tidak error/abu-abu karena masalah izin URI
                            String localPath = FormatHelper.copyImageToInternalStorage(this, sourceUri);

                            if (localPath != null) {
                                // 2. Simpan Path Lokal ke variabel (untuk database nanti)
                                selectedImageUri = Uri.parse(localPath);

                                // 3. Tampilkan Preview (Langsung baca file, tanpa ContentResolver)
                                if (ivPreviewInDialog != null) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                                    if (bitmap != null) {
                                        ivPreviewInDialog.setImageBitmap(bitmap);
                                        ivPreviewInDialog.setPadding(0, 0, 0, 0);
                                        ivPreviewInDialog.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        if (tvUploadHint != null) tvUploadHint.setText("Ganti Foto");
                                    } else {
                                        Toast.makeText(this, "Gagal menampilkan gambar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Gagal menyalin gambar dari galeri", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        // Flags untuk memastikan kita punya izin baca awal
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    private void loadMenus() {
        if (myStandId == -1) return;
        List<Menu> menus = dbHelper.getMenusByStand(myStandId);

        if (menus.isEmpty()) {
            rvMenus.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvMenus.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            adapter = new MenuAdapterAdmin(this, menus, this);
            rvMenus.setAdapter(adapter);
        }
    }

    private void showMenuDialog(Menu menuToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu, null);
        builder.setView(dialogView);

        // Init Views di Dialog
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        ivPreviewInDialog = dialogView.findViewById(R.id.ivMenuImage);
        tvUploadHint = dialogView.findViewById(R.id.tvUploadHint);

        EditText etMenuNama = dialogView.findViewById(R.id.etMenuNama);
        EditText etMenuHarga = dialogView.findViewById(R.id.etMenuHarga);
        EditText etMenuDeskripsi = dialogView.findViewById(R.id.etMenuDeskripsi);
        Spinner spinnerKategori = dialogView.findViewById(R.id.spinnerKategori);
        RadioGroup rgStatus = dialogView.findViewById(R.id.rgStatus);
        RadioButton rbAvailable = dialogView.findViewById(R.id.rbAvailable);
        RadioButton rbUnavailable = dialogView.findViewById(R.id.rbUnavailable);
        CardView btnSave = dialogView.findViewById(R.id.btnSaveMenu);
        TextView tvBtnSave = (TextView) btnSave.getChildAt(0);

        // Reset URI
        selectedImageUri = null;

        // Listener Klik Gambar
        ivPreviewInDialog.setOnClickListener(v -> openGallery());

        // Setup Spinner
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kategoriOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(catAdapter);

        // === MODE EDIT: ISI DATA ===
        if (menuToEdit != null) {
            tvDialogTitle.setText("Edit Menu");
            etMenuNama.setText(menuToEdit.getNama());
            etMenuHarga.setText(String.valueOf(menuToEdit.getHarga()));
            etMenuDeskripsi.setText(menuToEdit.getDeskripsi());

            // LOAD GAMBAR LAMA (DARI PATH LOKAL)
            if (menuToEdit.getImage() != null && !menuToEdit.getImage().isEmpty()) {
                String path = menuToEdit.getImage();
                selectedImageUri = Uri.parse(path); // Simpan path lama

                // Baca File Langsung
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    ivPreviewInDialog.setImageBitmap(bitmap);
                    ivPreviewInDialog.setPadding(0,0,0,0);
                    ivPreviewInDialog.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    tvUploadHint.setText("Ganti Foto");
                }
            }

            // Set Kategori
            for (int i = 0; i < kategoriOptions.length; i++) {
                if (kategoriOptions[i].equals(menuToEdit.getKategori())) {
                    spinnerKategori.setSelection(i);
                    break;
                }
            }

            // Set Status
            if ("available".equals(menuToEdit.getStatus())) {
                rbAvailable.setChecked(true);
            } else {
                rbUnavailable.setChecked(true);
            }

            tvBtnSave.setText("💾 Update Menu");
        } else {
            tvBtnSave.setText("💾 Simpan Menu");
        }

        AlertDialog dialog = builder.create();

        // Listener Simpan
        btnSave.setOnClickListener(v -> {
            String nama = etMenuNama.getText().toString().trim();
            String hargaStr = etMenuHarga.getText().toString().trim();
            String deskripsi = etMenuDeskripsi.getText().toString().trim();
            String kategori = spinnerKategori.getSelectedItem().toString();
            String status = rbAvailable.isChecked() ? "available" : "unavailable";

            // Konversi Path ke String untuk DB
            String imagePath = (selectedImageUri != null) ? selectedImageUri.toString() : null;

            if (TextUtils.isEmpty(nama)) {
                etMenuNama.setError("Nama wajib diisi");
                return;
            }
            if (TextUtils.isEmpty(hargaStr)) {
                etMenuHarga.setError("Harga wajib diisi");
                return;
            }

            try {
                int harga = Integer.parseInt(hargaStr);
                if (harga < 0) {
                    etMenuHarga.setError("Harga tidak boleh negatif");
                    return;
                }

                if (menuToEdit == null) {
                    // === INSERT BARU ===
                    long result = dbHelper.addMenu(myStandId, nama, harga, deskripsi, kategori, status);

                    // Update Gambar jika ada
                    if (result > 0 && imagePath != null) {
                        dbHelper.updateMenuImage((int) result, imagePath);
                    }

                    if (result > 0) {
                        Toast.makeText(this, "✅ Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        loadMenus();
                        dialog.dismiss();
                    }
                } else {
                    // === UPDATE EXISTING ===
                    int result = dbHelper.updateMenu(menuToEdit.getId(), nama, harga, deskripsi, kategori, status);

                    // Update Gambar jika ada (berubah)
                    if (imagePath != null) {
                        dbHelper.updateMenuImage(menuToEdit.getId(), imagePath);
                    }

                    if (result > 0) {
                        Toast.makeText(this, "✅ Menu berhasil diupdate", Toast.LENGTH_SHORT).show();
                        loadMenus();
                        dialog.dismiss();
                    }
                }

            } catch (NumberFormatException e) {
                etMenuHarga.setError("Harga harus angka valid");
            }
        });

        dialog.show();
    }

    // Implementasi Interface Adapter
    @Override
    public void onToggleStatus(Menu menu) {
        String newStatus = menu.isAvailable() ? "unavailable" : "available";
        int result = dbHelper.updateMenu(menu.getId(), menu.getNama(), menu.getHarga(),
                menu.getDeskripsi(), menu.getKategori(), newStatus);

        if (result > 0) {
            loadMenus();
            Toast.makeText(this, "Status diubah", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditMenu(Menu menu) {
        showMenuDialog(menu);
    }

    @Override
    public void onDeleteMenu(Menu menu) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Menu")
                .setMessage("Yakin hapus menu " + menu.getNama() + "?")
                .setPositiveButton("Ya, Hapus", (d, w) -> {
                    dbHelper.deleteMenu(menu.getId());
                    loadMenus();
                    Toast.makeText(this, "🗑️ Menu dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}