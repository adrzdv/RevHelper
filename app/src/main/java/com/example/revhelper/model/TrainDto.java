package com.example.revhelper.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class TrainDto {

    @ColumnInfo(name = "number_straight")
    private String directNumber;
    @ColumnInfo(name = "number_reversed")
    private String reversedNumber;
    private String route;
    @ColumnInfo(name = "dep_name")
    private String depName;
    @ColumnInfo(name = "branch_name")
    private String branchName;
    @ColumnInfo(name = "has_registrator")
    private int hasRegistrator;
    @ColumnInfo(name = "has_progressive")
    private int hasProgressive;

    public void setDirectNumber(String dirNumber) {
        this.directNumber = dirNumber;
    }

    public void setReversedNumber(String revNumber) {
        this.reversedNumber = revNumber;
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

    public String getDirectNumber() {
        return directNumber;
    }

    public String getReversedNumber() {
        return reversedNumber;
    }

    public String getRoute() {
        return route;
    }

    public String getDepName() {
        return getDepName();
    }

    public String getBranchName() {
        return getBranchName();
    }

    public int getHasRegistrator() {
        return hasRegistrator;
    }

    public int getHasProgressive() {
        return hasProgressive;
    }

    @NonNull
    public String toString() {
        String yesString = "Да";
        String noString = "Нет";

        String res = "Поезд: " + this.getDirectNumber() + "/" + this.getReversedNumber() + "\n" +
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
        return res;
    }


}
