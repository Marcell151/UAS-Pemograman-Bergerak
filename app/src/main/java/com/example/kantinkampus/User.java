package com.example.kantinkampus;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String role; // 'admin' atau 'customer'
    private String phone;
    private String nimNip; // NIM untuk mahasiswa, NIP untuk dosen
    private String type; // 'mahasiswa', 'dosen', atau null untuk admin
    private String createdAt;

    public User() {}

    public User(int id, String email, String name, String role, String phone, String nimNip, String type) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.nimNip = nimNip;
        this.type = type;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNimNip() {
        return nimNip;
    }

    public void setNimNip(String nimNip) {
        this.nimNip = nimNip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isAdmin() {
        return "admin".equals(role);
    }

    public boolean isCustomer() {
        return "customer".equals(role);
    }

    public boolean isMahasiswa() {
        return "mahasiswa".equals(type);
    }

    public boolean isDosen() {
        return "dosen".equals(type);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}