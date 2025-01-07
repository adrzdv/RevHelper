package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.revhelper.model.train.Train;
import com.example.revhelper.model.train.TrainDto;

import java.util.List;

@Dao
public interface TrainDao {

    @Query("SELECT * FROM trains " +
            "WHERE trains.number " +
            "LIKE :trainNumber LIKE :trainNumber")
    Train findByNumber(String trainNumber);

    @Query("SELECT trains.number, trains.route, trains.has_progressive," +
            "trains.has_registrator, trains.has_portal, deps.name AS dep_name, branches.name AS branch_name " +
            "FROM trains " +
            "LEFT JOIN deps ON trains.dep = deps.id " +
            "LEFT JOIN branches ON branches.id = deps.branch " +
            "WHERE trains.number LIKE :trainNumber")
    TrainDto findByNumberWithDep(String trainNumber);

    @Query("SELECT * FROM trains")
    List<Train> getAllTrains();

    @Insert
    void insertTrains(List<Train> trainList);

    @Query("DELETE FROM trains")
    void cleanTrainTable();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'trains'")
    void cleanKeys();

}