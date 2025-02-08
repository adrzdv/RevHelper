package com.example.revhelper.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "violation_attribute")
public class Attribute {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    @ColumnInfo(name = "id_violation")
    @NonNull
    private int idViolation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdViolation() {
        return idViolation;
    }

    public void setIdViolation(int idViolation) {
        this.idViolation = idViolation;
    }
}
