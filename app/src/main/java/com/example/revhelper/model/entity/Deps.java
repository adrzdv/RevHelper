package com.example.revhelper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "deps")
public class Deps {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "name")
    @NonNull
    private String name = "";
    @ColumnInfo(name = "branch")
    @NonNull
    private int branch = 0;


    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getBranch() {
        return branch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

