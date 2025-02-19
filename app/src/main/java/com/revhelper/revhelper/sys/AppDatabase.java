package com.revhelper.revhelper.sys;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.revhelper.revhelper.dao.AttributeDao;
import com.revhelper.revhelper.dao.CoachDao;
import com.revhelper.revhelper.dao.DepoDao;
import com.revhelper.revhelper.dao.TempParametersDao;
import com.revhelper.revhelper.dao.TrainDao;
import com.revhelper.revhelper.dao.ViolationDao;
import com.revhelper.revhelper.model.entity.Attribute;
import com.revhelper.revhelper.model.entity.Branches;
import com.revhelper.revhelper.model.entity.MainCoach;
import com.revhelper.revhelper.model.entity.Deps;
import com.revhelper.revhelper.model.entity.TempStatsParameter;
import com.revhelper.revhelper.model.entity.Train;
import com.revhelper.revhelper.model.entity.Violation;

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
