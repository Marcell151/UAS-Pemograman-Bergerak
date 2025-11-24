package com.example.kantinkampus;

public class Menu {
    private int id;
    private int standId;
    private String nama;
    private int harga;

    public Menu() {}

    public Menu(int id, int standId, String nama, int harga) {
        this.id = id;
        this.standId = standId;
        this.nama = nama;
        this.harga = harga;
    }

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
}