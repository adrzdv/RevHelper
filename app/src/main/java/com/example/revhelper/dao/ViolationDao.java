package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.revhelper.model.entity.Train;
import com.example.revhelper.model.entity.Violation;

import java.util.List;

@Dao
public interface ViolationDao {

    @Query("SELECT * FROM violations WHERE active = 1")
    List<Violation> getAllViolations();

    @Insert
    void insertViolations(List<Violation> violations);

    @Query("DELETE FROM violations")
    void cleanViolationTable();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'violations'")
    void cleanKeys();
}
