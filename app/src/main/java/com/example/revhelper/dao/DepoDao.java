package com.example.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.revhelper.model.entity.Deps;

import java.util.List;

@Dao
public interface DepoDao {

    @Query("SELECT * FROM deps")
    List<Deps> getAlldeps();

    @Insert
    void insertDeps(List<Deps> deps);

    @Query("DELETE FROM deps")
    void cleanDepsTable();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'deps'")
    void cleanKeys();
}
