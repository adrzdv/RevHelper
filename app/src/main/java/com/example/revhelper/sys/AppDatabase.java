package com.example.revhelper.sys;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.revhelper.dao.AttributeDao;
import com.example.revhelper.dao.CoachDao;
import com.example.revhelper.dao.DepoDao;
import com.example.revhelper.dao.TempParametersDao;
import com.example.revhelper.dao.TrainDao;
import com.example.revhelper.dao.ViolationDao;
import com.example.revhelper.model.entity.Attribute;
import com.example.revhelper.model.entity.Branches;
import com.example.revhelper.model.entity.MainCoach;
import com.example.revhelper.model.entity.Deps;
import com.example.revhelper.model.entity.TempStatsParameter;
import com.example.revhelper.model.entity.Train;
import com.example.revhelper.model.entity.Violation;

/**
 * Application database class
 */

@Database(entities = {Train.class, MainCoach.class, Branches.class, Deps.class,
        TempStatsParameter.class, Violation.class, Attribute.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainDao trainDao();

    public abstract CoachDao coachDao();

    public abstract TempParametersDao templeParametersDao();

    public abstract ViolationDao violationDao();

    public abstract DepoDao depoDao();

    public abstract AttributeDao attributeDao();

}
