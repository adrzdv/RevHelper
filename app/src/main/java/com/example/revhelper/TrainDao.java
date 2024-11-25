package com.example.revhelper;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.Train;
import com.example.revhelper.model.TrainDto;

@Dao
public interface TrainDao {

//    @Query("SELECT * FROM trains LEFT JOIN deps ON trains.dep = deps.id " +
//            "LEFT JOIN branches ON deps.id = branches.id " +
//            "WHERE trains.number_straight " +
//            "LIKE :trainNumber OR trains.number_reversed LIKE :trainNumber")
//    Train findByNumberWithBranch(String trainNumber);
//
// Не канает, если хочешь ван ту мэни - делай мапу, где ключ - филиал а значение - куча структурок


    @Query("SELECT * FROM trains " +
            "WHERE trains.number_straight " +
            "LIKE :trainNumber OR trains.number_reversed LIKE :trainNumber")
    Train findByNumber(String trainNumber);

    @Query("SELECT trains.number_straight, trains.number_reversed, trains.route, trains.has_progressive," +
            "trains.has_registrator, deps.name AS dep_name, branches.name AS branch_name " +
            "FROM trains " +
            "LEFT JOIN deps ON trains.dep = deps.id " +
            "LEFT JOIN branches ON branches.id = deps.branch " +
            "WHERE trains.number_straight LIKE :trainNumber OR trains.number_reversed LIKE :trainNumber")
    TrainDto findByNumberWithDep(String trainNumber);

}