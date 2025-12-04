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
    private static final String DATABASE_NAME = "kantinkampus.db";
    private static final int DATABASE_VERSION = 1;

    // Table Stand
    private static final String TABLE_STAND = "table_stand";
    private static final String STAND_ID = "id";
    private static final String STAND_NAMA = "nama";
    private static final String STAND_DESKRIPSI = "deskripsi";

    // Table Menu
    private static final String TABLE_MENU = "table_menu";
    private static final String MENU_ID = "id";
    private static final String MENU_STAND_ID = "stand_id";
    private static final String MENU_NAMA = "nama";
    private static final String MENU_HARGA = "harga";

    // Table Cart
    private static final String TABLE_CART = "table_cart";
    private static final String CART_ID = "id";
    private static final String CART_MENU_ID = "menu_id";
    private static final String CART_QTY = "qty";

    // Table History
    private static final String TABLE_HISTORY = "table_history";
    private static final String HISTORY_ID = "id";
    private static final String HISTORY_ITEMS = "items";
    private static final String HISTORY_TOTAL = "total";
    private static final String HISTORY_TANGGAL = "tanggal";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Stand Table
        String createStandTable = "CREATE TABLE " + TABLE_STAND + " ("
                + STAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STAND_NAMA + " TEXT, "
                + STAND_DESKRIPSI + " TEXT)";
        db.execSQL(createStandTable);

        // Create Menu Table
        String createMenuTable = "CREATE TABLE " + TABLE_MENU + " ("
                + MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MENU_STAND_ID + " INTEGER, "
                + MENU_NAMA + " TEXT, "
                + MENU_HARGA + " INTEGER)";
        db.execSQL(createMenuTable);

        // Create Cart Table
        String createCartTable = "CREATE TABLE " + TABLE_CART + " ("
                + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CART_MENU_ID + " INTEGER, "
                + CART_QTY + " INTEGER)";
        db.execSQL(createCartTable);

        // Create History Table
        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " ("
                + HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HISTORY_ITEMS + " TEXT, "
                + HISTORY_TOTAL + " INTEGER, "
                + HISTORY_TANGGAL + " TEXT)";
        db.execSQL(createHistoryTable);

        // Insert Initial Data
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insert Stands
        ContentValues stand1 = new ContentValues();
        stand1.put(STAND_NAMA, "Warung Nasi Bu Sari");
        stand1.put(STAND_DESKRIPSI, "Makanan Berat");
        db.insert(TABLE_STAND, null, stand1);

        ContentValues stand2 = new ContentValues();
        stand2.put(STAND_NAMA, "Minuman Fresh Pak Danu");
        stand2.put(STAND_DESKRIPSI, "Minuman");
        db.insert(TABLE_STAND, null, stand2);

        ContentValues stand3 = new ContentValues();
        stand3.put(STAND_NAMA, "Snack Corner Mbak Lia");
        stand3.put(STAND_DESKRIPSI, "Snack");
        db.insert(TABLE_STAND, null, stand3);

        // Insert Menu Stand 1
        insertMenu(db, 1, "Nasi Ayam Geprek", 18000);
        insertMenu(db, 1, "Nasi Telur Balado", 12000);
        insertMenu(db, 1, "Nasi Ayam Kremes", 17000);
        insertMenu(db, 1, "Nasi Ayam Bakar", 20000);
        insertMenu(db, 1, "Nasi Lele Goreng", 16000);

        // Insert Menu Stand 2
        insertMenu(db, 2, "Es Teh Manis", 5000);
        insertMenu(db, 2, "Es Jeruk Fresh", 7000);
        insertMenu(db, 2, "Lemon Tea", 8000);
        insertMenu(db, 2, "Matcha Latte", 12000);
        insertMenu(db, 2, "Thai Tea", 10000);

        // Insert Menu Stand 3
        insertMenu(db, 3, "Kentang Goreng", 10000);
        insertMenu(db, 3, "Sosis Bakar", 12000);
        insertMenu(db, 3, "Nugget Goreng", 13000);
        insertMenu(db, 3, "Roti Bakar Coklat", 15000);
        insertMenu(db, 3, "Pisang Coklat", 14000);
    }

    private void insertMenu(SQLiteDatabase db, int standId, String nama, int harga) {
        ContentValues values = new ContentValues();
        values.put(MENU_STAND_ID, standId);
        values.put(MENU_NAMA, nama);
        values.put(MENU_HARGA, harga);
        db.insert(TABLE_MENU, null, values);
    }

    // Stand Methods
    public List<Stand> getAllStands() {
        List<Stand> stands = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STAND, null);

        if (cursor.moveToFirst()) {
            do {
                Stand stand = new Stand();
                stand.setId(cursor.getInt(0));
                stand.setNama(cursor.getString(1));
                stand.setDeskripsi(cursor.getString(2));
                stands.add(stand);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stands;
    }

    // Menu Methods
    public List<Menu> getMenuByStandId(int standId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + MENU_STAND_ID + " = ?",
                new String[]{String.valueOf(standId)});

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();
                menu.setId(cursor.getInt(0));
                menu.setStandId(cursor.getInt(1));
                menu.setNama(cursor.getString(2));
                menu.setHarga(cursor.getInt(3));
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
            menu.setId(cursor.getInt(0));
            menu.setStandId(cursor.getInt(1));
            menu.setNama(cursor.getString(2));
            menu.setHarga(cursor.getInt(3));
        }
        cursor.close();
        return menu;
    }

    // Cart Methods
    public void addToCart(int menuId, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if item already exists in cart
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + CART_MENU_ID + " = ?",
                new String[]{String.valueOf(menuId)});

        if (cursor.moveToFirst()) {
            int currentQty = cursor.getInt(2);
            ContentValues values = new ContentValues();
            values.put(CART_QTY, currentQty + qty);
            db.update(TABLE_CART, values, CART_MENU_ID + " = ?", new String[]{String.valueOf(menuId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(CART_MENU_ID, menuId);
            values.put(CART_QTY, qty);
            db.insert(TABLE_CART, null, values);
        }
        cursor.close();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem();
                item.setId(cursor.getInt(0));
                int menuId = cursor.getInt(1);
                item.setMenu(getMenuById(menuId));
                item.setQty(cursor.getInt(2));
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

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
    }

    public int getCartCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + CART_QTY + ") FROM " + TABLE_CART, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // History Methods
    public void addToHistory(String items, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HISTORY_ITEMS, items);
        values.put(HISTORY_TOTAL, total);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        values.put(HISTORY_TANGGAL, sdf.format(new Date()));

        db.insert(TABLE_HISTORY, null, values);
    }

    public List<History> getAllHistory() {
        List<History> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + HISTORY_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setId(cursor.getInt(0));
                history.setItems(cursor.getString(1));
                history.setTotal(cursor.getInt(2));
                history.setTanggal(cursor.getString(3));
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }
}