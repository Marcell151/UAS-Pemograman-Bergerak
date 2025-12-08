package com.example.kantinkampus;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // SharedPreferences file name
    private static final String PREF_NAME = "KantinKampusSession";

    // SharedPreferences Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole"; // 'seller' atau 'buyer'
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_NIM_NIP = "userNimNip";
    private static final String KEY_USER_TYPE = "userType"; // 'mahasiswa', 'dosen', null
    private static final String KEY_BUSINESS_LICENSE = "businessLicense";
    private static final String KEY_STAND_ID = "standId";

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.putString(KEY_USER_PHONE, user.getPhone());

        if (user.isBuyer()) {
            editor.putString(KEY_USER_NIM_NIP, user.getNimNip());
            editor.putString(KEY_USER_TYPE, user.getType());
        } else if (user.isSeller()) {
            editor.putString(KEY_BUSINESS_LICENSE, user.getBusinessLicenseNumber());
            if (user.getStandId() != null) {
                editor.putInt(KEY_STAND_ID, user.getStandId());
            }
        }

        editor.commit();
    }

    /**
     * Update stand ID for seller
     */
    public void updateStandId(int standId) {
        editor.putInt(KEY_STAND_ID, standId);
        editor.commit();
    }

    /**
     * Check login status
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Get user ID
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    /**
     * Get user role
     */
    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, null);
    }

    /**
     * Check if user is seller
     */
    public boolean isSeller() {
        return "seller".equals(getUserRole());
    }

    /**
     * Check if user is buyer
     */
    public boolean isBuyer() {
        return "buyer".equals(getUserRole());
    }

    /**
     * Get stand ID (for sellers)
     */
    public int getStandId() {
        return pref.getInt(KEY_STAND_ID, -1);
    }

    /**
     * Check if seller has stand
     */
    public boolean hasStand() {
        return getStandId() > 0;
    }

    /**
     * Get user details
     */
    public User getUserDetails() {
        if (!isLoggedIn()) {
            return null;
        }

        User user = new User();
        user.setId(pref.getInt(KEY_USER_ID, -1));
        user.setEmail(pref.getString(KEY_USER_EMAIL, null));
        user.setName(pref.getString(KEY_USER_NAME, null));
        user.setRole(pref.getString(KEY_USER_ROLE, null));
        user.setPhone(pref.getString(KEY_USER_PHONE, null));

        if (isBuyer()) {
            user.setNimNip(pref.getString(KEY_USER_NIM_NIP, null));
            user.setType(pref.getString(KEY_USER_TYPE, null));
        } else if (isSeller()) {
            user.setBusinessLicenseNumber(pref.getString(KEY_BUSINESS_LICENSE, null));
            int standId = pref.getInt(KEY_STAND_ID, -1);
            if (standId > 0) {
                user.setStandId(standId);
            }
        }

        return user;
    }

    /**
     * Logout user
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    /**
     * Get user name
     */
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "User");
    }

    /**
     * Get user email
     */
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }
}