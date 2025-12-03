package com.example.kantinkampus;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SessionManager untuk mengelola sesi login user
 * Menggunakan SharedPreferences untuk menyimpan data user yang sedang login
 */
public class SessionManager {
    private static final String PREF_NAME = "KantinKampusSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_NIM_NIP = "userNimNip";
    private static final String KEY_USER_TYPE = "userType";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Simpan sesi login user
     */
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_NIM_NIP, user.getNimNip());
        editor.putString(KEY_USER_TYPE, user.getType());
        editor.apply();
    }

    /**
     * Cek apakah user sudah login
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Dapatkan data user yang sedang login
     */
    public User getUserDetails() {
        if (!isLoggedIn()) {
            return null;
        }

        User user = new User();
        user.setId(sharedPreferences.getInt(KEY_USER_ID, -1));
        user.setEmail(sharedPreferences.getString(KEY_USER_EMAIL, ""));
        user.setName(sharedPreferences.getString(KEY_USER_NAME, ""));
        user.setRole(sharedPreferences.getString(KEY_USER_ROLE, ""));
        user.setPhone(sharedPreferences.getString(KEY_USER_PHONE, ""));
        user.setNimNip(sharedPreferences.getString(KEY_USER_NIM_NIP, ""));
        user.setType(sharedPreferences.getString(KEY_USER_TYPE, ""));
        return user;
    }

    /**
     * Dapatkan ID user yang sedang login
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    /**
     * Dapatkan nama user yang sedang login
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    /**
     * Dapatkan role user yang sedang login
     */
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "");
    }

    /**
     * Cek apakah user adalah admin
     */
    public boolean isAdmin() {
        return "admin".equals(getUserRole());
    }

    /**
     * Cek apakah user adalah customer
     */
    public boolean isCustomer() {
        return "customer".equals(getUserRole());
    }

    /**
     * Logout user
     */
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    /**
     * Update nama user
     */
    public void updateUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    /**
     * Update phone user
     */
    public void updateUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }
}