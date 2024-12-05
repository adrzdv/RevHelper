package com.example.revhelper.sys;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.revhelper.dao.CoachDao;
import com.example.revhelper.dao.TrainDao;
import com.example.revhelper.model.Branches;
import com.example.revhelper.model.Coach;
import com.example.revhelper.model.Deps;
import com.example.revhelper.model.Train;

@Database(entities = {Train.class, Coach.class, Branches.class, Deps.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainDao trainDao();

    public abstract CoachDao coachDao();

}
