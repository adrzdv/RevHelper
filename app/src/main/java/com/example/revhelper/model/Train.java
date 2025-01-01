package com.example.revhelper.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

@Entity(tableName = "trains")
public class Train {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "number")
    @NonNull
    private String number = "";
    @ColumnInfo(name = "route")
    @NonNull
    private String route = "";
    @ColumnInfo(name = "has_progressive")
    @Nullable
    private Integer hasProgressive = 0;
    @ColumnInfo(name = "has_registrator")
    @Nullable
    private Integer hasRegistrator = 0;
    @ColumnInfo(name = "dep")
    @NonNull
    private Integer dep = 0;
    @ColumnInfo(name = "has_portal")
    @Nullable
    private Integer hasPortal = 0;

    public int getId() {
        return id;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    @NonNull
    public String getRoute() {
        return route;
    }

    public int getHasRegistrator() {
        return hasRegistrator;
    }

    public int getHasProgressive() {
        return hasProgressive;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    public void setRoute(@NonNull String route) {
        this.route = route;
    }

    public void setHasProgressive(int hasProgressive) {
        this.hasProgressive = hasProgressive;
    }

    public void setHasRegistrator(int hasRegistrator) {
        this.hasRegistrator = hasRegistrator;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    public int getDep() {
        return dep;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHasPortal() {
        return hasPortal;
    }

    public void setHasPortal(int hasPortal) {
        this.hasPortal = hasPortal;
    }

    @NonNull
    public String toString() {
        return this.getNumber() + " " + this.getRoute();
    }

}
