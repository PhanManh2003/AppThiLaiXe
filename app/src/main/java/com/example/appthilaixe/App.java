package com.example.appthilaixe;

import android.app.Application;

import androidx.room.Database;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.util.DataSeeder;
import com.example.appthilaixe.util.DatabaseCleaner;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Bước 1: Xoá db mỗi lần khởi động lại
        DatabaseCleaner.clearDatabase(getApplicationContext());
        // Bước 2: tạo db
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        // Bước 3: Seed dữ liệu mới
        DataSeeder.seedAll(getApplicationContext(), db);
    }
}
