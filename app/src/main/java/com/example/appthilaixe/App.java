package com.example.appthilaixe;

import android.app.Application;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.util.DataSeeder;
import com.example.appthilaixe.util.DatabaseCleaner;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        // 🧹 Bước 1: Reset dữ liệu cũ (xoá tất cả trừ users)
        DatabaseCleaner.clearAllExceptUsers(getApplicationContext());

        // 🌱 Bước 2: Seed dữ liệu mới
        DataSeeder.seedAll(getApplicationContext(), db);
    }
}
