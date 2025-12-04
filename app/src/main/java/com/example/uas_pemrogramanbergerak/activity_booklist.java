package com.example.uas_pemrogramanbergerak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class activity_booklist extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<book_model> daftarBuku, hasilCari;
    book_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerBuku);
        searchView = findViewById(R.id.searchView);

        daftarBuku = book_data.getDaftarBuku();
        hasilCari = new ArrayList<>(daftarBuku);

        adapter = new book_adapter(this, hasilCari);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(activity_booklist.this, activity_bookdetail.class);
            intent.putExtra("position", daftarBuku.indexOf(hasilCari.get(position)));
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        hasilCari.clear();
        for (book_model b : daftarBuku) {
            if (b.getJudul().toLowerCase().contains(text.toLowerCase()) ||
                    b.getPenulis().toLowerCase().contains(text.toLowerCase())) {
                hasilCari.add(b);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
