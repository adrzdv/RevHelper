package com.example.revhelper.model.violation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "violations")
public class Violation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "code")
    @NonNull
    private int code;
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    @ColumnInfo(name = "revision_type")
    @NonNull
    private int revisionType;

    public Violation(int id, int code, @NonNull String name, int revisionType) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Violation violation = (Violation) obj;
        return id == violation.id && name.equals(violation.name) && code == violation.code;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        return result;
    }

}
