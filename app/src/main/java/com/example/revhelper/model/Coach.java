package com.example.revhelper.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "informators")
public class Coach implements Serializable {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "coach_number")
    @NonNull
    private String coachNumber = "";
    @ColumnInfo(name = "id_branch")
    @NonNull
    private int dep = 0;

    public void setCoachNumber(@NonNull String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    @NonNull
    public String getCoachNumber() {
        return coachNumber;
    }

    public int getDep() {
        return dep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
