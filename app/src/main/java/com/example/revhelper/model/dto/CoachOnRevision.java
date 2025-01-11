package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.revhelper.model.entity.Violation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class CoachOnRevision implements Parcelable {
    private String coachNumber;
    private String coachWorker;
    private boolean coachSkudopp;
    private boolean coachAutomaticDoor;
    private boolean coachEnergySystem;
    private boolean coachProgressive;
    private LocalDateTime revisionTime;
    private List<ViolationForCoach> violationList;

    public CoachOnRevision(String coachNumber,
                           String coachWorker,
                           boolean coachSkudopp,
                           boolean coachAutomaticDoor,
                           boolean coachEnergySystem,
                           boolean coachProgressive,
                           LocalDateTime revisionTime,
                           List<ViolationForCoach> violationList) {
        this.coachAutomaticDoor = coachAutomaticDoor;
        this.coachSkudopp = coachSkudopp;
        this.coachWorker = coachWorker;
        this.coachNumber = coachNumber;
        this.coachEnergySystem = coachEnergySystem;
        this.coachProgressive = coachProgressive;
        this.revisionTime = revisionTime;
        this.violationList = violationList;
    }

    public void setCoachProgressive(boolean coachProgressive) {
        this.coachProgressive = coachProgressive;
    }

    public boolean isCoachProgressive() {
        return this.coachProgressive;
    }

    public List<ViolationForCoach> getViolationList() {
        return this.violationList;
    }

    public void setViolationList(List<ViolationForCoach> violationList) {
        this.violationList = violationList;
    }

    public boolean isCoachEnergySystem() {
        return this.coachEnergySystem;
    }

    public void setCoachEnergySystem(boolean coachEnergySystem) {
        this.coachEnergySystem = coachEnergySystem;
    }

    public void setRevisionTime(LocalDateTime revisionTime) {
        this.revisionTime = revisionTime;
    }

    public LocalDateTime getRevisionTime() {
        return this.revisionTime;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public void setCoachWorker(String coachWorker) {
        this.coachWorker = coachWorker;
    }

    public void setCoachSkudopp(boolean coachSkudopp) {
        this.coachSkudopp = coachSkudopp;
    }

    public void setCoachAutomaticDoor(boolean coachAutomaticDoor) {
        this.coachAutomaticDoor = coachAutomaticDoor;
    }

    public String getCoachNumber() {
        return this.coachNumber;
    }

    public String getCoachWorker() {
        return this.coachWorker;
    }

    public boolean isCoachSkudopp() {
        return this.coachSkudopp;
    }

    public boolean isCoachAutomaticDoor() {
        return this.coachAutomaticDoor;
    }

    protected CoachOnRevision(Parcel in) {
        coachNumber = in.readString();
        coachWorker = in.readString();
        coachSkudopp = in.readByte() != 0;
        coachAutomaticDoor = in.readByte() != 0;
        coachEnergySystem = in.readByte() != 0;
        coachProgressive = in.readByte() != 0;
        revisionTime = LocalDateTime.parse(in.readString());
        violationList = new ArrayList<>();
        in.readList(violationList, Violation.class.getClassLoader());
    }

    public static final Creator<CoachOnRevision> CREATOR = new Creator<CoachOnRevision>() {
        @Override
        public CoachOnRevision createFromParcel(Parcel in) {
            return new CoachOnRevision(in);
        }

        @Override
        public CoachOnRevision[] newArray(int size) {
            return new CoachOnRevision[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(coachNumber);
        dest.writeString(coachWorker);
        dest.writeByte((byte) (coachSkudopp ? 1 : 0));
        dest.writeByte((byte) (coachAutomaticDoor ? 1 : 0));
        dest.writeByte((byte) (coachEnergySystem ? 1 : 0));
        dest.writeByte((byte) (coachProgressive ? 1 : 0));
        dest.writeString(revisionTime.toString());
        dest.writeList(violationList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
