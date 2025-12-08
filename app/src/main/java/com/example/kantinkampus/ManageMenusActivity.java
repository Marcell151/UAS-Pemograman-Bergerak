package com.example.kantinkampus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
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

    // Kategori options
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

        // 1. Ambil Stand ID milik user yang login
        int userId = sessionManager.getUserId();
        Stand myStand = dbHelper.getStandByUserId(userId);

        if (myStand == null) {
            Toast.makeText(this, "Error: Stand tidak ditemukan!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myStandId = myStand.getId();

        // 2. Load menu stand tersebut
        loadMenus();

        // 3. Setup Tombol Tambah
        btnAddMenu.setOnClickListener(v -> showAddMenuDialog());
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

            // Gunakan MenuAdapterAdmin (pastikan file ini ada)
            adapter = new MenuAdapterAdmin(this, menus, this);
            rvMenus.setAdapter(adapter);
        }
    }

    private void showAddMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_menu, null);
        builder.setView(dialogView);

        EditText etMenuNama = dialogView.findViewById(R.id.etMenuNama);
        EditText etMenuHarga = dialogView.findViewById(R.id.etMenuHarga);
        EditText etMenuDeskripsi = dialogView.findViewById(R.id.etMenuDeskripsi);
        Spinner spinnerKategori = dialogView.findViewById(R.id.spinnerKategori);
        RadioGroup rgStatus = dialogView.findViewById(R.id.rgStatus);

        // Setup Spinner Kategori
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kategoriOptions);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(catAdapter);

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

    @Override
    public void onToggleStatus(Menu menu) {
        String newStatus = menu.isAvailable() ? "unavailable" : "available";
        int result = dbHelper.updateMenu(menu.getId(), menu.getNama(), menu.getHarga(),
                menu.getDeskripsi(), menu.getKategori(), newStatus);
        if (result > 0) {
            loadMenus(); // Refresh tampilan
        }
    }

    @Override
    public void onEditMenu(Menu menu) {
        // TODO: Buat dialog edit mirip add menu, tapi isi datanya dulu
        Toast.makeText(this, "Edit menu: " + menu.getNama(), Toast.LENGTH_SHORT).show();
        // Anda bisa copy paste logika showAddMenuDialog dan set text fieldnya
    }

    @Override
    public void onDeleteMenu(Menu menu) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Menu")
                .setMessage("Hapus menu " + menu.getNama() + "?")
                .setPositiveButton("Ya, Hapus", (d, w) -> {
                    dbHelper.deleteMenu(menu.getId());
                    loadMenus();
                    Toast.makeText(this, "Menu dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}