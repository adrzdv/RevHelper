package com.example.revhelper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.revhelper.model.Coach;
import com.example.revhelper.model.Train;

@Database(entities = {Train.class, Coach.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainDao trainDao();

    public abstract CoachDao coachDao();

}
