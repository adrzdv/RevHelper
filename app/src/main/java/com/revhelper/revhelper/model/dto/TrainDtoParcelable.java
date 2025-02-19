package com.revhelper.revhelper.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TrainDtoParcelable implements Parcelable {

    private String number;
    private String route;
    private String depName;
    private String branchName;
    private int hasRegistrator;
    private int hasProgressive;
    private int hasPortal;
    private int hasAutoinformator;

    public TrainDtoParcelable(String number, String route, String depName,
                              String branchName, int hasRegistrator, int hasProgressive, int hasPortal) {
        this.number = number;
        this.route = route;
        this.depName = depName;
        this.branchName = branchName;
        this.hasRegistrator = hasRegistrator;
        this.hasProgressive = hasProgressive;
        this.hasPortal = hasPortal;
    }

    protected TrainDtoParcelable(Parcel in) {
        number = in.readString();
        route = in.readString();
        depName = in.readString();
        branchName = in.readString();
        hasProgressive = in.readInt();
        hasRegistrator = in.readInt();
        hasPortal = in.readInt();
        hasAutoinformator = in.readInt();
    }

    public static final Creator<TrainDtoParcelable> CREATOR = new Creator<TrainDtoParcelable>() {
        @Override
        public TrainDtoParcelable createFromParcel(Parcel in) {
            return new TrainDtoParcelable(in);
        }

        @Override
        public TrainDtoParcelable[] newArray(int size) {
            return new TrainDtoParcelable[size];
        }
    };

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public String getNumber() {
        return number;
    }

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

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepName() {
        return this.depName;
    }

    public int getHasPortal() {
        return hasPortal;
    }

    public int getHasAutoinformator() {
        return hasAutoinformator;
    }

    public void setHasAutoinformator(int hasAutoinformator) {
        this.hasAutoinformator = hasAutoinformator;
    }

    public void setHasPortal(int hasPortal) {
        this.hasPortal = hasPortal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(route);
        dest.writeString(depName);
        dest.writeString(branchName);
        dest.writeInt(hasProgressive);
        dest.writeInt(hasRegistrator);
        dest.writeInt(hasPortal);
        dest.writeInt(hasAutoinformator);
    }
}
