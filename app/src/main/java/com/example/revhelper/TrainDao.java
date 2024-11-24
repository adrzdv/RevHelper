package com.example.revhelper;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.Train;

@Dao
public interface TrainDao {

    @Query("SELECT * FROM trains WHERE trains.number_straight " +
            "LIKE :trainNumber OR trains.number_reversed LIKE :trainNumber")
    Train findByNumber(String trainNumber);


}