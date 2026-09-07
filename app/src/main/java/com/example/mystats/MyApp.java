package com.example.mystats;

import android.app.Application;
import com.example.mystats.data.database.AppDatabase;

public class MyApp extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}