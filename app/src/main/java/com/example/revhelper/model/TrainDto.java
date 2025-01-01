package com.example.revhelper.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class TrainDto {

    @ColumnInfo(name = "number")
    private String number;
    private String route;
    @ColumnInfo(name = "dep_name")
    private String depName;
    @ColumnInfo(name = "branch_name")
    private String branchName;
    @ColumnInfo(name = "has_registrator")
    private int hasRegistrator;
    @ColumnInfo(name = "has_progressive")
    private int hasProgressive;
    @ColumnInfo(name = "has_portal")
    private int hasPortal;

    public TrainDto(String number, String route, String branchName, String depName,
                    int hasRegistrator, int hasProgressive, int hasPortal) {
        this.number = number;
        this.route = route;
        this.depName = depName;
        this.branchName = branchName;
        this.hasRegistrator = hasRegistrator;
        this.hasProgressive = hasProgressive;
        this.hasPortal = hasPortal;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setHasProgressive(int progres) {
        this.hasProgressive = progres;
    }

    public void setHasRegistrator(int regist) {
        this.hasRegistrator = regist;
    }

    public String getNumber() {
        return this.number;
    }

    public String getRoute() {
        return route;
    }

    public String getDepName() {
        return this.depName;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public int getHasRegistrator() {
        return hasRegistrator;
    }

    public int getHasProgressive() {
        return hasProgressive;
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

        String res = "Поезд: " + this.getNumber() + "\n" +
                "Сообщение: " + this.getRoute() + "\n" +
                "Формирование: " + this.depName + ", ФПКФ " + this.branchName + "\n" +
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
