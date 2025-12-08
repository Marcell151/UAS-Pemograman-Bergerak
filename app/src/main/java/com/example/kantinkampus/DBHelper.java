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
        // 1. Create Tables
        db.execSQL("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_ROLE + " TEXT,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_NIM_NIP + " TEXT,"
                + COLUMN_USER_TYPE + " TEXT,"
                + COLUMN_USER_BUSINESS_LICENSE + " TEXT,"
                + COLUMN_USER_STAND_ID + " INTEGER,"
                + COLUMN_USER_CREATED_AT + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_STAND + "("
                + COLUMN_STAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STAND_NAME + " TEXT,"
                + COLUMN_STAND_DESC + " TEXT,"
                + COLUMN_STAND_IMAGE + " TEXT,"
                + COLUMN_STAND_OWNER_ID + " INTEGER,"
                + COLUMN_STAND_OVO + " TEXT,"
                + COLUMN_STAND_GOPAY + " TEXT,"
                + COLUMN_STAND_CREATED_AT + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_MENU + "("
                + COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MENU_STAND_ID + " INTEGER,"
                + COLUMN_MENU_NAME + " TEXT,"
                + COLUMN_MENU_PRICE + " INTEGER,"
                + COLUMN_MENU_IMAGE + " TEXT,"
                + COLUMN_MENU_DESC + " TEXT,"
                + COLUMN_MENU_CATEGORY + " TEXT,"
                + COLUMN_MENU_STATUS + " TEXT DEFAULT 'available')");

        db.execSQL("CREATE TABLE " + TABLE_CART + "("
                + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CART_USER_ID + " INTEGER,"
                + COLUMN_CART_MENU_ID + " INTEGER,"
                + COLUMN_CART_QTY + " INTEGER,"
                + COLUMN_CART_NOTES + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_USER_ID + " INTEGER,"
                + COLUMN_ORDER_STAND_ID + " INTEGER,"
                + COLUMN_ORDER_TOTAL + " INTEGER,"
                + COLUMN_ORDER_STATUS + " TEXT DEFAULT 'pending_payment',"
                + COLUMN_ORDER_PAYMENT_METHOD + " TEXT,"
                + COLUMN_ORDER_PAYMENT_STATUS + " TEXT DEFAULT 'unpaid',"
                + COLUMN_ORDER_PAYMENT_PROOF + " TEXT,"
                + COLUMN_ORDER_NOTES + " TEXT,"
                + COLUMN_ORDER_DATE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEMS + "("
                + COLUMN_ORDERITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDERITEM_ORDER_ID + " INTEGER,"
                + COLUMN_ORDERITEM_MENU_ID + " INTEGER,"
                + COLUMN_ORDERITEM_QTY + " INTEGER,"
                + COLUMN_ORDERITEM_PRICE + " INTEGER,"
                + COLUMN_ORDERITEM_SUBTOTAL + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FAV_USER_ID + " INTEGER,"
                + COLUMN_FAV_MENU_ID + " INTEGER,"
                + "UNIQUE(" + COLUMN_FAV_USER_ID + ", " + COLUMN_FAV_MENU_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_REVIEWS + "("
                + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REVIEW_USER_ID + " INTEGER,"
                + COLUMN_REVIEW_MENU_ID + " INTEGER,"
                + COLUMN_REVIEW_ORDER_ID + " INTEGER,"
                + COLUMN_REVIEW_RATING + " INTEGER,"
                + COLUMN_REVIEW_COMMENT + " TEXT,"
                + COLUMN_REVIEW_DATE + " TEXT)");

        // 2. INSERT DEMO DATA (AKUN OTOMATIS)
        insertDemoData(db);
    }

    // ==========================================
    // METHOD UNTUK INPUT AKUN DEMO
    // ==========================================
    private void insertDemoData(SQLiteDatabase db) {
        String date = getDateTime();

        // --- 1. Penjual 01 ---
        ContentValues seller1 = new ContentValues();
        seller1.put(COLUMN_USER_EMAIL, "penjual01@gmail.com");
        seller1.put(COLUMN_USER_PASSWORD, "user123");
        seller1.put(COLUMN_USER_NAME, "penjual01");
        seller1.put(COLUMN_USER_ROLE, "seller");
        seller1.put(COLUMN_USER_PHONE, "08123456701");
        seller1.put(COLUMN_USER_BUSINESS_LICENSE, "LICENSE01");
        seller1.put(COLUMN_USER_CREATED_AT, date);
        long seller1Id = db.insert(TABLE_USERS, null, seller1);

        // Buat Stand untuk Penjual 01
        ContentValues stand1 = new ContentValues();
        stand1.put(COLUMN_STAND_NAME, "Warung Penjual 01");
        stand1.put(COLUMN_STAND_DESC, "Menyediakan aneka makanan berat enak");
        stand1.put(COLUMN_STAND_OWNER_ID, seller1Id);
        stand1.put(COLUMN_STAND_OVO, "08123456701");
        stand1.put(COLUMN_STAND_GOPAY, "08123456701");
        stand1.put(COLUMN_STAND_CREATED_AT, date);
        long stand1Id = db.insert(TABLE_STAND, null, stand1);

        // Update User Penjual 01 dengan Stand ID
        ContentValues updateSeller1 = new ContentValues();
        updateSeller1.put(COLUMN_USER_STAND_ID, stand1Id);
        db.update(TABLE_USERS, updateSeller1, COLUMN_USER_ID + "=?", new String[]{String.valueOf(seller1Id)});

        // --- 2. Penjual 02 ---
        ContentValues seller2 = new ContentValues();
        seller2.put(COLUMN_USER_EMAIL, "penjual02@gmail.com");
        seller2.put(COLUMN_USER_PASSWORD, "user123");
        seller2.put(COLUMN_USER_NAME, "penjual02");
        seller2.put(COLUMN_USER_ROLE, "seller");
        seller2.put(COLUMN_USER_PHONE, "08123456702");
        seller2.put(COLUMN_USER_BUSINESS_LICENSE, "LICENSE02");
        seller2.put(COLUMN_USER_CREATED_AT, date);
        long seller2Id = db.insert(TABLE_USERS, null, seller2);

        // Buat Stand untuk Penjual 02
        ContentValues stand2 = new ContentValues();
        stand2.put(COLUMN_STAND_NAME, "Warung Penjual 02");
        stand2.put(COLUMN_STAND_DESC, "Spesialis minuman segar dan snack");
        stand2.put(COLUMN_STAND_OWNER_ID, seller2Id);
        stand2.put(COLUMN_STAND_OVO, "08123456702");
        stand2.put(COLUMN_STAND_GOPAY, "08123456702");
        stand2.put(COLUMN_STAND_CREATED_AT, date);
        long stand2Id = db.insert(TABLE_STAND, null, stand2);

        // Update User Penjual 02 dengan Stand ID
        ContentValues updateSeller2 = new ContentValues();
        updateSeller2.put(COLUMN_USER_STAND_ID, stand2Id);
        db.update(TABLE_USERS, updateSeller2, COLUMN_USER_ID + "=?", new String[]{String.valueOf(seller2Id)});

        // --- 3. Pembeli 01 (Mahasiswa) ---
        ContentValues buyer1 = new ContentValues();
        buyer1.put(COLUMN_USER_EMAIL, "pembeli01@gmail.com");
        buyer1.put(COLUMN_USER_PASSWORD, "user123");
        buyer1.put(COLUMN_USER_NAME, "pembeli01");
        buyer1.put(COLUMN_USER_ROLE, "buyer");
        buyer1.put(COLUMN_USER_PHONE, "08123456703");
        buyer1.put(COLUMN_USER_NIM_NIP, "20240001"); // NIM
        buyer1.put(COLUMN_USER_TYPE, "mahasiswa");
        buyer1.put(COLUMN_USER_CREATED_AT, date);
        db.insert(TABLE_USERS, null, buyer1);

        // --- 4. Pembeli 02 (Dosen) ---
        ContentValues buyer2 = new ContentValues();
        buyer2.put(COLUMN_USER_EMAIL, "pembeli02@gmail.com");
        buyer2.put(COLUMN_USER_PASSWORD, "user123");
        buyer2.put(COLUMN_USER_NAME, "pembeli02");
        buyer2.put(COLUMN_USER_ROLE, "buyer");
        buyer2.put(COLUMN_USER_PHONE, "08123456704");
        buyer2.put(COLUMN_USER_NIM_NIP, "19800101202401"); // NIP
        buyer2.put(COLUMN_USER_TYPE, "dosen");
        buyer2.put(COLUMN_USER_CREATED_AT, date);
        db.insert(TABLE_USERS, null, buyer2);

        Log.d("DBHelper", "Demo accounts inserted!");
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
    // 3. USER MANAGEMENT (AUTH)
    // ==========================================

    public long registerUser(String email, String password, String name, String role,
                             String phone, String nimNip, String type, String businessLicense) {
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

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));

            String role = user.getRole();
            if (role.equals("buyer")) {
                user.setNimNip(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NIM_NIP)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_TYPE)));
            } else if (role.equals("seller")) {
                user.setBusinessLicenseNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_BUSINESS_LICENSE)));
                if (!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_USER_STAND_ID))) {
                    user.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_STAND_ID)));
                }
            }
            cursor.close();
            return user;
        }
        return null;
    }

    // [BARU] Ambil data user berdasarkan ID (Tanpa Password)
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)));

            String role = user.getRole();
            if (role.equals("buyer")) {
                user.setNimNip(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NIM_NIP)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_TYPE)));
            } else if (role.equals("seller")) {
                user.setBusinessLicenseNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_BUSINESS_LICENSE)));
                if (!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_USER_STAND_ID))) {
                    user.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_STAND_ID)));
                }
            }
            cursor.close();
            return user;
        }
        return null;
    }


    // ==========================================
    // 4. STAND MANAGEMENT (SELLER)
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
        db.close();
        return standId;
    }

    public Stand getStandByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STAND, null, COLUMN_STAND_OWNER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

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
        String selectQuery = "SELECT * FROM " + TABLE_STAND + " ORDER BY " + COLUMN_STAND_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        return db.update(TABLE_STAND, values, COLUMN_STAND_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // ==========================================
    // 5. MENU MANAGEMENT
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

        long id = db.insert(TABLE_MENU, null, values);
        db.close();
        return id;
    }

    public List<Menu> getMenusByStand(int standId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_MENU +
                " WHERE " + COLUMN_MENU_STAND_ID + " = " + standId +
                " ORDER BY " + COLUMN_MENU_CATEGORY;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_STAND_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
                menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_DESC)));
                menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_CATEGORY)));
                menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_STATUS)));
                menus.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    public Menu getMenuById(int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MENU, null, COLUMN_MENU_ID + "=?",
                new String[]{String.valueOf(menuId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Menu menu = new Menu();
            menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID)));
            menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_STAND_ID)));
            menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
            menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
            menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_DESC)));
            menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_CATEGORY)));
            menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_STATUS)));
            cursor.close();
            return menu;
        }
        return null;
    }

    public List<Menu> getMenuByStandId(int standId, int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.*, f." + COLUMN_FAV_ID + " as fav_id " +
                " FROM " + TABLE_MENU + " m " +
                " LEFT JOIN " + TABLE_FAVORITES + " f ON m." + COLUMN_MENU_ID + " = f." + COLUMN_FAV_MENU_ID +
                " AND f." + COLUMN_FAV_USER_ID + " = ?" +
                " WHERE m." + COLUMN_MENU_STAND_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(standId)});

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_STAND_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
                menu.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_DESC)));
                menu.setKategori(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_CATEGORY)));
                menu.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_STATUS)));

                boolean isFav = !cursor.isNull(cursor.getColumnIndexOrThrow("fav_id"));
                menu.setFavorite(isFav);

                menus.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    public int deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COLUMN_MENU_ID + " = ?", new String[]{String.valueOf(id)});
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

    // ==========================================
    // 6. CART MANAGEMENT
    // ==========================================

    public long addToCart(int userId, int menuId, int qty, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART +
                        " WHERE " + COLUMN_CART_USER_ID + "=? AND " + COLUMN_CART_MENU_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(menuId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QTY));
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));

            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QTY, currentQty + qty);
            db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
            cursor.close();
            db.close();
            return cartId;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_USER_ID, userId);
            values.put(COLUMN_CART_MENU_ID, menuId);
            values.put(COLUMN_CART_QTY, qty);
            values.put(COLUMN_CART_NOTES, notes);

            long id = db.insert(TABLE_CART, null, values);
            cursor.close();
            db.close();
            return id;
        }
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, m." + COLUMN_MENU_NAME + ", m." + COLUMN_MENU_PRICE +
                ", m." + COLUMN_MENU_IMAGE + ", m." + COLUMN_MENU_STAND_ID +
                " FROM " + TABLE_CART + " c " +
                " JOIN " + TABLE_MENU + " m ON c." + COLUMN_CART_MENU_ID + " = m." + COLUMN_MENU_ID +
                " WHERE c." + COLUMN_CART_USER_ID + " = ?";

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
                item.setMenu(menu);

                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void updateCartQty(int cartId, int newQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (newQty <= 0) {
            db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QTY, newQty);
            db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        }
        db.close();
    }

    public int getCartCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USER_ID + " = " + userId;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // ==========================================
    // 7. ORDER MANAGEMENT
    // ==========================================

    public void createOrderFromCart(int userId, List<CartItem> cartItems, String paymentMethod, String paymentProofPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        String dateTime = getDateTime();

        List<Integer> standIds = new ArrayList<>();
        for (CartItem item : cartItems) {
            int sid = item.getMenu().getStandId();
            if (!standIds.contains(sid)) {
                standIds.add(sid);
            }
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
        db.close();
    }

    public List<Order> getOrdersByStand(int standId, String statusFilter) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o.*, u." + COLUMN_USER_NAME + " as user_name FROM " + TABLE_ORDERS + " o " +
                " JOIN " + TABLE_USERS + " u ON o." + COLUMN_ORDER_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE o." + COLUMN_ORDER_STAND_ID + " = ?";

        if (!statusFilter.equals("all")) {
            query += " AND o." + COLUMN_ORDER_STATUS + " = '" + statusFilter + "'";
        }

        query += " ORDER BY o." + COLUMN_ORDER_ID + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(standId)});

        if (cursor.moveToFirst()) {
            do {
                Order order = cursorToOrder(cursor);
                orders.add(order);
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
                Order order = cursorToOrder(cursor);
                orders.add(order);
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
        return order;
    }

    public int updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, status);

        if (status.equals("paid") || status.equals("cooking")) {
            values.put(COLUMN_ORDER_PAYMENT_STATUS, "verified");
        }

        return db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    // ==========================================
    // 8. REVIEWS & FAVORITES
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
        return db.delete(TABLE_FAVORITES,
                COLUMN_FAV_USER_ID + "=? AND " + COLUMN_FAV_MENU_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(menuId)});
    }

    public boolean isFavorite(int userId, int menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, null,
                COLUMN_FAV_USER_ID + "=? AND " + COLUMN_FAV_MENU_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(menuId)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<Menu> getFavoriteMenus(int userId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT m.* FROM " + TABLE_MENU + " m " +
                " JOIN " + TABLE_FAVORITES + " f ON m." + COLUMN_MENU_ID + " = f." + COLUMN_FAV_MENU_ID +
                " WHERE f." + COLUMN_FAV_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_ID)));
                menu.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_NAME)));
                menu.setHarga(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_PRICE)));
                menu.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MENU_IMAGE)));
                menu.setStandId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MENU_STAND_ID)));
                menu.setFavorite(true);
                menus.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menus;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}