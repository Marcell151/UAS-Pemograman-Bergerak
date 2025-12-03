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

public class RegisterActivity extends AppCompatActivity {
    private RadioGroup rgRole, rgCustomerType;
    private RadioButton rbCustomer, rbAdmin, rbMahasiswa, rbDosen;
    private EditText etName, etEmail, etPassword, etPhone, etNimNip, etAdminCode;
    private TextView btnRegister, tvLogin, tvNimNipLabel;
    private LinearLayout layoutCustomerType, layoutNimNip, layoutAdminCode;

    private DBHelper dbHelper;
    private static final String ADMIN_CODE = "ADMIN2024"; // Kode rahasia untuk admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        // Initialize views
        rgRole = findViewById(R.id.rgRole);
        rgCustomerType = findViewById(R.id.rgCustomerType);
        rbCustomer = findViewById(R.id.rbCustomer);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbMahasiswa = findViewById(R.id.rbMahasiswa);
        rbDosen = findViewById(R.id.rbDosen);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etNimNip = findViewById(R.id.etNimNip);
        etAdminCode = findViewById(R.id.etAdminCode);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        tvNimNipLabel = findViewById(R.id.tvNimNipLabel);

        layoutCustomerType = findViewById(R.id.layoutCustomerType);
        layoutNimNip = findViewById(R.id.layoutNimNip);
        layoutAdminCode = findViewById(R.id.layoutAdminCode);

        // Setup listeners
        setupRoleListener();
        setupCustomerTypeListener();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRoleListener() {
        rgRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbCustomer) {
                    // Show customer-specific fields
                    layoutCustomerType.setVisibility(View.VISIBLE);
                    layoutNimNip.setVisibility(View.VISIBLE);
                    layoutAdminCode.setVisibility(View.GONE);
                    updateNimNipLabel();
                } else if (checkedId == R.id.rbAdmin) {
                    // Show admin-specific fields
                    layoutCustomerType.setVisibility(View.GONE);
                    layoutNimNip.setVisibility(View.GONE);
                    layoutAdminCode.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupCustomerTypeListener() {
        rgCustomerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateNimNipLabel();
            }
        });
    }

    private void updateNimNipLabel() {
        if (rbMahasiswa.isChecked()) {
            tvNimNipLabel.setText("NIM (Nomor Induk Mahasiswa)");
            etNimNip.setHint("Masukkan NIM");
        } else if (rbDosen.isChecked()) {
            tvNimNipLabel.setText("NIP (Nomor Induk Pegawai)");
            etNimNip.setHint("Masukkan NIP");
        }
    }

    private void attemptRegister() {
        // Reset errors
        etName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etPhone.setError(null);
        etNimNip.setError(null);
        etAdminCode.setError(null);

        // Get values
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String nimNip = etNimNip.getText().toString().trim();
        String adminCode = etAdminCode.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Determine role and type
        String role = rbCustomer.isChecked() ? "customer" : "admin";
        String type = null;

        if (rbCustomer.isChecked()) {
            type = rbMahasiswa.isChecked() ? "mahasiswa" : "dosen";

            // Validate NIM/NIP for customer
            if (TextUtils.isEmpty(nimNip)) {
                etNimNip.setError("NIM/NIP tidak boleh kosong");
                focusView = etNimNip;
                cancel = true;
            }
        } else {
            // Validate admin code
            if (TextUtils.isEmpty(adminCode)) {
                etAdminCode.setError("Kode admin tidak boleh kosong");
                focusView = etAdminCode;
                cancel = true;
            } else if (!adminCode.equals(ADMIN_CODE)) {
                etAdminCode.setError("Kode admin tidak valid");
                focusView = etAdminCode;
                cancel = true;
            }
        }

        // Validate phone
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("No. HP tidak boleh kosong");
            focusView = etPhone;
            cancel = true;
        } else if (phone.length() < 10) {
            etPhone.setError("No. HP minimal 10 digit");
            focusView = etPhone;
            cancel = true;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            focusView = etPassword;
            cancel = true;
        } else if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            focusView = etPassword;
            cancel = true;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email tidak boleh kosong");
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError("Format email tidak valid");
            focusView = etEmail;
            cancel = true;
        }

        // Validate name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Nama tidak boleh kosong");
            focusView = etName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            performRegister(email, password, name, role, phone, nimNip, type);
        }
    }

    private void performRegister(String email, String password, String name,
                                 String role, String phone, String nimNip, String type) {
        long result = dbHelper.registerUser(email, password, name, role, phone, nimNip, type);

        if (result > 0) {
            String roleText = role.equals("admin") ? "Admin" :
                    (type.equals("mahasiswa") ? "Mahasiswa" : "Dosen");

            Toast.makeText(this, "✅ Registrasi berhasil sebagai " + roleText + "!\nSilakan login.",
                    Toast.LENGTH_LONG).show();

            // Redirect to login
            finish();
        } else {
            Toast.makeText(this, "❌ Email sudah terdaftar. Gunakan email lain.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
}