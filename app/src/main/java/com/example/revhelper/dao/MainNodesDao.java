package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.entity.MainNodes;

import java.util.List;

@Dao
public interface MainNodesDao {

    @Query("SELECT * FROM main_nodes")
    List<MainNodes> getMainNodesList();
}
