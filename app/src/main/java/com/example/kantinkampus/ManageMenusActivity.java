package com.example.kantinkampus;

<<<<<<< HEAD
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
=======
>>>>>>> origin/master
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.LinearLayout; // <--- INI YANG KURANG TADI
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
=======
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
>>>>>>> origin/master
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
<<<<<<< HEAD
import java.io.InputStream;
=======
>>>>>>> origin/master
import java.util.List;

public class ManageMenusActivity extends AppCompatActivity implements MenuAdapterAdmin.OnMenuActionListener {

    private RecyclerView rvMenus;
    private LinearLayout layoutEmpty;
    private CardView btnAddMenu;

    private MenuAdapterAdmin adapter;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private int myStandId = -1;

<<<<<<< HEAD
    // Variabel Gambar
    private ImageView ivPreviewInDialog;
    private TextView tvUploadHint;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

=======
    // Kategori options
>>>>>>> origin/master
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

<<<<<<< HEAD
        setupImagePicker();

=======
        // 1. Ambil Stand ID milik user yang login
>>>>>>> origin/master
        int userId = sessionManager.getUserId();
        Stand myStand = dbHelper.getStandByUserId(userId);

        if (myStand == null) {
            Toast.makeText(this, "Error: Stand tidak ditemukan!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myStandId = myStand.getId();

<<<<<<< HEAD
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
=======
        // 2. Load menu stand tersebut
        loadMenus();

        // 3. Setup Tombol Tambah
        btnAddMenu.setOnClickListener(v -> showAddMenuDialog());
>>>>>>> origin/master
    }

    private void loadMenus() {
        if (myStandId == -1) return;
<<<<<<< HEAD
=======

>>>>>>> origin/master
        List<Menu> menus = dbHelper.getMenusByStand(myStandId);

        if (menus.isEmpty()) {
            rvMenus.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            rvMenus.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
<<<<<<< HEAD
=======

            // Gunakan MenuAdapterAdmin (pastikan file ini ada)
>>>>>>> origin/master
            adapter = new MenuAdapterAdmin(this, menus, this);
            rvMenus.setAdapter(adapter);
        }
    }

<<<<<<< HEAD
    private void showMenuDialog(Menu menuToEdit) {
=======
    private void showAddMenuDialog() {
>>>>>>> origin/master
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu, null);
        builder.setView(dialogView);

<<<<<<< HEAD
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        ivPreviewInDialog = dialogView.findViewById(R.id.ivMenuImage);
        tvUploadHint = dialogView.findViewById(R.id.tvUploadHint);

=======
>>>>>>> origin/master
        EditText etMenuNama = dialogView.findViewById(R.id.etMenuNama);
        EditText etMenuHarga = dialogView.findViewById(R.id.etMenuHarga);
        EditText etMenuDeskripsi = dialogView.findViewById(R.id.etMenuDeskripsi);
        Spinner spinnerKategori = dialogView.findViewById(R.id.spinnerKategori);
        RadioGroup rgStatus = dialogView.findViewById(R.id.rgStatus);
<<<<<<< HEAD
        RadioButton rbAvailable = dialogView.findViewById(R.id.rbAvailable);
        RadioButton rbUnavailable = dialogView.findViewById(R.id.rbUnavailable);
        CardView btnSave = dialogView.findViewById(R.id.btnSaveMenu);
        TextView tvBtnSave = (TextView) btnSave.getChildAt(0);

        selectedImageUri = null;

=======

        // Setup Spinner Kategori
>>>>>>> origin/master
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kategoriOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(catAdapter);

<<<<<<< HEAD
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

=======
        AlertDialog dialog = builder.create();

        // Tombol Simpan (Cari ID dari dialog layout Anda, biasanya ada tombol save)
        // Jika di XML dialog_add_menu belum ada tombol save, kita tambahkan PositiveButton
        // Tapi cek dulu XML Anda. Jika pakai tombol custom di dalam XML:
        CardView btnSave = dialogView.findViewById(R.id.btnSaveMenu); // Asumsi ID tombol di dialog
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                saveNewMenu(dialog, etMenuNama, etMenuHarga, etMenuDeskripsi, spinnerKategori, rgStatus);
            });
        } else {
            // Fallback jika pakai standard dialog buttons
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Simpan", (d, w) -> {
                // Override onclick later to prevent dismiss on error
            });
        }

        dialog.show();

        // Handle Save Logic inside Dialog (agar bisa validasi)
        if (btnSave == null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                saveNewMenu(dialog, etMenuNama, etMenuHarga, etMenuDeskripsi, spinnerKategori, rgStatus);
            });
        }
    }

    private void saveNewMenu(AlertDialog dialog, EditText etNama, EditText etHarga,
                             EditText etDesc, Spinner spKat, RadioGroup rgStatus) {

        String nama = etNama.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String deskripsi = etDesc.getText().toString().trim();
        String kategori = spKat.getSelectedItem().toString();

        // Status
        int selectedStatusId = rgStatus.getCheckedRadioButtonId();
        String status = "available";
        // Asumsi ID radio button di XML Anda
        // if (selectedStatusId == R.id.rbUnavailable) status = "unavailable";
        // Cek ID resource yang benar nanti, tapi default available aman.

        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama wajib diisi");
            return;
        }
        if (TextUtils.isEmpty(hargaStr)) {
            etHarga.setError("Harga wajib diisi");
            return;
        }

        try {
            int harga = Integer.parseInt(hargaStr);

            // SIMPAN KE DATABASE
            long result = dbHelper.addMenu(myStandId, nama, harga, deskripsi, kategori, status);

            if (result > 0) {
                Toast.makeText(this, "✅ Menu berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                loadMenus(); // Refresh list
                dialog.dismiss();
            } else {
                Toast.makeText(this, "❌ Gagal menambah menu", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            etHarga.setError("Harga harus angka valid");
        }
    }

    // --- Implementasi Interface Adapter ---

>>>>>>> origin/master
    @Override
    public void onToggleStatus(Menu menu) {
        String newStatus = menu.isAvailable() ? "unavailable" : "available";
        int result = dbHelper.updateMenu(menu.getId(), menu.getNama(), menu.getHarga(),
                menu.getDeskripsi(), menu.getKategori(), newStatus);
<<<<<<< HEAD
        if (result > 0) loadMenus();
=======
        if (result > 0) {
            loadMenus(); // Refresh tampilan
        }
>>>>>>> origin/master
    }

    @Override
    public void onEditMenu(Menu menu) {
<<<<<<< HEAD
        showMenuDialog(menu);
=======
        // TODO: Buat dialog edit mirip add menu, tapi isi datanya dulu
        Toast.makeText(this, "Edit menu: " + menu.getNama(), Toast.LENGTH_SHORT).show();
        // Anda bisa copy paste logika showAddMenuDialog dan set text fieldnya
>>>>>>> origin/master
    }

    @Override
    public void onDeleteMenu(Menu menu) {
        new AlertDialog.Builder(this)
<<<<<<< HEAD
                .setTitle("Hapus")
=======
                .setTitle("Hapus Menu")
>>>>>>> origin/master
                .setMessage("Hapus menu " + menu.getNama() + "?")
                .setPositiveButton("Ya, Hapus", (d, w) -> {
                    dbHelper.deleteMenu(menu.getId());
                    loadMenus();
<<<<<<< HEAD
=======
                    Toast.makeText(this, "Menu dihapus", Toast.LENGTH_SHORT).show();
>>>>>>> origin/master
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}