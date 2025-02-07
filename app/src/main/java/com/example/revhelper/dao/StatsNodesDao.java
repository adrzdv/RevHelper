package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.revhelper.model.entity.StatsNode;

import java.util.List;

@Dao
public interface StatsNodesDao {

    @Query("SELECT * FROM stats_nodes")
    List<StatsNode> getMainNodesList();
}
