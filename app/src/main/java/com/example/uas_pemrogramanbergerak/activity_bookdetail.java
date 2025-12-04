package com.example.uas_pemrogramanbergerak;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class activity_bookdetail extends AppCompatActivity {

    TextView tvJudul, tvPenulis, tvDeskripsi, tvStatus;
    ImageView imgDetailBuku;
    Button btnPinjamKembalikan;
    book_model buku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int position = getIntent().getIntExtra("position", -1);
        buku = book_data.getDaftarBuku().get(position);

        if (buku.getCoverResId() != 0)
            imgDetailBuku.setImageResource(buku.getCoverResId());
        else
            imgDetailBuku.setImageResource(R.drawable.ic_book_placeholder);
        tvJudul = findViewById(R.id.tvJudul);
        tvPenulis = findViewById(R.id.tvPenulis);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvStatus = findViewById(R.id.tvStatus);
        btnPinjamKembalikan = findViewById(R.id.btnPinjamKembalikan);

        tvJudul.setText(buku.getJudul());
        tvPenulis.setText("Penulis: " + buku.getPenulis());
        tvDeskripsi.setText(buku.getDeskripsi());
        updateStatus();

        btnPinjamKembalikan.setOnClickListener(v -> {
            if (buku.isDipinjam()) {
                buku.setDipinjam(false);
                buku.setTanggalPinjam(null);
                Toast.makeText(this, "📗 Buku dikembalikan", Toast.LENGTH_SHORT).show();
            } else {
                buku.setDipinjam(true);
                String tgl = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                buku.setTanggalPinjam(tgl);
                Toast.makeText(this, "📘 Buku dipinjam", Toast.LENGTH_SHORT).show();
            }
            updateStatus();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateStatus() {
        if (buku.isDipinjam()) {
            tvStatus.setText("Status: Dipinjam (Sejak " + buku.getTanggalPinjam() + ")");
            tvStatus.setTextColor(0xFFD32F2F);
            btnPinjamKembalikan.setText("Kembalikan Buku");
        } else {
            tvStatus.setText("Status: Tersedia");
            tvStatus.setTextColor(0xFF388E3C);
            btnPinjamKembalikan.setText("Pinjam Buku");
        }
    }
}
