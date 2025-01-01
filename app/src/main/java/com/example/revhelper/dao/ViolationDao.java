package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.Violation;

import java.util.List;

@Dao
public interface ViolationDao {

    @Query("SELECT * FROM violations")
    List<Violation> getAllViolations();
}
