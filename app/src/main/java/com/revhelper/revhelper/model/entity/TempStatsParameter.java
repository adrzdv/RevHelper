package com.revhelper.revhelper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_parameters")
public class TempStatsParameter {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    @NonNull
    private String name = "";
    @ColumnInfo(name = "active")
    @NonNull
    private Integer active;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public Integer getActive() {
        return active;
    }

    public void setActive(@NonNull Integer active) {
        this.active = active;
    }
}
