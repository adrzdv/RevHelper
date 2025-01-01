package com.example.revhelper.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "violations")
public class Violation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    @ColumnInfo(name = "revision_type")
    @NonNull
    private int revisionType;

    public Violation(int id, @NonNull String name, int revisionType) {
        this.id = id;
        this.name = name;
        this.revisionType = revisionType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setRevisionType(int revisionType) {
        this.revisionType = revisionType;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getRevisionType() {
        return this.revisionType;
    }

}
