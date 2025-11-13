package com.example.appthilaixe;

import android.app.Application;

import com.example.appthilaixe.database.AppDatabase;
import com.example.appthilaixe.util.DataSeeder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        DataSeeder.seedAll(getApplicationContext(), db);
    }
}
