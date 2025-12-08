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
import android.widget.LinearLayout; // <--- INI YANG KURANG TADI
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
import java.io.InputStream;
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
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private String[] kategoriOptions = {"Makanan Berat", "Minuman", "Snack", "Dessert", "Lainnya"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menus);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        rvMenus = findViewById(R.id.rvMenus);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        btnAddMenu = findViewById(R.id.btnAddMenu);

        rvMenus.setLayoutManager(new LinearLayoutManager(this));

        setupImagePicker();

        int userId = sessionManager.getUserId();
        Stand myStand = dbHelper.getStandByUserId(userId);

        if (myStand == null) {
            Toast.makeText(this, "Error: Stand tidak ditemukan!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myStandId = myStand.getId();

        loadMenus();

        btnAddMenu.setOnClickListener(v -> showMenuDialog(null));
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            // --- PERUBAHAN UTAMA DI SINI ---

                            // 1. Copy file ke Internal Storage
                            String localPath = FormatHelper.copyImageToInternalStorage(this, uri);

                            if (localPath != null) {
                                // 2. Simpan Path Lokal (Bukan URI lagi)
                                selectedImageUri = Uri.parse(localPath);

                                // 3. Tampilkan
                                Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                                if (bitmap != null) {
                                    ivPreviewInDialog.setImageBitmap(bitmap);
                                    ivPreviewInDialog.setPadding(0, 0, 0, 0);
                                    ivPreviewInDialog.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    if (tvUploadHint != null) tvUploadHint.setText("Ganti Foto");
                                }
                            } else {
                                Toast.makeText(this, "Gagal menyalin gambar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private Bitmap getResizedBitmap(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            if (input != null) input.close();

            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;
            int scaleFactor = 1;
            while (imageWidth / 2 > 500 || imageHeight / 2 > 500) {
                imageWidth /= 2;
                imageHeight /= 2;
                scaleFactor *= 2;
            }

            InputStream input2 = getContentResolver().openInputStream(uri);
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            Bitmap bitmap = BitmapFactory.decodeStream(input2, null, options);
            if (input2 != null) input2.close();

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
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

        selectedImageUri = null;

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kategoriOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(catAdapter);

        ivPreviewInDialog.setOnClickListener(v -> openGallery());

        if (menuToEdit != null) {
            tvDialogTitle.setText("Edit Menu");
            etMenuNama.setText(menuToEdit.getNama());
            etMenuHarga.setText(String.valueOf(menuToEdit.getHarga()));
            etMenuDeskripsi.setText(menuToEdit.getDeskripsi());

            if (menuToEdit.getImage() != null && !menuToEdit.getImage().isEmpty()) {
                String path = menuToEdit.getImage();
                selectedImageUri = Uri.parse(path); // Simpan path di variabel global

                // LOAD DARI FILE (BUKAN CONTENT RESOLVER)
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                if (bitmap != null) {
                    ivPreviewInDialog.setImageBitmap(bitmap);
                    ivPreviewInDialog.setPadding(0,0,0,0);
                    ivPreviewInDialog.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    tvUploadHint.setText("Ganti Foto");
                }
            }

            for (int i = 0; i < kategoriOptions.length; i++) {
                if (kategoriOptions[i].equals(menuToEdit.getKategori())) {
                    spinnerKategori.setSelection(i);
                    break;
                }
            }

            if ("available".equals(menuToEdit.getStatus())) rbAvailable.setChecked(true);
            else rbUnavailable.setChecked(true);

            tvBtnSave.setText("💾 Update Menu");
        } else {
            tvBtnSave.setText("💾 Simpan Menu");
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String nama = etMenuNama.getText().toString().trim();
            String hargaStr = etMenuHarga.getText().toString().trim();
            String deskripsi = etMenuDeskripsi.getText().toString().trim();
            String kategori = spinnerKategori.getSelectedItem().toString();
            String status = rbAvailable.isChecked() ? "available" : "unavailable";
            String imagePath = (selectedImageUri != null) ? selectedImageUri.toString() : null;

            if (TextUtils.isEmpty(nama)) {
                etMenuNama.setError("Wajib diisi");
                return;
            }
            if (TextUtils.isEmpty(hargaStr)) {
                etMenuHarga.setError("Wajib diisi");
                return;
            }

            try {
                int harga = Integer.parseInt(hargaStr);

                if (menuToEdit == null) {
                    long result = dbHelper.addMenu(myStandId, nama, harga, deskripsi, kategori, status);
                    if (result > 0 && imagePath != null) {
                        dbHelper.updateMenuImage((int) result, imagePath);
                    }
                    if (result > 0) {
                        Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                        loadMenus();
                        dialog.dismiss();
                    }
                } else {
                    int result = dbHelper.updateMenu(menuToEdit.getId(), nama, harga, deskripsi, kategori, status);
                    if (imagePath != null) {
                        dbHelper.updateMenuImage(menuToEdit.getId(), imagePath);
                    }
                    if (result > 0) {
                        Toast.makeText(this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
                        loadMenus();
                        dialog.dismiss();
                    }
                }
            } catch (NumberFormatException e) {
                etMenuHarga.setError("Angka tidak valid");
            }
        });

        dialog.show();
    }

    @Override
    public void onToggleStatus(Menu menu) {
        String newStatus = menu.isAvailable() ? "unavailable" : "available";
        int result = dbHelper.updateMenu(menu.getId(), menu.getNama(), menu.getHarga(),
                menu.getDeskripsi(), menu.getKategori(), newStatus);
        if (result > 0) loadMenus();
    }

    @Override
    public void onEditMenu(Menu menu) {
        showMenuDialog(menu);
    }

    @Override
    public void onDeleteMenu(Menu menu) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Hapus menu " + menu.getNama() + "?")
                .setPositiveButton("Ya, Hapus", (d, w) -> {
                    dbHelper.deleteMenu(menu.getId());
                    loadMenus();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}