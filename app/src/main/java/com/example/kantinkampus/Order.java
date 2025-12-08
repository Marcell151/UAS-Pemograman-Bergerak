package com.example.kantinkampus;

import java.util.List;

public class Order {
    private int id;
    private int userId;
    private int standId;
    private int total;
    private String status; // 'pending_payment', 'paid', 'cooking', 'ready', 'completed', 'cancelled'
    private String paymentMethod; // 'cash', 'ovo', 'gopay'
    private String paymentStatus; // 'unpaid', 'pending_verification', 'verified', 'rejected'
    private String paymentProofPath; // Path ke bukti pembayaran (untuk e-wallet)
    private String sellerAccountNumber; // Nomor rekening penjual (untuk e-wallet)
    private String notes;
    private String createdAt;
    private String updatedAt;

    // Additional info (dari JOIN query)
    private String standName;
    private String userName;
    private String sellerName;
    private List<OrderItem> items;

    // Constructor
    public Order() {}

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

    public int getStandId() {
        return standId;
    }

    public void setStandId(int standId) {
        this.standId = standId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentProofPath() {
        return paymentProofPath;
    }

    public void setPaymentProofPath(String paymentProofPath) {
        this.paymentProofPath = paymentProofPath;
    }

    public String getSellerAccountNumber() {
        return sellerAccountNumber;
    }

    public void setSellerAccountNumber(String sellerAccountNumber) {
        this.sellerAccountNumber = sellerAccountNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // Helper Methods
    public boolean isPendingPayment() {
        return "pending_payment".equals(status);
    }

    public boolean isPaid() {
        return "paid".equals(status);
    }

    public boolean isCooking() {
        return "cooking".equals(status);
    }

    public boolean isReady() {
        return "ready".equals(status);
    }

    public boolean isCompleted() {
        return "completed".equals(status);
    }

    public boolean isCancelled() {
        return "cancelled".equals(status);
    }

    public boolean needsPaymentVerification() {
        return "pending_verification".equals(paymentStatus);
    }

    public boolean isPaymentVerified() {
        return "verified".equals(paymentStatus);
    }

    public boolean isCashPayment() {
        return "cash".equals(paymentMethod);
    }

    public boolean isEwalletPayment() {
        return "ovo".equals(paymentMethod) || "gopay".equals(paymentMethod);
    }

    public String getStatusEmoji() {
        switch (status) {
            case "pending_payment":
                return "💳";
            case "paid":
                return "✅";
            case "cooking":
                return "👨‍🍳";
            case "ready":
                return "🎉";
            case "completed":
                return "✔️";
            case "cancelled":
                return "❌";
            default:
                return "📦";
        }
    }

    public String getStatusText() {
        switch (status) {
            case "pending_payment":
                return "Menunggu Pembayaran";
            case "paid":
                return "Dibayar - Menunggu Konfirmasi";
            case "cooking":
                return "Sedang Dimasak";
            case "ready":
                return "Siap Diambil";
            case "completed":
                return "Selesai";
            case "cancelled":
                return "Dibatalkan";
            default:
                return "Unknown";
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", standId=" + standId +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}