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
    @ColumnInfo(name = "number_straight")
    @NonNull
    private String directNumber = "";
    @ColumnInfo(name = "number_reversed")
    @NonNull
    private String reversedNumber = "";
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
    public String getDirectNumber() {
        return directNumber;
    }

    @NonNull
    public String getReversedNumber() {
        return reversedNumber;
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

    public void setDirectNumber(@NonNull String directNumber) {
        this.directNumber = directNumber;
    }

    public void setReversedNumber(@NonNull String reversedNumber) {
        this.reversedNumber = reversedNumber;
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
        String yesString = "Да";
        String noString = "Нет";

        String res = "Поезд: " + this.getDirectNumber() + "/" + this.getReversedNumber() + "\n" +
                "Сообщение: " + this.getRoute() + "\n" +
                "Прогрессивные нормы: ";
        if (this.hasProgressive == 1) {
            res += yesString + "\n";
        } else {
            res += noString + "\n";
        }

        res += "Видеорегистратор: ";
        if (this.hasRegistrator == 1) {
            res += yesString + "\n";
        } else {
            res += noString + "\n";
        }

        res += "Мультимедийный портал: ";
        if (this.hasPortal == 1) {
            res += yesString + "\n";
        } else {
            res += noString + "\n";
        }

        return res;
    }
}
