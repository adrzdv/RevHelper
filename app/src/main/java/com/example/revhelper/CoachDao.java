package com.example.revhelper;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.Coach;

@Dao
public interface CoachDao {
    @Query("SELECT * FROM informators WHERE informators.coach_number LIKE :coach")
    Coach findByCoach(String coach);

}
