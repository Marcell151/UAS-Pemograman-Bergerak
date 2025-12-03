package com.example.kantinkampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kantinkampus_v2.db";
    private static final int DATABASE_VERSION = 2;

    // Table Users (untuk autentikasi)
    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_NAME = "name";
    private static final String USER_ROLE = "role"; // 'admin' atau 'customer'
    private static final String USER_PHONE = "phone";
    private static final String USER_NIM_NIP = "nim_nip"; // NIM untuk mahasiswa, NIP untuk dosen
    private static final String USER_TYPE = "type"; // 'mahasiswa', 'dosen', atau null untuk admin
    private static final String USER_CREATED_AT = "created_at";

    // Table Stand
    private static final String TABLE_STAND = "table_stand";
    private static final String STAND_ID = "id";
    private static final String STAND_NAMA = "nama";
    private static final String STAND_DESKRIPSI = "deskripsi";
    private static final String STAND_IMAGE = "image";
    private static final String STAND_OWNER_ID = "owner_id";
    private static final String STAND_CREATED_AT = "created_at";

    // Table Menu
    private static final String TABLE_MENU = "table_menu";
    private static final String MENU_ID = "id";
    private static final String MENU_STAND_ID = "stand_id";
    private static final String MENU_NAMA = "nama";
    private static final String MENU_HARGA = "harga";
    private static final String MENU_IMAGE = "image";
    private static final String MENU_DESKRIPSI = "deskripsi";
    private static final String MENU_KATEGORI = "kategori";
    private static final String MENU_STATUS = "status"; // 'available' atau 'unavailable'
    private static final String MENU_CREATED_AT = "created_at";

    // Table Cart
    private static final String TABLE_CART = "table_cart";
    private static final String CART_ID = "id";
    private static final String CART_USER_ID = "user_id";
    private static final String CART_MENU_ID = "menu_id";
    private static final String CART_QTY = "qty";
    private static final String CART_NOTES = "notes";

    // Table Orders (menggantikan History)
    private static final String TABLE_ORDERS = "orders";
    private static final String ORDER_ID = "id";
    private static final String ORDER_USER_ID = "user_id";
    private static final String ORDER_STAND_ID = "stand_id";
    private static final String ORDER_TOTAL = "total";
    private static final String ORDER_STATUS = "status"; // 'pending', 'processing', 'ready', 'completed', 'cancelled'
    private static final String ORDER_PAYMENT_METHOD = "payment_method";
    private static final String ORDER_PAYMENT_STATUS = "payment_status"; // 'unpaid', 'paid'
    private static final String ORDER_NOTES = "notes";
    private static final String ORDER_CREATED_AT = "created_at";
    private static final String ORDER_UPDATED_AT = "updated_at";

    // Table Order Items
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String ORDER_ITEM_ID = "id";
    private static final String ORDER_ITEM_ORDER_ID = "order_id";
    private static final String ORDER_ITEM_MENU_ID = "menu_id";
    private static final String ORDER_ITEM_QTY = "qty";
    private static final String ORDER_ITEM_PRICE = "price";
    private static final String ORDER_ITEM_SUBTOTAL = "subtotal";

    // Table Favorites
    private static final String TABLE_FAVORITES = "favorites";
    private static final String FAV_ID = "id";
    private static final String FAV_USER_ID = "user_id";
    private static final String FAV_MENU_ID = "menu_id";
    private static final String FAV_CREATED_AT = "created_at";

    // Table Reviews
    private static final String TABLE_REVIEWS = "reviews";
    private static final String REVIEW_ID = "id";
    private static final String REVIEW_USER_ID = "user_id";
    private static final String REVIEW_MENU_ID = "menu_id";
    private static final String REVIEW_ORDER_ID = "order_id";
    private static final String REVIEW_RATING = "rating"; // 1-5
    private static final String REVIEW_COMMENT = "comment";
    private static final String REVIEW_CREATED_AT = "created_at";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_EMAIL + " TEXT UNIQUE NOT NULL, "
                + USER_PASSWORD + " TEXT NOT NULL, "
                + USER_NAME + " TEXT NOT NULL, "
                + USER_ROLE + " TEXT NOT NULL, "
                + USER_PHONE + " TEXT, "
                + USER_NIM_NIP + " TEXT, "
                + USER_TYPE + " TEXT, "
                + USER_CREATED_AT + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Stand Table
        String createStandTable = "CREATE TABLE " + TABLE_STAND + " ("
                + STAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STAND_NAMA + " TEXT NOT NULL, "
                + STAND_DESKRIPSI + " TEXT, "
                + STAND_IMAGE + " TEXT, "
                + STAND_OWNER_ID + " INTEGER, "
                + STAND_CREATED_AT + " TEXT, "
                + "FOREIGN KEY(" + STAND_OWNER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "))";
        db.execSQL(createStandTable);

        // Create Menu Table
        String createMenuTable = "CREATE TABLE " + TABLE_MENU + " ("
                + MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MENU_STAND_ID + " INTEGER NOT NULL, "
                + MENU_NAMA + " TEXT NOT NULL, "
                + MENU_HARGA + " INTEGER NOT NULL, "
                + MENU_IMAGE + " TEXT, "
                + MENU_DESKRIPSI + " TEXT, "
                + MENU_KATEGORI + " TEXT, "
                + MENU_STATUS + " TEXT DEFAULT 'available', "
                + MENU_CREATED_AT + " TEXT, "
                + "FOREIGN KEY(" + MENU_STAND_ID + ") REFERENCES " + TABLE_STAND + "(" + STAND_ID + "))";
        db.execSQL(createMenuTable);

        // Create Cart Table
        String createCartTable = "CREATE TABLE " + TABLE_CART + " ("
                + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CART_USER_ID + " INTEGER NOT NULL, "
                + CART_MENU_ID + " INTEGER NOT NULL, "
                + CART_QTY + " INTEGER NOT NULL, "
                + CART_NOTES + " TEXT, "
                + "FOREIGN KEY(" + CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "), "
                + "FOREIGN KEY(" + CART_MENU_ID + ") REFERENCES " + TABLE_MENU + "(" + MENU_ID + "))";
        db.execSQL(createCartTable);

        // Create Orders Table
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " ("
                + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ORDER_USER_ID + " INTEGER NOT NULL, "
                + ORDER_STAND_ID + " INTEGER NOT NULL, "
                + ORDER_TOTAL + " INTEGER NOT NULL, "
                + ORDER_STATUS + " TEXT DEFAULT 'pending', "
                + ORDER_PAYMENT_METHOD + " TEXT, "
                + ORDER_PAYMENT_STATUS + " TEXT DEFAULT 'unpaid', "
                + ORDER_NOTES + " TEXT, "
                + ORDER_CREATED_AT + " TEXT, "
                + ORDER_UPDATED_AT + " TEXT, "
                + "FOREIGN KEY(" + ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "), "
                + "FOREIGN KEY(" + ORDER_STAND_ID + ") REFERENCES " + TABLE_STAND + "(" + STAND_ID + "))";
        db.execSQL(createOrdersTable);

        // Create Order Items Table
        String createOrderItemsTable = "CREATE TABLE " + TABLE_ORDER_ITEMS + " ("
                + ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ORDER_ITEM_ORDER_ID + " INTEGER NOT NULL, "
                + ORDER_ITEM_MENU_ID + " INTEGER NOT NULL, "
                + ORDER_ITEM_QTY + " INTEGER NOT NULL, "
                + ORDER_ITEM_PRICE + " INTEGER NOT NULL, "
                + ORDER_ITEM_SUBTOTAL + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + ORDER_ID + "), "
                + "FOREIGN KEY(" + ORDER_ITEM_MENU_ID + ") REFERENCES " + TABLE_MENU + "(" + MENU_ID + "))";
        db.execSQL(createOrderItemsTable);

        // Create Favorites Table
        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " ("
                + FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAV_USER_ID + " INTEGER NOT NULL, "
                + FAV_MENU_ID + " INTEGER NOT NULL, "
                + FAV_CREATED_AT + " TEXT, "
                + "FOREIGN KEY(" + FAV_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "), "
                + "FOREIGN KEY(" + FAV_MENU_ID + ") REFERENCES " + TABLE_MENU + "(" + MENU_ID + "), "
                + "UNIQUE(" + FAV_USER_ID + ", " + FAV_MENU_ID + "))";
        db.execSQL(createFavoritesTable);

        // Create Reviews Table
        String createReviewsTable = "CREATE TABLE " + TABLE_REVIEWS + " ("
                + REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + REVIEW_USER_ID + " INTEGER NOT NULL, "
                + REVIEW_MENU_ID + " INTEGER NOT NULL, "
                + REVIEW_ORDER_ID + " INTEGER, "
                + REVIEW_RATING + " INTEGER NOT NULL CHECK(" + REVIEW_RATING + " >= 1 AND " + REVIEW_RATING + " <= 5), "
                + REVIEW_COMMENT + " TEXT, "
                + REVIEW_CREATED_AT + " TEXT, "
                + "FOREIGN KEY(" + REVIEW_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "), "
                + "FOREIGN KEY(" + REVIEW_MENU_ID + ") REFERENCES " + TABLE_MENU + "(" + MENU_ID + "), "
                + "FOREIGN KEY(" + REVIEW_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + ORDER_ID + "))";
        db.execSQL(createReviewsTable);

        // Insert Initial Data
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        String timestamp = getCurrentTimestamp();

        // Insert Default Admin
        ContentValues admin = new ContentValues();
        admin.put(USER_EMAIL, "admin@kantinkampus.com");
        admin.put(USER_PASSWORD, "admin123"); // Dalam produksi, gunakan hashing!
        admin.put(USER_NAME, "Administrator");
        admin.put(USER_ROLE, "admin");
        admin.put(USER_PHONE, "081234567890");
        admin.put(USER_CREATED_AT, timestamp);
        db.insert(TABLE_USERS, null, admin);

        // Insert Sample Customer - Mahasiswa
        ContentValues customer1 = new ContentValues();
        customer1.put(USER_EMAIL, "mahasiswa@example.com");
        customer1.put(USER_PASSWORD, "customer123");
        customer1.put(USER_NAME, "Budi Santoso");
        customer1.put(USER_ROLE, "customer");
        customer1.put(USER_PHONE, "082345678901");
        customer1.put(USER_NIM_NIP, "123456789");
        customer1.put(USER_TYPE, "mahasiswa");
        customer1.put(USER_CREATED_AT, timestamp);
        db.insert(TABLE_USERS, null, customer1);

        // Insert Sample Customer - Dosen
        ContentValues customer2 = new ContentValues();
        customer2.put(USER_EMAIL, "dosen@example.com");
        customer2.put(USER_PASSWORD, "customer123");
        customer2.put(USER_NAME, "Dr. Siti Nurhaliza");
        customer2.put(USER_ROLE, "customer");
        customer2.put(USER_PHONE, "083456789012");
        customer2.put(USER_NIM_NIP, "198501012010012001");
        customer2.put(USER_TYPE, "dosen");
        customer2.put(USER_CREATED_AT, timestamp);
        db.insert(TABLE_USERS, null, customer2);

        // Insert Stands
        ContentValues stand1 = new ContentValues();
        stand1.put(STAND_NAMA, "Warung Nasi Bu Sari");
        stand1.put(STAND_DESKRIPSI, "Makanan Berat");
        stand1.put(STAND_OWNER_ID, 1); // Admin sebagai owner
        stand1.put(STAND_CREATED_AT, timestamp);
        long stand1Id = db.insert(TABLE_STAND, null, stand1);

        ContentValues stand2 = new ContentValues();
        stand2.put(STAND_NAMA, "Minuman Fresh Pak Danu");
        stand2.put(STAND_DESKRIPSI, "Minuman Segar");
        stand2.put(STAND_OWNER_ID, 1);
        stand2.put(STAND_CREATED_AT, timestamp);
        long stand2Id = db.insert(TABLE_STAND, null, stand2);

        ContentValues stand3 = new ContentValues();
        stand3.put(STAND_NAMA, "Snack Corner Mbak Lia");
        stand3.put(STAND_DESKRIPSI, "Cemilan & Snack");
        stand3.put(STAND_OWNER_ID, 1);
        stand3.put(STAND_CREATED_AT, timestamp);
        long stand3Id = db.insert(TABLE_STAND, null, stand3);

        // Insert Menu Stand 1 (Makanan Berat)
        insertMenuInitial(db, (int) stand1Id, "Nasi Ayam Geprek", 18000, "Nasi putih dengan ayam geprek pedas", "Makanan Berat", timestamp);
        insertMenuInitial(db, (int) stand1Id, "Nasi Telur Balado", 12000, "Nasi putih dengan telur balado", "Makanan Berat", timestamp);
        insertMenuInitial(db, (int) stand1Id, "Nasi Ayam Kremes", 17000, "Nasi putih dengan ayam kremes kriuk", "Makanan Berat", timestamp);
        insertMenuInitial(db, (int) stand1Id, "Nasi Ayam Bakar", 20000, "Nasi putih dengan ayam bakar bumbu kecap", "Makanan Berat", timestamp);
        insertMenuInitial(db, (int) stand1Id, "Nasi Lele Goreng", 16000, "Nasi putih dengan lele goreng crispy", "Makanan Berat", timestamp);

        // Insert Menu Stand 2 (Minuman)
        insertMenuInitial(db, (int) stand2Id, "Es Teh Manis", 5000, "Teh manis dingin segar", "Minuman", timestamp);
        insertMenuInitial(db, (int) stand2Id, "Es Jeruk Fresh", 7000, "Jeruk peras asli tanpa pengawet", "Minuman", timestamp);
        insertMenuInitial(db, (int) stand2Id, "Lemon Tea", 8000, "Teh dengan perasan lemon segar", "Minuman", timestamp);
        insertMenuInitial(db, (int) stand2Id, "Matcha Latte", 12000, "Minuman matcha creamy", "Minuman", timestamp);
        insertMenuInitial(db, (int) stand2Id, "Thai Tea", 10000, "Teh Thailand khas", "Minuman", timestamp);

        // Insert Menu Stand 3 (Snack)
        insertMenuInitial(db, (int) stand3Id, "Kentang Goreng", 10000, "French fries crispy", "Snack", timestamp);
        insertMenuInitial(db, (int) stand3Id, "Sosis Bakar", 12000, "Sosis bakar dengan saus", "Snack", timestamp);
        insertMenuInitial(db, (int) stand3Id, "Nugget Goreng", 13000, "Chicken nugget crispy", "Snack", timestamp);
        insertMenuInitial(db, (int) stand3Id, "Roti Bakar Coklat", 15000, "Roti bakar dengan topping coklat", "Snack", timestamp);
        insertMenuInitial(db, (int) stand3Id, "Pisang Coklat", 14000, "Pisang goreng dengan saus coklat", "Snack", timestamp);
    }

    private void insertMenuInitial(SQLiteDatabase db, int standId, String nama, int harga, String deskripsi, String kategori, String timestamp) {
        ContentValues values = new ContentValues();
        values.put(MENU_STAND_ID, standId);
        values.put(MENU_NAMA, nama);
        values.put(MENU_HARGA, harga);
        values.put(MENU_DESKRIPSI, deskripsi);
        values.put(MENU_KATEGORI, kategori);
        values.put(MENU_STATUS, "available");
        values.put(MENU_CREATED_AT, timestamp);
        db.insert(TABLE_MENU, null, values);
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // ======================== USER AUTHENTICATION METHODS ========================

    /**
     * Register new user
     */
    public long registerUser(String email, String password, String name, String role,
                             String phone, String nimNip, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if email already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + USER_EMAIL + " = ?",
                new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            return -1; // Email already exists
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, email);
        values.put(USER_PASSWORD, password); // Dalam produksi, gunakan hashing seperti BCrypt!
        values.put(USER_NAME, name);
        values.put(USER_ROLE, role);
        values.put(USER_PHONE, phone);
        values.put(USER_NIM_NIP, nimNip);
        values.put(USER_TYPE, type);
        values.put(USER_CREATED_AT, getCurrentTimestamp());

        return db.insert(TABLE_USERS, null, values);
    }

    /**
     * Login user
     */
    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + USER_EMAIL + " = ? AND " + USER_PASSWORD + " = ?",
                new String[]{email, password});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(USER_ROLE)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE)));
            user.setNimNip(cursor.getString(cursor.getColumnIndexOrThrow(USER_NIM_NIP)));
            user.setType(cursor.getString(cursor.getColumnIndexOrThrow(USER_TYPE)));
        }
        cursor.close();
        return user;
    }

    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + USER_ID + " = ?",
                new String[]{String.valueOf(userId)});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(USER_ROLE)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE)));
            user.setNimNip(cursor.getString(cursor.getColumnIndexOrThrow(USER_NIM_NIP)));
            user.setType(cursor.getString(cursor.getColumnIndexOrThrow(USER_TYPE)));
        }
        cursor.close();
        return user;
    }

    // ======================== STAND METHODS (ADMIN) ========================

    /**
     * Add new stand (Admin only)
     */
    public long addStand(String nama, String deskripsi, String image, int ownerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STAND_NAMA, nama);
        values.put(STAND_DESKRIPSI, deskripsi);
        values.put(STAND_IMAGE, image);
        values.put(STAND_OWNER_ID, ownerId);
        values.put(STAND_CREATED_AT, getCurrentTimestamp());
        return db.insert(TABLE_STAND, null, values);
    }

    /**
     * Update stand
     */
    public int updateStand(int standId, String nama, String deskripsi, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STAND_NAMA, nama);
        values.put(STAND_DESKRIPSI, deskripsi);
        if (image != null) {
            values.put(STAND_IMAGE, image);
        }
        return db.update(TABLE_STAND, values, STAND_ID + " = ?",
                new String[]{String.valueOf(standId)});
    }

    /**
     * Delete stand
     */
    public int deleteStand(int standId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Also delete related menus
        db.delete(TABLE_MENU, MENU_STAND_ID + " = ?", new String[]{String.valueOf(standId)});
        return db.delete(TABLE_STAND, STAND_ID + " = ?", new String[]{String.valueOf(standId)});
    }

    /**
     * Get all stands
     */
    public List<Stand> getAllStands() {
        List<Stand> stands = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STAND, null);

        if (cursor.moveToFirst()) {
            do {
                Stand stand = new Stand();
                stand.setId(cursor.getInt(cursor.getColumnIndexOrThrow(STAND_ID)));
                stand.setNama(cursor.getString(cursor.getColumnIndexOrThrow(STAND_NAMA)));
                stand.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(STAND_DESKRIPSI)));
                stand.setImage(cursor.getString(cursor.getColumnIndexOrThrow(STAND_IMAGE)));
                stand.setOwnerId(cursor.getInt(cursor.getColumnIndexOrThrow(STAND_OWNER_ID)));
                stands.add(stand);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stands;
    }

    // ======================== MENU METHODS ========================

    /**
     * Add new menu (Admin only)
     */
    public long addMenu(int standId, String nama, int harga, String image,
                        String deskripsi, String kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MENU_STAND_ID, standId);
        values.put(MENU_NAMA, nama);
        values.put(MENU_HARGA, harga);
        values.put(MENU_IMAGE, image);
        values.put(MENU_DESKRIPSI, deskripsi);
        values.put(MENU_KATEGORI, kategori);
        values.put(MENU_STATUS, "available");
        values.put(MENU_CREATED_AT, getCurrentTimestamp());
        return db.insert(TABLE_MENU, null, values);
    }

    /**
     * Update menu
     */
    public int updateMenu(int menuId, String nama, int harga, String image,
                          String deskripsi, String kategori, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MENU_NAMA, nama);
        values.put(MENU_HARGA, harga);
        if (image != null) {
            values.put(MENU_IMAGE, image);
        }
        values.put(MENU_DESKRIPSI, deskripsi);
        values.put(MENU_KATEGORI, kategori);
        values.put(MENU_STATUS, status);
        return db.update(TABLE_MENU, values, MENU_ID + " = ?",
                new String[]{String.valueOf(menuId)});
    }

    /**
     * Delete menu
     */
    public int deleteMenu(int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, MENU_ID + " = ?", new String[]{String.valueOf(menuId)});
    }

    /**
     * Get menu by stand ID with rating and favorite info
     */
    public List<Menu> getMenuByStandId(int standId, int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.*, " +
                "COALESCE(AVG(r.rating), 0) as avg_rating, " +
                "COUNT(DISTINCT r.id) as total_reviews, " +
                "CASE WHEN f.id IS NOT NULL THEN 1 ELSE 0 END as is_favorite " +
                "FROM " + TABLE_MENU + " m " +
                "LEFT JOIN " + TABLE_REVIEWS + " r ON m.id = r.menu_id " +
                "LEFT JOIN " + TABLE_FAVORITES + " f ON m.id = f.menu_id AND f.user_id = ? " +
                "WHERE m." + MENU_STAND_ID + " = ? " +
                "GROUP BY m.id";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(standId)});

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_STAND_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAMA)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_HARGA)));
                menu.setImage(cursor.getString(cursor.getColumnIndexOrThrow(MENU_IMAGE)));
                menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(MENU_DESKRIPSI)));
                menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(MENU_KATEGORI)));
                menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(MENU_STATUS)));
                menu.setAverageRating(cursor.getFloat(cursor.getColumnIndexOrThrow("avg_rating")));
                menu.setTotalReviews(cursor.getInt(cursor.getColumnIndexOrThrow("total_reviews")));
                menu.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite")) == 1);
                menus.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    /**
     * Search menu by keyword
     */
    public List<Menu> searchMenu(String keyword, int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.*, " +
                "COALESCE(AVG(r.rating), 0) as avg_rating, " +
                "COUNT(DISTINCT r.id) as total_reviews, " +
                "CASE WHEN f.id IS NOT NULL THEN 1 ELSE 0 END as is_favorite " +
                "FROM " + TABLE_MENU + " m " +
                "LEFT JOIN " + TABLE_REVIEWS + " r ON m.id = r.menu_id " +
                "LEFT JOIN " + TABLE_FAVORITES + " f ON m.id = f.menu_id AND f.user_id = ? " +
                "WHERE m." + MENU_NAMA + " LIKE ? OR m." + MENU_DESKRIPSI + " LIKE ? " +
                "GROUP BY m.id";

        String searchPattern = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), searchPattern, searchPattern});

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_STAND_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAMA)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_HARGA)));
                menu.setImage(cursor.getString(cursor.getColumnIndexOrThrow(MENU_IMAGE)));
                menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(MENU_DESKRIPSI)));
                menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(MENU_KATEGORI)));
                menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(MENU_STATUS)));
                menu.setAverageRating(cursor.getFloat(cursor.getColumnIndexOrThrow("avg_rating")));
                menu.setTotalReviews(cursor.getInt(cursor.getColumnIndexOrThrow("total_reviews")));
                menu.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite")) == 1);
                menus.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    public Menu getMenuById(int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + MENU_ID + " = ?",
                new String[]{String.valueOf(menuId)});

        Menu menu = null;
        if (cursor.moveToFirst()) {
            menu = new Menu();
            menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)));
            menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_STAND_ID)));
            menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAMA)));
            menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_HARGA)));
            menu.setImage(cursor.getString(cursor.getColumnIndexOrThrow(MENU_IMAGE)));
            menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(MENU_DESKRIPSI)));
            menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(MENU_KATEGORI)));
            menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(MENU_STATUS)));
        }
        cursor.close();
        return menu;
    }

    // ======================== CART METHODS (Updated for multi-user) ========================

    public void addToCart(int userId, int menuId, int qty, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART +
                        " WHERE " + CART_USER_ID + " = ? AND " + CART_MENU_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(menuId)});

        if (cursor.moveToFirst()) {
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(CART_QTY));
            ContentValues values = new ContentValues();
            values.put(CART_QTY, currentQty + qty);
            if (notes != null) {
                values.put(CART_NOTES, notes);
            }
            db.update(TABLE_CART, values, CART_USER_ID + " = ? AND " + CART_MENU_ID + " = ?",
                    new String[]{String.valueOf(userId), String.valueOf(menuId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(CART_USER_ID, userId);
            values.put(CART_MENU_ID, menuId);
            values.put(CART_QTY, qty);
            values.put(CART_NOTES, notes);
            db.insert(TABLE_CART, null, values);
        }
        cursor.close();
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + CART_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CART_ID)));
                item.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(CART_USER_ID)));
                int menuId = cursor.getInt(cursor.getColumnIndexOrThrow(CART_MENU_ID));
                item.setMenu(getMenuById(menuId));
                item.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(CART_QTY)));
                item.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(CART_NOTES)));
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public void updateCartQty(int cartId, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (qty <= 0) {
            db.delete(TABLE_CART, CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(CART_QTY, qty);
            db.update(TABLE_CART, values, CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        }
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, CART_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public int getCartCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + CART_QTY + ") FROM " + TABLE_CART +
                " WHERE " + CART_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // ======================== FAVORITES METHODS ========================

    public long addToFavorites(int userId, int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAV_USER_ID, userId);
        values.put(FAV_MENU_ID, menuId);
        values.put(FAV_CREATED_AT, getCurrentTimestamp());
        return db.insert(TABLE_FAVORITES, null, values);
    }

    public int removeFromFavorites(int userId, int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVORITES, FAV_USER_ID + " = ? AND " + FAV_MENU_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(menuId)});
    }

    public boolean isFavorite(int userId, int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES +
                        " WHERE " + FAV_USER_ID + " = ? AND " + FAV_MENU_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(menuId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Menu> getFavoriteMenus(int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.*, " +
                "COALESCE(AVG(r.rating), 0) as avg_rating, " +
                "COUNT(DISTINCT r.id) as total_reviews " +
                "FROM " + TABLE_FAVORITES + " f " +
                "INNER JOIN " + TABLE_MENU + " m ON f." + FAV_MENU_ID + " = m." + MENU_ID + " " +
                "LEFT JOIN " + TABLE_REVIEWS + " r ON m." + MENU_ID + " = r." + REVIEW_MENU_ID + " " +
                "WHERE f." + FAV_USER_ID + " = ? " +
                "GROUP BY m." + MENU_ID;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_ID)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_STAND_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAMA)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(MENU_HARGA)));
                menu.setImage(cursor.getString(cursor.getColumnIndexOrThrow(MENU_IMAGE)));
                menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(MENU_DESKRIPSI)));
                menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(MENU_KATEGORI)));
                menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(MENU_STATUS)));
                menu.setAverageRating(cursor.getFloat(cursor.getColumnIndexOrThrow("avg_rating")));
                menu.setTotalReviews(cursor.getInt(cursor.getColumnIndexOrThrow("total_reviews")));
                menu.setFavorite(true);
                menus.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    // ======================== REVIEW METHODS ========================

    public long addReview(int userId, int menuId, int orderId, int rating, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REVIEW_USER_ID, userId);
        values.put(REVIEW_MENU_ID, menuId);
        values.put(REVIEW_ORDER_ID, orderId);
        values.put(REVIEW_RATING, rating);
        values.put(REVIEW_COMMENT, comment);
        values.put(REVIEW_CREATED_AT, getCurrentTimestamp());
        return db.insert(TABLE_REVIEWS, null, values);
    }

    public List<Review> getMenuReviews(int menuId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.*, u." + USER_NAME + " " +
                "FROM " + TABLE_REVIEWS + " r " +
                "INNER JOIN " + TABLE_USERS + " u ON r." + REVIEW_USER_ID + " = u." + USER_ID + " " +
                "WHERE r." + REVIEW_MENU_ID + " = ? " +
                "ORDER BY r." + REVIEW_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(menuId)});

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review();
                review.setId(cursor.getInt(cursor.getColumnIndexOrThrow(REVIEW_ID)));
                review.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(REVIEW_USER_ID)));
                review.setMenuId(cursor.getInt(cursor.getColumnIndexOrThrow(REVIEW_MENU_ID)));
                review.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(REVIEW_RATING)));
                review.setComment(cursor.getString(cursor.getColumnIndexOrThrow(REVIEW_COMMENT)));
                review.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(REVIEW_CREATED_AT)));
                review.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    // ======================== ORDER METHODS ========================
