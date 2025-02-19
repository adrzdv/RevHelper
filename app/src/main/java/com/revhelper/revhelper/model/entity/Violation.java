package com.revhelper.revhelper.model.entity;

import androidx.annotation.NonNull;
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
    @ColumnInfo(name = "in_transit", defaultValue = "0")
    @NonNull
    private Integer inTransit;
    @ColumnInfo(name = "at_start_point", defaultValue = "0")
    @NonNull
    private Integer atStartPoint;
    @ColumnInfo(name = "at_turnround_point", defaultValue = "0")
    @NonNull
    private Integer atTurnroundPoint;
    @ColumnInfo(name = "conflictive_code", defaultValue = "0")
    @NonNull
    private Integer conflictiveCode;
    @ColumnInfo(name = "at_ticket_office", defaultValue = "0")
    @NonNull
    private Integer atTicketOffice;
    @ColumnInfo(name = "active", defaultValue = "1")
    @NonNull
    private Integer active;

    public Violation(int id, int code, @NonNull String name, int inTransit, int atStartPoint,
                     int atTurnroundPoint, int conflictiveCode, int atTicketOffice, int active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.inTransit = inTransit;
        this.atStartPoint = atStartPoint;
        this.atTurnroundPoint = atTurnroundPoint;
        this.atTicketOffice = atTicketOffice;
        this.conflictiveCode = conflictiveCode;
        this.active = active;
    }

    public Violation() {

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

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getConflictiveCode() {
        return conflictiveCode;
    }

    public void setConflictiveCode(Integer conflictiveCode) {
        this.conflictiveCode = conflictiveCode;
    }

    public int getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public int compareTo(Violation other) {
        return this.code.compareTo(other.code);
    }

    public void setCode(@NonNull Integer code) {
        this.code = code;
    }

    @NonNull
    public Integer getInTransit() {
        return inTransit;
    }

    public void setInTransit(@NonNull Integer inTransit) {
        this.inTransit = inTransit;
    }

    @NonNull
    public Integer getAtStartPoint() {
        return atStartPoint;
    }

    public void setAtStartPoint(@NonNull Integer atStartPoint) {
        this.atStartPoint = atStartPoint;
    }

    @NonNull
    public Integer getAtTurnroundPoint() {
        return atTurnroundPoint;
    }

    public void setAtTurnroundPoint(@NonNull Integer atTurnroundPoint) {
        this.atTurnroundPoint = atTurnroundPoint;
    }

    @NonNull
    public Integer getAtTicketOffice() {
        return atTicketOffice;
    }

    public void setAtTicketOffice(@NonNull Integer atTicketOffice) {
        this.atTicketOffice = atTicketOffice;
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
        return code.longValue() == violation.code;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(code);
        result = 31 * result + code.hashCode();
        return result;
    }
}
