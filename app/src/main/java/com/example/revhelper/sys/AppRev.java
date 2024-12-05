package com.example.revhelper.sys;

import android.app.Application;

import androidx.room.Room;


public class AppRev extends Application {

    private static AppRev instance;
    private static AppDatabase db;

    public static AppRev getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dbtrain")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .createFromAsset("database/train.db")
                .build();
    }

    public static AppDatabase getDb() {

        return db;
    }


}
