package com.example.uas_pemrogramanbergerak;

import java.util.ArrayList;

public class book_data {
    private static ArrayList<book_model> daftarBuku;

    public static ArrayList<book_model> getDaftarBuku() {
        if (daftarBuku == null) {
            daftarBuku = new ArrayList<>();
            daftarBuku.add(new book_model("Pemrograman Java Dasar", "Andi Setiawan", "Panduan belajar Java dari nol.", R.drawable.cover_java));
            daftarBuku.add(new book_model("Algoritma & Struktur Data", "Budi Raharjo", "Konsep logika dan struktur data.", R.drawable.cover_algoritma));
            daftarBuku.add(new book_model("Android Studio for Beginner", "Citra Dewi", "Langkah-langkah membuat aplikasi Android.", R.drawable.cover_android));
            daftarBuku.add(new book_model("Jaringan Komputer", "Eko Prasetyo", "Dasar komunikasi data dan jaringan.", R.drawable.cover_jaringan));
            daftarBuku.add(new book_model("Basis Data MySQL", "Sinta Amelia", "Pengenalan sistem basis data relasional.", R.drawable.cover_mysql));
        }
        return daftarBuku;
    }
}
