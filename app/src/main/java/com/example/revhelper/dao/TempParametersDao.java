package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.entity.TempStatsParameter;

import java.util.List;

@Dao
public interface TempParametersDao {

    @Query("SELECT * FROM temp_parameters WHERE active = 1")
    List<TempStatsParameter> getAllTempParameters();
}
