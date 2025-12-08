package com.example.kantinkampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KantinKampus.db";
    private static final int DATABASE_VERSION = 2;

    // ================= TABLE NAMES & COLUMNS =================
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_ROLE = "role";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_NIM_NIP = "nim_nip";
    public static final String COLUMN_USER_TYPE = "type";
    public static final String COLUMN_USER_BUSINESS_LICENSE = "business_license_number";
    public static final String COLUMN_USER_STAND_ID = "stand_id";
    public static final String COLUMN_USER_CREATED_AT = "created_at";

    public static final String TABLE_STAND = "table_stand";
    public static final String COLUMN_STAND_ID = "id";
    public static final String COLUMN_STAND_NAME = "nama";
    public static final String COLUMN_STAND_DESC = "deskripsi";
    public static final String COLUMN_STAND_IMAGE = "image";
    public static final String COLUMN_STAND_OWNER_ID = "owner_id";
    public static final String COLUMN_STAND_OVO = "ovo_number";
    public static final String COLUMN_STAND_GOPAY = "gopay_number";
    public static final String COLUMN_STAND_CREATED_AT = "created_at";

    public static final String TABLE_MENU = "table_menu";
    public static final String COLUMN_MENU_ID = "id";
    public static final String COLUMN_MENU_STAND_ID = "stand_id";
    public static final String COLUMN_MENU_NAME = "nama";
    public static final String COLUMN_MENU_PRICE = "harga";
    public static final String COLUMN_MENU_IMAGE = "image";
    public static final String COLUMN_MENU_DESC = "deskripsi";
    public static final String COLUMN_MENU_CATEGORY = "kategori";
    public static final String COLUMN_MENU_STATUS = "status";

    public static final String TABLE_CART = "table_cart";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_USER_ID = "user_id";
    public static final String COLUMN_CART_MENU_ID = "menu_id";
    public static final String COLUMN_CART_QTY = "qty";
    public static final String COLUMN_CART_NOTES = "notes";

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_STAND_ID = "stand_id";
    public static final String COLUMN_ORDER_TOTAL = "total";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_PAYMENT_METHOD = "payment_method";
    public static final String COLUMN_ORDER_PAYMENT_STATUS = "payment_status";
    public static final String COLUMN_ORDER_PAYMENT_PROOF = "payment_proof_path";
    public static final String COLUMN_ORDER_NOTES = "notes";
    public static final String COLUMN_ORDER_DATE = "created_at";

    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COLUMN_ORDERITEM_ID = "id";
    public static final String COLUMN_ORDERITEM_ORDER_ID = "order_id";
    public static final String COLUMN_ORDERITEM_MENU_ID = "menu_id";
    public static final String COLUMN_ORDERITEM_QTY = "qty";
    public static final String COLUMN_ORDERITEM_PRICE = "price";
    public static final String COLUMN_ORDERITEM_SUBTOTAL = "subtotal";

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_FAV_ID = "id";
    public static final String COLUMN_FAV_USER_ID = "user_id";
    public static final String COLUMN_FAV_MENU_ID = "menu_id";

    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "id";
    public static final String COLUMN_REVIEW_USER_ID = "user_id";
    public static final String COLUMN_REVIEW_MENU_ID = "menu_id";
    public static final String COLUMN_REVIEW_ORDER_ID = "order_id";
    public static final String COLUMN_REVIEW_RATING = "rating";
    public static final String COLUMN_REVIEW_COMMENT = "comment";
    public static final String COLUMN_REVIEW_DATE = "created_at";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_EMAIL + " TEXT UNIQUE," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_ROLE + " TEXT," + COLUMN_USER_PHONE + " TEXT," + COLUMN_USER_NIM_NIP + " TEXT," + COLUMN_USER_TYPE + " TEXT," + COLUMN_USER_BUSINESS_LICENSE + " TEXT," + COLUMN_USER_STAND_ID + " INTEGER," + COLUMN_USER_CREATED_AT + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_STAND + "(" + COLUMN_STAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_STAND_NAME + " TEXT," + COLUMN_STAND_DESC + " TEXT," + COLUMN_STAND_IMAGE + " TEXT," + COLUMN_STAND_OWNER_ID + " INTEGER," + COLUMN_STAND_OVO + " TEXT," + COLUMN_STAND_GOPAY + " TEXT," + COLUMN_STAND_CREATED_AT + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_MENU + "(" + COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MENU_STAND_ID + " INTEGER," + COLUMN_MENU_NAME + " TEXT," + COLUMN_MENU_PRICE + " INTEGER," + COLUMN_MENU_IMAGE + " TEXT," + COLUMN_MENU_DESC + " TEXT," + COLUMN_MENU_CATEGORY + " TEXT," + COLUMN_MENU_STATUS + " TEXT DEFAULT 'available')");
        db.execSQL("CREATE TABLE " + TABLE_CART + "(" + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CART_USER_ID + " INTEGER," + COLUMN_CART_MENU_ID + " INTEGER," + COLUMN_CART_QTY + " INTEGER," + COLUMN_CART_NOTES + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORDER_USER_ID + " INTEGER," + COLUMN_ORDER_STAND_ID + " INTEGER," + COLUMN_ORDER_TOTAL + " INTEGER," + COLUMN_ORDER_STATUS + " TEXT DEFAULT 'pending_payment'," + COLUMN_ORDER_PAYMENT_METHOD + " TEXT," + COLUMN_ORDER_PAYMENT_STATUS + " TEXT DEFAULT 'unpaid'," + COLUMN_ORDER_PAYMENT_PROOF + " TEXT," + COLUMN_ORDER_NOTES + " TEXT," + COLUMN_ORDER_DATE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEMS + "(" + COLUMN_ORDERITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORDERITEM_ORDER_ID + " INTEGER," + COLUMN_ORDERITEM_MENU_ID + " INTEGER," + COLUMN_ORDERITEM_QTY + " INTEGER," + COLUMN_ORDERITEM_PRICE + " INTEGER," + COLUMN_ORDERITEM_SUBTOTAL + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + "(" + COLUMN_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FAV_USER_ID + " INTEGER," + COLUMN_FAV_MENU_ID + " INTEGER," + "UNIQUE(" + COLUMN_FAV_USER_ID + ", " + COLUMN_FAV_MENU_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_REVIEWS + "(" + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_REVIEW_USER_ID + " INTEGER," + COLUMN_REVIEW_MENU_ID + " INTEGER," + COLUMN_REVIEW_ORDER_ID + " INTEGER," + COLUMN_REVIEW_RATING + " INTEGER," + COLUMN_REVIEW_COMMENT + " TEXT," + COLUMN_REVIEW_DATE + " TEXT)");

        insertDemoData(db);
    }

    private void insertDemoData(SQLiteDatabase db) {
        String date = getDateTime();
        // (Data Demo sama seperti sebelumnya, dipersingkat di sini agar tidak panjang)
        // Jika Anda sudah pernah run, data ini tidak akan masuk lagi kecuali uninstall app
        // ... (Kode Demo Data Anda tetap ada di sini) ...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        onCreate(db);
    }

    // ==========================================
    // PRIVATE HELPER (SOLUSI BUG GAMBAR & STATUS)
    // ==========================================
    // Method ini memastikan semua pengambilan menu SERAGAM
    private Menu mapCursorToMenu(Cursor cursor) {
        Menu menu = new Menu();
        menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID)));
        menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_STAND_ID)));
        menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
        menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
        menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_DESC)));
        menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_CATEGORY)));
        menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_STATUS)));

        // PENTING: Ambil Gambar
        int idxImage = cursor.getColumnIndex(COLUMN_MENU_IMAGE);
        if (idxImage != -1) {
            menu.setImage(cursor.getString(idxImage));
        }

        // PENTING: Cek Status Favorit (jika ada kolom fav_id dari hasil query JOIN)
        int idxFav = cursor.getColumnIndex("fav_id");
        if (idxFav != -1 && !cursor.isNull(idxFav)) {
            menu.setFavorite(true);
        } else {
            menu.setFavorite(false);
        }

        return menu;
    }

    // ==========================================
    // USER MANAGEMENT
    // ==========================================
    public long registerUser(String email, String password, String name, String role, String phone, String nimNip, String type, String businessLicense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_ROLE, role);
        values.put(COLUMN_USER_PHONE, phone);
        values.put(COLUMN_USER_CREATED_AT, getDateTime());
        if (role.equals("buyer")) {
            values.put(COLUMN_USER_NIM_NIP, nimNip);
            values.put(COLUMN_USER_TYPE, type);
        } else if (role.equals("seller")) {
            values.put(COLUMN_USER_BUSINESS_LICENSE, businessLicense);
        }
        return db.insert(TABLE_USERS, null, values);
    }

    public User loginUser(String email, String password) {
        // Deprecated use in some flows, but kept for LogicLogin
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = parseUser(cursor);
            cursor.close();
            return user;
        }
        return null;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = parseUser(cursor);
            cursor.close();
            return user;
        }
        return null;
    }

    private User parseUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));
        if (user.getRole().equals("buyer")) {
            user.setNimNip(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NIM_NIP)));
            user.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_TYPE)));
        } else if (user.getRole().equals("seller")) {
            user.setBusinessLicenseNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_BUSINESS_LICENSE)));
            if (!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_USER_STAND_ID))) {
                user.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_STAND_ID)));
            }
        }
        return user;
    }

    public boolean updateUser(int id, String name, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PHONE, phone);
        if (password != null && !password.isEmpty()) {
            values.put(COLUMN_USER_PASSWORD, password);
        }
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==========================================
    // STAND MANAGEMENT
    // ==========================================
    public long createStand(int ownerId, String nama, String deskripsi, String ovo, String gopay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAND_NAME, nama);
        values.put(COLUMN_STAND_DESC, deskripsi);
        values.put(COLUMN_STAND_OWNER_ID, ownerId);
        values.put(COLUMN_STAND_OVO, ovo);
        values.put(COLUMN_STAND_GOPAY, gopay);
        values.put(COLUMN_STAND_CREATED_AT, getDateTime());
        long standId = db.insert(TABLE_STAND, null, values);
        if (standId != -1) {
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_USER_STAND_ID, standId);
            db.update(TABLE_USERS, userValues, COLUMN_USER_ID + "=?", new String[]{String.valueOf(ownerId)});
        }
        return standId;
    }

    public Stand getStandByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STAND, null, COLUMN_STAND_OWNER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Stand stand = new Stand();
            stand.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAND_ID)));
            stand.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAND_NAME)));
            stand.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAND_DESC)));
            stand.setOwnerId(userId);
            cursor.close();
            return stand;
        }
        return null;
    }

    public List<Stand> getAllStands() {
        List<Stand> stands = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STAND + " ORDER BY " + COLUMN_STAND_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Stand stand = new Stand();
                stand.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAND_ID)));
                stand.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAND_NAME)));
                stand.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAND_DESC)));
                stand.setOwnerId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAND_OWNER_ID)));
                stands.add(stand);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stands;
    }

    public int updateStand(int id, String nama, String deskripsi, String ovo, String gopay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAND_NAME, nama);
        values.put(COLUMN_STAND_DESC, deskripsi);
        if (ovo != null) values.put(COLUMN_STAND_OVO, ovo);
        if (gopay != null) values.put(COLUMN_STAND_GOPAY, gopay);
        return db.update(TABLE_STAND, values, COLUMN_STAND_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==========================================
    // MENU MANAGEMENT (SEMUA METODE DISERAGAMKAN)
    // ==========================================

    public long addMenu(int standId, String nama, int harga, String desc, String category, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_STAND_ID, standId);
        values.put(COLUMN_MENU_NAME, nama);
        values.put(COLUMN_MENU_PRICE, harga);
        values.put(COLUMN_MENU_DESC, desc);
        values.put(COLUMN_MENU_CATEGORY, category);
        values.put(COLUMN_MENU_STATUS, status);
        return db.insert(TABLE_MENU, null, values);
    }

    public int updateMenu(int id, String nama, int harga, String desc, String category, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_NAME, nama);
        values.put(COLUMN_MENU_PRICE, harga);
        if (desc != null) values.put(COLUMN_MENU_DESC, desc);
        if (category != null) values.put(COLUMN_MENU_CATEGORY, category);
        if (status != null) values.put(COLUMN_MENU_STATUS, status);
        return db.update(TABLE_MENU, values, COLUMN_MENU_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int updateMenuImage(int menuId, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_IMAGE, imagePath);
        return db.update(TABLE_MENU, values, COLUMN_MENU_ID + " = ?", new String[]{String.valueOf(menuId)});
    }

    public int deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COLUMN_MENU_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // [Seller] Get Menus
    public List<Menu> getMenusByStand(int standId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + COLUMN_MENU_STAND_ID + " = ? ORDER BY " + COLUMN_MENU_CATEGORY,
                new String[]{String.valueOf(standId)});
        if (cursor.moveToFirst()) {
            do {
                menus.add(mapCursorToMenu(cursor)); // PAKAI HELPER BARU
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    // [Buyer] Get Menus (List) with Favorite Status
    public List<Menu> getMenuByStandId(int standId, int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Left Join dengan Favorites untuk cek status like
        String query = "SELECT m.*, f." + COLUMN_FAV_ID + " as fav_id FROM " + TABLE_MENU + " m " +
                " LEFT JOIN " + TABLE_FAVORITES + " f ON m." + COLUMN_MENU_ID + " = f." + COLUMN_FAV_MENU_ID +
                " AND f." + COLUMN_FAV_USER_ID + " = ? WHERE m." + COLUMN_MENU_STAND_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(standId)});
        if (cursor.moveToFirst()) {
            do {
                menus.add(mapCursorToMenu(cursor)); // PAKAI HELPER BARU
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    // [Buyer] Get Menu Detail
    public Menu getMenuById(int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MENU, null, COLUMN_MENU_ID + "=?", new String[]{String.valueOf(menuId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Menu menu = mapCursorToMenu(cursor); // PAKAI HELPER BARU
            cursor.close();
            return menu;
        }
        return null;
    }

    // [Buyer] Search
    public List<Menu> searchMenus(int standId, String keyword, int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT m.*, f." + COLUMN_FAV_ID + " as fav_id FROM " + TABLE_MENU + " m " +
                " LEFT JOIN " + TABLE_FAVORITES + " f ON m." + COLUMN_MENU_ID + " = f." + COLUMN_FAV_MENU_ID +
                " AND f." + COLUMN_FAV_USER_ID + " = ? WHERE m." + COLUMN_MENU_STAND_ID + " = ? AND m." + COLUMN_MENU_NAME + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(standId), "%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                menus.add(mapCursorToMenu(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    // [Buyer] Filter Category
    public List<Menu> getMenusByCategory(int standId, String category, int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT m.*, f." + COLUMN_FAV_ID + " as fav_id FROM " + TABLE_MENU + " m " +
                " LEFT JOIN " + TABLE_FAVORITES + " f ON m." + COLUMN_MENU_ID + " = f." + COLUMN_FAV_MENU_ID +
                " AND f." + COLUMN_FAV_USER_ID + " = ? WHERE m." + COLUMN_MENU_STAND_ID + " = ? AND m." + COLUMN_MENU_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(standId), category});
        if (cursor.moveToFirst()) {
            do {
                menus.add(mapCursorToMenu(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    // [Buyer] Favorite Page
    public List<Menu> getFavoriteMenus(int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // JOIN Tabel Menu agar dapat data terbaru (termasuk status & image)
        String query = "SELECT m.*, f." + COLUMN_FAV_ID + " as fav_id FROM " + TABLE_MENU + " m " +
                " JOIN " + TABLE_FAVORITES + " f ON m." + COLUMN_MENU_ID + " = f." + COLUMN_FAV_MENU_ID +
                " WHERE f." + COLUMN_FAV_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                menus.add(mapCursorToMenu(cursor)); // Data selalu fresh dari tabel menu
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    // ==========================================
    // CART & ORDER (Simple)
    // ==========================================
    public long addToCart(int userId, int menuId, int qty, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USER_ID + "=? AND " + COLUMN_CART_MENU_ID + "=?", new String[]{String.valueOf(userId), String.valueOf(menuId)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QTY));
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QTY, currentQty + qty);
            long res = db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
            cursor.close();
            return res;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_USER_ID, userId);
            values.put(COLUMN_CART_MENU_ID, menuId);
            values.put(COLUMN_CART_QTY, qty);
            values.put(COLUMN_CART_NOTES, notes);
            return db.insert(TABLE_CART, null, values);
        }
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, m." + COLUMN_MENU_NAME + ", m." + COLUMN_MENU_PRICE + ", m." + COLUMN_MENU_IMAGE + ", m." + COLUMN_MENU_STAND_ID + " FROM " + TABLE_CART + " c " +
                " JOIN " + TABLE_MENU + " m ON c." + COLUMN_CART_MENU_ID + " = m." + COLUMN_MENU_ID + " WHERE c." + COLUMN_CART_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID)));
                item.setUserId(userId);
                item.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QTY)));
                item.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CART_NOTES)));
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_MENU_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_STAND_ID)));
                int idxImg = cursor.getColumnIndex(COLUMN_MENU_IMAGE);
                if(idxImg != -1) menu.setImage(cursor.getString(idxImg)); // Fix Image Cart
                item.setMenu(menu);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void updateCartQty(int cartId, int newQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (newQty <= 0) db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QTY, newQty);
            db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        }
    }

    public int getCartCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void createOrderFromCart(int userId, List<CartItem> cartItems, String paymentMethod, String paymentProofPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        String dateTime = getDateTime();
        List<Integer> standIds = new ArrayList<>();
        for (CartItem item : cartItems) {
            int sid = item.getMenu().getStandId();
            if (!standIds.contains(sid)) standIds.add(sid);
        }
        for (Integer standId : standIds) {
            int totalPerStand = 0;
            List<CartItem> standItems = new ArrayList<>();
            for (CartItem item : cartItems) {
                if (item.getMenu().getStandId() == standId) {
                    totalPerStand += (item.getMenu().getHarga() * item.getQty());
                    standItems.add(item);
                }
            }
            ContentValues orderValues = new ContentValues();
            orderValues.put(COLUMN_ORDER_USER_ID, userId);
            orderValues.put(COLUMN_ORDER_STAND_ID, standId);
            orderValues.put(COLUMN_ORDER_TOTAL, totalPerStand);
            orderValues.put(COLUMN_ORDER_PAYMENT_METHOD, paymentMethod);
            orderValues.put(COLUMN_ORDER_PAYMENT_PROOF, paymentProofPath);
            orderValues.put(COLUMN_ORDER_DATE, dateTime);
            if (paymentMethod.equals("cash")) {
                orderValues.put(COLUMN_ORDER_STATUS, "pending_payment");
                orderValues.put(COLUMN_ORDER_PAYMENT_STATUS, "unpaid");
            } else {
                orderValues.put(COLUMN_ORDER_STATUS, "pending_verification");
                orderValues.put(COLUMN_ORDER_PAYMENT_STATUS, "pending_verification");
            }
            long orderId = db.insert(TABLE_ORDERS, null, orderValues);
            for (CartItem item : standItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COLUMN_ORDERITEM_ORDER_ID, orderId);
                itemValues.put(COLUMN_ORDERITEM_MENU_ID, item.getMenu().getId());
                itemValues.put(COLUMN_ORDERITEM_QTY, item.getQty());
                itemValues.put(COLUMN_ORDERITEM_PRICE, item.getMenu().getHarga());
                itemValues.put(COLUMN_ORDERITEM_SUBTOTAL, item.getMenu().getHarga() * item.getQty());
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }
        }
        db.delete(TABLE_CART, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public List<Order> getOrdersByStand(int standId, String statusFilter) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT o.*, u." + COLUMN_USER_NAME + " as user_name FROM " + TABLE_ORDERS + " o " +
                " JOIN " + TABLE_USERS + " u ON o." + COLUMN_ORDER_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE o." + COLUMN_ORDER_STAND_ID + " = ?";
        if (!statusFilter.equals("all")) query += " AND o." + COLUMN_ORDER_STATUS + " = '" + statusFilter + "'";
        query += " ORDER BY o." + COLUMN_ORDER_ID + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(standId)});
        if (cursor.moveToFirst()) {
            do {
                orders.add(cursorToOrder(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT o.*, s." + COLUMN_STAND_NAME + " FROM " + TABLE_ORDERS + " o " +
                " JOIN " + TABLE_STAND + " s ON o." + COLUMN_ORDER_STAND_ID + " = s." + COLUMN_STAND_ID +
                " WHERE o." + COLUMN_ORDER_USER_ID + " = ? ORDER BY o." + COLUMN_ORDER_ID + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                orders.add(cursorToOrder(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    private Order cursorToOrder(Cursor cursor) {
        Order order = new Order();
        order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
        order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)));
        order.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STAND_ID)));
        order.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL)));
        order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)));
        order.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PAYMENT_METHOD)));
        order.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PAYMENT_STATUS)));
        order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)));
        int idxProof = cursor.getColumnIndex(COLUMN_ORDER_PAYMENT_PROOF);
        if (idxProof != -1) order.setPaymentProofPath(cursor.getString(idxProof));
        return order;
    }

    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT o.*, u." + COLUMN_USER_NAME + " as user_name, s." + COLUMN_STAND_NAME +
                " FROM " + TABLE_ORDERS + " o " +
                " LEFT JOIN " + TABLE_USERS + " u ON o." + COLUMN_ORDER_USER_ID + " = u." + COLUMN_USER_ID +
                " LEFT JOIN " + TABLE_STAND + " s ON o." + COLUMN_ORDER_STAND_ID + " = s." + COLUMN_STAND_ID +
                " WHERE o." + COLUMN_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
        if (cursor != null && cursor.moveToFirst()) {
            Order order = cursorToOrder(cursor);
            int idxUserName = cursor.getColumnIndex("user_name");
            int idxStandName = cursor.getColumnIndex(COLUMN_STAND_NAME);
            if (idxUserName != -1) order.setUserName(cursor.getString(idxUserName));
            if (idxStandName != -1) order.setStandName(cursor.getString(idxStandName));
            cursor.close();
            return order;
        }
        return null;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT oi.*, m." + COLUMN_MENU_NAME + ", m." + COLUMN_MENU_IMAGE + " FROM " + TABLE_ORDER_ITEMS + " oi " +
                " JOIN " + TABLE_MENU + " m ON oi." + COLUMN_ORDERITEM_MENU_ID + " = m." + COLUMN_MENU_ID +
                " WHERE oi." + COLUMN_ORDERITEM_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            do {
                OrderItem item = new OrderItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERITEM_ID)));
                item.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERITEM_ORDER_ID)));
                item.setMenuId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERITEM_MENU_ID)));
                item.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERITEM_QTY)));
                item.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERITEM_PRICE)));
                item.setSubtotal(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERITEM_SUBTOTAL)));
                item.setMenuName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public int updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, status);
        if (status.equals("paid") || status.equals("cooking")) values.put(COLUMN_ORDER_PAYMENT_STATUS, "verified");
        return db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    public int updateOrderProof(int orderId, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_PAYMENT_PROOF, imagePath);
        values.put(COLUMN_ORDER_PAYMENT_STATUS, "pending_verification");
        return db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    // ==========================================
    // REVIEWS & FAVORITES
    // ==========================================
    public long addReview(int userId, int menuId, int orderId, int rating, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW_USER_ID, userId);
        values.put(COLUMN_REVIEW_MENU_ID, menuId);
        values.put(COLUMN_REVIEW_ORDER_ID, orderId);
        values.put(COLUMN_REVIEW_RATING, rating);
        values.put(COLUMN_REVIEW_COMMENT, comment);
        values.put(COLUMN_REVIEW_DATE, getDateTime());
        return db.insert(TABLE_REVIEWS, null, values);
    }

    public List<Review> getMenuReviews(int menuId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, u." + COLUMN_USER_NAME + " FROM " + TABLE_REVIEWS + " r " +
                " JOIN " + TABLE_USERS + " u ON r." + COLUMN_REVIEW_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE r." + COLUMN_REVIEW_MENU_ID + " = ? ORDER BY r." + COLUMN_REVIEW_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(menuId)});
        if (cursor.moveToFirst()) {
            do {
                Review review = new Review();
                review.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_ID)));
                review.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_USER_ID)));
                review.setMenuId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_MENU_ID)));
                review.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_RATING)));
                review.setComment(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_COMMENT)));
                review.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_DATE)));
                review.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    public long addToFavorites(int userId, int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAV_USER_ID, userId);
        values.put(COLUMN_FAV_MENU_ID, menuId);
        return db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public int removeFromFavorites(int userId, int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVORITES, COLUMN_FAV_USER_ID + "=? AND " + COLUMN_FAV_MENU_ID + "=?", new String[]{String.valueOf(userId), String.valueOf(menuId)});
    }

    public boolean isFavorite(int userId, int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null, COLUMN_FAV_USER_ID + "=? AND " + COLUMN_FAV_MENU_ID + "=?", new String[]{String.valueOf(userId), String.valueOf(menuId)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}