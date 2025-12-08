package com.example.kantinkampus;

public class Review {
    private int id;
    private int userId;
    private int menuId;
    private int orderId;
    private int rating; // 1-5
    private String comment;
    private String createdAt;

    // Additional field from JOIN query
    private String userName;

    public Review() {}

    public Review(int id, int userId, int menuId, int orderId, int rating, String comment, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.orderId = orderId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
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

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Helper methods
    public String getRatingStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("⭐");
        }
        return stars.toString();
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", menuId=" + menuId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}