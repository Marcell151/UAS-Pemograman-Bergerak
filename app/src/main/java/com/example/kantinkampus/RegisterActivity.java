package com.example.kantinkampus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RegisterActivity extends AppCompatActivity {

    // UI Components
    private RadioGroup rgRole, rgBuyerType;
    private RadioButton rbBuyer, rbSeller, rbMahasiswa, rbDosen;
    private EditText etName, etEmail, etPhone, etPassword;
    private EditText etNimNip, etBusinessLicense;
    private LinearLayout layoutBuyerFields, layoutSellerFields;
    private CardView btnRegister;
    private TextView tvLogin;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        // Initialize Views
        rgRole = findViewById(R.id.rgRole);
        rgBuyerType = findViewById(R.id.rgBuyerType);
        rbBuyer = findViewById(R.id.rbBuyer);
        rbSeller = findViewById(R.id.rbSeller);
        rbMahasiswa = findViewById(R.id.rbMahasiswa);
        rbDosen = findViewById(R.id.rbDosen);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        // Conditional Fields
        etNimNip = findViewById(R.id.etNimNip);
        etBusinessLicense = findViewById(R.id.etBusinessLicense);

        // Layout Containers
        layoutBuyerFields = findViewById(R.id.layoutBuyerFields);
        layoutSellerFields = findViewById(R.id.layoutSellerFields);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Listener untuk Pilihan Role (Penjual vs Pembeli)
        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSeller) {
                // Jika pilih Penjual: Sembunyikan field pembeli, Tampilkan field penjual
                layoutBuyerFields.setVisibility(View.GONE);
                layoutSellerFields.setVisibility(View.VISIBLE);
            } else {
                // Jika pilih Pembeli: Sebaliknya
                layoutBuyerFields.setVisibility(View.VISIBLE);
                layoutSellerFields.setVisibility(View.GONE);
            }
        });

        // Listener Tombol Register
        btnRegister.setOnClickListener(v -> validateAndRegister());

        // Listener Link Login
        tvLogin.setOnClickListener(v -> finish());
    }

    private void validateAndRegister() {
        // Reset Errors
        etName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etNimNip.setError(null);
        etBusinessLicense.setError(null);

        // Get Values
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean isSeller = rbSeller.isChecked();
        String role = isSeller ? "seller" : "buyer";

        // Validation Flags
        boolean cancel = false;
        View focusView = null;

        // 1. Validasi Kolom Wajib (Umum)
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password wajib diisi");
            focusView = etPassword;
            cancel = true;
        } else if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            focusView = etPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email wajib diisi");
            focusView = etEmail;
            cancel = true;
        } else if (!email.contains("@")) {
            etEmail.setError("Email tidak valid");
            focusView = etEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            etName.setError("Nama wajib diisi");
            focusView = etName;
            cancel = true;
        }

        // 2. Validasi Khusus Role
        String nimNip = null;
        String buyerType = null;
        String businessLicense = null;

        if (isSeller) {
            // Validasi Penjual
            businessLicense = etBusinessLicense.getText().toString().trim();
            if (TextUtils.isEmpty(businessLicense)) {
                etBusinessLicense.setError("Nomor Kartu Usaha wajib diisi");
                focusView = etBusinessLicense;
                cancel = true;
            }
        } else {
            // Validasi Pembeli
            nimNip = etNimNip.getText().toString().trim();
            buyerType = rbMahasiswa.isChecked() ? "mahasiswa" : "dosen";

            if (TextUtils.isEmpty(nimNip)) {
                etNimNip.setError("NIM/NIP wajib diisi");
                focusView = etNimNip;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Eksekusi Registrasi ke Database
            performRegister(email, password, name, role, phone, nimNip, buyerType, businessLicense);
        }
    }

    private void performRegister(String email, String password, String name,
                                 String role, String phone, String nimNip, String type, String license) {

        // Panggil method registerUser yang baru di DBHelper
        long result = dbHelper.registerUser(email, password, name, role, phone, nimNip, type, license);

        if (result > 0) {
            String roleText = role.equals("seller") ? "Penjual" : "Pembeli";
            Toast.makeText(this, "✅ Registrasi Berhasil sebagai " + roleText + "!\nSilakan Login.", Toast.LENGTH_LONG).show();
            finish(); // Kembali ke Login
        } else {
            Toast.makeText(this, "❌ Gagal Mendaftar! Email mungkin sudah terdaftar.", Toast.LENGTH_LONG).show();
        }
    }
}