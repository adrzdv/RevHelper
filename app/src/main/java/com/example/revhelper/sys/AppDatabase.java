package com.example.revhelper.sys;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.revhelper.dao.CoachDao;
import com.example.revhelper.dao.MainNodesDao;
import com.example.revhelper.dao.TrainDao;
import com.example.revhelper.dao.ViolationDao;
import com.example.revhelper.model.Branches;
import com.example.revhelper.model.coach.Coach;
import com.example.revhelper.model.Deps;
import com.example.revhelper.model.MainNodes;
import com.example.revhelper.model.train.Train;
import com.example.revhelper.model.violation.Violation;

@Database(entities = {Train.class, Coach.class, Branches.class, Deps.class,
        MainNodes.class, Violation.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainDao trainDao();

    public abstract CoachDao coachDao();

    public abstract MainNodesDao mainNodesDao();

    public abstract ViolationDao violationDao();

}
