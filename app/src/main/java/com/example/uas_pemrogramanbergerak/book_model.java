package com.example.uas_pemrogramanbergerak;

public class book_model {
    private String judul;
    private String penulis;
    private String deskripsi;
    private String tanggalPinjam;
    private boolean dipinjam;
    private int coverResId; // 🆕 gambar cover buku

    public book_model(String judul, String penulis, String deskripsi, int coverResId) {
        this.judul = judul;
        this.penulis = penulis;
        this.deskripsi = deskripsi;
        this.coverResId = coverResId;
        this.dipinjam = false;
    }

    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String d) { this.deskripsi = d; }
    public boolean isDipinjam() { return dipinjam; }
    public void setDipinjam(boolean d) { this.dipinjam = d; }
    public String getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(String t) { this.tanggalPinjam = t; }

    public int getCoverResId() { return coverResId; }
    public void setCoverResId(int id) { this.coverResId = id; }
}
