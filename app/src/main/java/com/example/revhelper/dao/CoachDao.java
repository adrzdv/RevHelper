package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.revhelper.model.entity.Coach;

import java.util.List;

@Dao
public interface CoachDao {
    @Query("SELECT * FROM informators WHERE informators.coach_number LIKE :coach")
    Coach findByCoach(String coach);

    @Insert
    void insertCoaches(List<Coach> coaches);

    @Query("DELETE FROM informators")
    void cleanCoachTable();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'informators'")
    void cleanKeys();

}
