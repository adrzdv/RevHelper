package com.example.revhelper.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "violations")
public class Violation implements Comparable<Violation> {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "code")
    @NonNull
    private Integer code;
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    @ColumnInfo(name = "revision_type")
    @NonNull
    private Integer revisionType;
    @ColumnInfo(name = "conflictive_code", defaultValue = "0")
    @NonNull
    private Integer conflictiveCode;
    @ColumnInfo(name = "active", defaultValue = "1")
    @NonNull
    private Integer active;

    public Violation(int id, int code, @NonNull String name, int revisionType, int conflictiveCode, int active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.conflictiveCode = conflictiveCode;
        this.active = active;
    }

    public Violation() {

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

    public int getConflictiveCode() {
        return conflictiveCode;
    }

    public void setConflictiveCode(int conflictiveCode) {
        this.conflictiveCode = conflictiveCode;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public int compareTo(Violation other) {
        return this.code.compareTo(other.code);
    }
}
