package com.example.appthilaixe.util;

import android.content.Context;

import com.example.appthilaixe.database.AppDatabase;

/**
 * Tiện ích dọn toàn bộ dữ liệu trong DB (ngoại trừ bảng users).
 * Dùng khi cần reset dữ liệu seed.
 */
public class DatabaseCleaner {
    public static void clearDatabase(Context context) {
        try {
            // Tên DB phải trùng với tên trong AppDatabase
            context.deleteDatabase("appthilaixe.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