// Add these methods to DBHelper.java

    /**
     * Create new order from cart
     */
    public long createOrderFromCart(int userId, int standId, String paymentMethod, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get cart items
        List<CartItem> cartItems = getCartItems(userId);
        if (cartItems.isEmpty()) {
            return -1;
        }

        // Calculate total
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        // Create order
        ContentValues orderValues = new ContentValues();
        orderValues.put(ORDER_USER_ID, userId);
        orderValues.put(ORDER_STAND_ID, standId);
        orderValues.put(ORDER_TOTAL, total);
        orderValues.put(ORDER_STATUS, "pending");
        orderValues.put(ORDER_PAYMENT_METHOD, paymentMethod);
        orderValues.put(ORDER_PAYMENT_STATUS, "unpaid");
        orderValues.put(ORDER_NOTES, notes);
        orderValues.put(ORDER_CREATED_AT, getCurrentTimestamp());
        orderValues.put(ORDER_UPDATED_AT, getCurrentTimestamp());

        long orderId = db.insert(TABLE_ORDERS, null, orderValues);

        if (orderId > 0) {
            // Insert order items
            for (CartItem item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(ORDER_ITEM_ORDER_ID, orderId);
                itemValues.put(ORDER_ITEM_MENU_ID, item.getMenu().getId());
                itemValues.put(ORDER_ITEM_QTY, item.getQty());
                itemValues.put(ORDER_ITEM_PRICE, item.getMenu().getHarga());
                itemValues.put(ORDER_ITEM_SUBTOTAL, item.getSubtotal());
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }

            // Clear cart
            clearCart(userId);
        }

        return orderId;
    }

    /**
     * Get orders by user
     */
    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, s." + STAND_NAMA + ", u." + USER_NAME + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "INNER JOIN " + TABLE_STAND + " s ON o." + ORDER_STAND_ID + " = s." + STAND_ID + " " +
                "INNER JOIN " + TABLE_USERS + " u ON o." + ORDER_USER_ID + " = u." + USER_ID + " " +
                "WHERE o." + ORDER_USER_ID + " = ? " +
                "ORDER BY o." + ORDER_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID)));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_USER_ID)));
                order.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_STAND_ID)));
                order.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_TOTAL)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_STATUS)));
                order.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_METHOD)));
                order.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_STATUS)));
                order.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_NOTES)));
                order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_CREATED_AT)));
                order.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_UPDATED_AT)));
                order.setStandName(cursor.getString(cursor.getColumnIndexOrThrow(STAND_NAMA)));
                order.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    /**
     * Get all orders (Admin)
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, s." + STAND_NAMA + ", u." + USER_NAME + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "INNER JOIN " + TABLE_STAND + " s ON o." + ORDER_STAND_ID + " = s." + STAND_ID + " " +
                "INNER JOIN " + TABLE_USERS + " u ON o." + ORDER_USER_ID + " = u." + USER_ID + " " +
                "ORDER BY o." + ORDER_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID)));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_USER_ID)));
                order.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_STAND_ID)));
                order.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_TOTAL)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_STATUS)));
                order.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_METHOD)));
                order.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_STATUS)));
                order.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_NOTES)));
                order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_CREATED_AT)));
                order.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_UPDATED_AT)));
                order.setStandName(cursor.getString(cursor.getColumnIndexOrThrow(STAND_NAMA)));
                order.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, s." + STAND_NAMA + ", u." + USER_NAME + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "INNER JOIN " + TABLE_STAND + " s ON o." + ORDER_STAND_ID + " = s." + STAND_ID + " " +
                "INNER JOIN " + TABLE_USERS + " u ON o." + ORDER_USER_ID + " = u." + USER_ID + " " +
                "WHERE o." + ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        Order order = null;
        if (cursor.moveToFirst()) {
            order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID)));
            order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_USER_ID)));
            order.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_STAND_ID)));
            order.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_TOTAL)));
            order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_STATUS)));
            order.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_METHOD)));
            order.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_STATUS)));
            order.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_NOTES)));
            order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_CREATED_AT)));
            order.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_UPDATED_AT)));
            order.setStandName(cursor.getString(cursor.getColumnIndexOrThrow(STAND_NAMA)));
            order.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
        }
        cursor.close();
        return order;
    }

    /**
     * Get order items by order ID
     */
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT oi.*, m." + MENU_NAMA + " " +
                "FROM " + TABLE_ORDER_ITEMS + " oi " +
                "INNER JOIN " + TABLE_MENU + " m ON oi." + ORDER_ITEM_MENU_ID + " = m." + MENU_ID + " " +
                "WHERE oi." + ORDER_ITEM_ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                OrderItem item = new OrderItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ITEM_ID)));
                item.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ITEM_ORDER_ID)));
                item.setMenuId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ITEM_MENU_ID)));
                item.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ITEM_QTY)));
                item.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ITEM_PRICE)));
                item.setSubtotal(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ITEM_SUBTOTAL)));
                item.setMenuName(cursor.getString(cursor.getColumnIndexOrThrow(MENU_NAMA)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    /**
     * Update order status (Admin)
     */
    public int updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_STATUS, status);
        values.put(ORDER_UPDATED_AT, getCurrentTimestamp());
        return db.update(TABLE_ORDERS, values, ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
    }

    /**
     * Update payment status
     */
    public int updatePaymentStatus(int orderId, String paymentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDER_PAYMENT_STATUS, paymentStatus);
        values.put(ORDER_UPDATED_AT, getCurrentTimestamp());
        return db.update(TABLE_ORDERS, values, ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
    }

    /**
     * Cancel order
     */
    public int cancelOrder(int orderId) {
        return updateOrderStatus(orderId, "cancelled");
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, s." + STAND_NAMA + ", u." + USER_NAME + " " +
                "FROM " + TABLE_ORDERS + " o " +
                "INNER JOIN " + TABLE_STAND + " s ON o." + ORDER_STAND_ID + " = s." + STAND_ID + " " +
                "INNER JOIN " + TABLE_USERS + " u ON o." + ORDER_USER_ID + " = u." + USER_ID + " " +
                "WHERE o." + ORDER_STATUS + " = ? " +
                "ORDER BY o." + ORDER_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{status});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_ID)));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_USER_ID)));
                order.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_STAND_ID)));
                order.setTotal(cursor.getInt(cursor.getColumnIndexOrThrow(ORDER_TOTAL)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_STATUS)));
                order.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_METHOD)));
                order.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_PAYMENT_STATUS)));
                order.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_NOTES)));
                order.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_CREATED_AT)));
                order.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(ORDER_UPDATED_AT)));
                order.setStandName(cursor.getString(cursor.getColumnIndexOrThrow(STAND_NAMA)));
                order.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

// ======================== STATISTICS METHODS (For Admin Dashboard) ========================

    /**
     * Get total orders count
     */
    public int getTotalOrdersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * Get total revenue
     */
    public int getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + ORDER_TOTAL + ") FROM " + TABLE_ORDERS +
                " WHERE " + ORDER_STATUS + " = 'completed'", null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    /**
     * Get total customers count
     */
    public int getTotalCustomersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS +
                " WHERE " + USER_ROLE + " = 'customer'", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * Get pending orders count
     */
    public int getPendingOrdersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS +
                " WHERE " + ORDER_STATUS + " = 'pending' OR " + ORDER_STATUS + " = 'processing'", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * Get all customers (Admin)
     */
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + USER_ROLE + " = 'customer' ORDER BY " + USER_CREATED_AT + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(USER_ROLE)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE)));
                user.setNimNip(cursor.getString(cursor.getColumnIndexOrThrow(USER_NIM_NIP)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(USER_TYPE)));
                user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(USER_CREATED_AT)));
                customers.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return customers;
    }
}