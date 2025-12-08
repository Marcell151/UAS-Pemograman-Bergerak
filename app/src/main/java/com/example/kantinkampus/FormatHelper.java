package com.example.kantinkampus;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormatHelper {

    // 1. Format Rupiah (Contoh: 15000 -> Rp 15.000)
    public static String formatRupiah(int number) {
        Locale localeID = new Locale("id", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        // Menghapus ",00" di belakang agar lebih bersih
        String result = formatRupiah.format(number);
        if (result.endsWith(",00")) {
            result = result.substring(0, result.length() - 3);
        }
        return result;
    }

    // 2. Format Tanggal Cantik (Relative Time)
    // Contoh: "Baru saja", "5 menit yang lalu", "Kemarin", "12 Des 2024"
    public static String formatWaktu(String dbDateString) {
        if (dbDateString == null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dbDateString);
            long now = System.currentTimeMillis();
            long time = date.getTime();
            long diff = now - time;

            if (diff < TimeUnit.MINUTES.toMillis(1)) {
                return "Baru saja";
            } else if (diff < TimeUnit.HOURS.toMillis(1)) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                return minutes + " menit yang lalu";
            } else if (diff < TimeUnit.DAYS.toMillis(1)) {
                long hours = TimeUnit.MILLISECONDS.toHours(diff);
                return hours + " jam yang lalu";
            } else if (diff < TimeUnit.DAYS.toMillis(2)) {
                return "Kemarin";
            } else {
                // Jika lebih dari 2 hari, tampilkan tanggal biasa yang rapi
                SimpleDateFormat prettyFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("id", "ID"));
                return prettyFormat.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return dbDateString; // Jika error, kembalikan format asli
        }
    }

    // 3. Helper Resize Gambar (Agar tidak Error/Abu-abu karena memori penuh)
    // Method ini akan memastikan gambar yang dimuat tidak lebih besar dari 1024x1024 piksel.
    public static android.graphics.Bitmap getResizedBitmap(android.content.Context context, android.net.Uri uri) {
        try {
            // A. Cek ukuran asli gambar tanpa memuatnya ke memori (inJustDecodeBounds = true)
            java.io.InputStream input = context.getContentResolver().openInputStream(uri);
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            android.graphics.BitmapFactory.decodeStream(input, null, options);
            if (input != null) input.close();

            // B. Hitung faktor pengecilan (Scale)
            // Target kita: dimensi terpanjang maks 1024px (aman untuk kebanyakan HP)
            int scale = 1;
            while (options.outWidth / scale > 1024 || options.outHeight / scale > 1024) {
                scale *= 2;
            }

            // C. Muat gambar sesungguhnya dengan skala yang sudah dihitung
            android.graphics.BitmapFactory.Options options2 = new android.graphics.BitmapFactory.Options();
            options2.inSampleSize = scale;
            java.io.InputStream input2 = context.getContentResolver().openInputStream(uri);
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(input2, null, options2);
            if (input2 != null) input2.close();

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Kembalikan null jika gagal total
        }
    }

    // 4. METHOD BARU: Copy Gambar dari Galeri ke Penyimpanan Aplikasi
    // Ini solusi agar gambar tidak abu-abu karena masalah izin.
    public static String copyImageToInternalStorage(android.content.Context context, android.net.Uri uri) {
        String savedImagePath = null;
        String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        try {
            // 1. Buka Stream dari Galeri
            java.io.InputStream inputStream = context.getContentResolver().openInputStream(uri);

            // 2. Siapkan File Tujuan di dalam folder aplikasi
            java.io.File storageDir = context.getFilesDir(); // Internal Storage
            java.io.File imageFile = new java.io.File(storageDir, imageFileName);

            // 3. Salin Data
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // 4. Tutup Stream
            outputStream.close();
            inputStream.close();

            // 5. Kembalikan Alamat File Lokal (Path Absolut)
            savedImagePath = imageFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedImagePath; // Contoh: /data/user/0/com.example/files/IMG_123.jpg
    }

}