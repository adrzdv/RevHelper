package com.example.revhelper.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;


@Entity(tableName = "trains")
public class Train {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "number_straight")
    @NonNull
    public String directNumber = "";
    @ColumnInfo(name = "number_reversed")
    @NonNull
    public String reversedNumber = "";
    @ColumnInfo(name = "route")
    @NonNull
    public String route = "";
    @ColumnInfo(name = "has_progressive")
    @Nullable
    public Integer hasProgressive = 0;
    @ColumnInfo(name = "has_registrator")
    @Nullable
    public Integer hasRegistrator = 0;
    @ColumnInfo(name = "dep")
    @NonNull
    public Integer dep = 0;

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

    public int isHasProgressive() {
        return hasProgressive;
    }

    public int isHasRegistrator() {
        return hasRegistrator;
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
        return res;
    }
}
