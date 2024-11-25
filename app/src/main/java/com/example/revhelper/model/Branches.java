package com.example.revhelper.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "branches")
public class Branches {
    @PrimaryKey
    int id;
    @ColumnInfo(name = "name")
    @NonNull
    String name = "";

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
