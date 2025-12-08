package com.example.kantinkampus;

public class CartItem {
    private int id;
    private int userId;
    private Menu menu;
    private int qty;
    private String notes;

    public CartItem() {}

    public CartItem(int id, Menu menu, int qty) {
        this.id = id;
        this.menu = menu;
        this.qty = qty;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper method
    public int getSubtotal() {
        return menu.getHarga() * qty;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", menu=" + (menu != null ? menu.getNama() : "null") +
                ", qty=" + qty +
                '}';
    }
}