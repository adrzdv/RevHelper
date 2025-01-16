package com.example.revhelper.sys;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.example.revhelper.services.CheckService;


public class AppRev extends Application {

    private static AppRev instance;
    private static AppDatabase db;
    private static CheckService checker;

    public static AppRev getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate();
        instance = this;
        checker = new CheckService();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dbtrain")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .createFromAsset("database/train.db")
                .build();
    }

    public static AppDatabase getDb() {

        return db;
    }

    public static CheckService getChecker() {

        return checker;
    }


}
