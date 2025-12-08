package com.example.kantinkampus;

public class Menu {
    private int id;
    private int standId;
    private String nama;
    private int harga;
    private String image;
    private String deskripsi;
    private String kategori;
    private String status; // 'available' or 'unavailable'

    // Additional fields for reviews and favorites
    private float averageRating;
    private int totalReviews;
    private boolean isFavorite;

    public Menu() {}

    public Menu(int id, int standId, String nama, int harga) {
        this.id = id;
        this.standId = standId;
        this.nama = nama;
        this.harga = harga;
        this.status = "available";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStandId() {
        return standId;
    }

    public void setStandId(int standId) {
        this.standId = standId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // Helper methods
    public boolean isAvailable() {
        return "available".equals(status);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", standId=" + standId +
                ", nama='" + nama + '\'' +
                ", harga=" + harga +
                ", kategori='" + kategori + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}