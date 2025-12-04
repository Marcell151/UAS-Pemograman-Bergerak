package com.example.uas_pemrogramanbergerak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class activity_main extends AppCompatActivity {

    Button btnDaftarBuku, btnBukuDipinjam, btnTentang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDaftarBuku = findViewById(R.id.btnDaftarBuku);
        btnBukuDipinjam = findViewById(R.id.btnBukuDipinjam);
        btnTentang = findViewById(R.id.btnTentang);

        btnDaftarBuku.setOnClickListener(v ->
                startActivity(new Intent(activity_main.this, activity_booklist.class)));

        btnBukuDipinjam.setOnClickListener(v ->
                startActivity(new Intent(activity_main.this, activity_borrowedbooks.class)));

        btnTentang.setOnClickListener(v ->
                startActivity(new Intent(activity_main.this, activity_about.class)));
    }
}
