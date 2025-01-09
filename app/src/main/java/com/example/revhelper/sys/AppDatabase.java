package com.example.revhelper.sys;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.revhelper.dao.CoachDao;
import com.example.revhelper.dao.MainNodesDao;
import com.example.revhelper.dao.TrainDao;
import com.example.revhelper.dao.ViolationDao;
import com.example.revhelper.model.entity.Branches;
import com.example.revhelper.model.entity.Coach;
import com.example.revhelper.model.entity.Deps;
import com.example.revhelper.model.entity.MainNodes;
import com.example.revhelper.model.entity.Train;
import com.example.revhelper.model.entity.Violation;

@Database(entities = {Train.class, Coach.class, Branches.class, Deps.class,
        MainNodes.class, Violation.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainDao trainDao();

    public abstract CoachDao coachDao();

    public abstract MainNodesDao mainNodesDao();

    public abstract ViolationDao violationDao();

}
