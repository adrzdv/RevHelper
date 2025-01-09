package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.entity.Violation;

import java.util.List;

@Dao
public interface ViolationDao {

    @Query("SELECT * FROM violations")
    List<Violation> getAllViolations();

    @Query("SELECT * FROM violations WHERE revision_type= :revisionType")
    List<Violation> getViolationsByRevisionType(int revisionType);
}
