package com.example.uas_pemrogramanbergerak;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class activity_borrowedbooks extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<book_model> bukuDipinjam;
    book_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowedbooks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerDipinjam);
        bukuDipinjam = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date sekarang = new Date();

        for (book_model b : book_data.getDaftarBuku()) {
            if (b.isDipinjam()) {
                if (b.getTanggalPinjam() != null) {
                    try {
                        Date tgl = sdf.parse(b.getTanggalPinjam());
                        long diff = sekarang.getTime() - tgl.getTime();
                        long hari = diff / (1000 * 60 * 60 * 24);

                        if (hari >= 7) {
                            b.setDeskripsi("⚠️ Harap segera dikembalikan (lebih dari 7 hari)");
                        } else {
                            b.setDeskripsi("Dipinjam selama " + hari + " hari");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                bukuDipinjam.add(b);
            }
        }

        adapter = new book_adapter(this, bukuDipinjam);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
