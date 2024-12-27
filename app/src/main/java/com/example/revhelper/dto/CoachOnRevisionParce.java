package com.example.revhelper.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CoachOnRevisionParce implements Parcelable {

    private String coachNumber;
    private String coachWorker;
    private boolean coachSkudopp;
    private boolean coachAutomaticDoor;
    private LocalDateTime revisionTime;

    public CoachOnRevisionParce(String coachNumber, String coachWorker, boolean coachSkudopp, boolean coachAutomaticDoor,
                                LocalDateTime revisionTime) {
        this.coachAutomaticDoor = coachAutomaticDoor;
        this.coachSkudopp = coachSkudopp;
        this.coachWorker = coachWorker;
        this.coachNumber = coachNumber;
        this.revisionTime = revisionTime;
    }

    @SuppressLint("NewApi")
    protected CoachOnRevisionParce(Parcel in) {
        coachNumber = in.readString();
        coachWorker = in.readString();
        coachSkudopp = in.readByte() != 0;
        coachAutomaticDoor = in.readByte() != 0;
        revisionTime = LocalDateTime.parse(in.readString());
    }

    public static final Creator<CoachOnRevisionParce> CREATOR = new Creator<CoachOnRevisionParce>() {
        @Override
        public CoachOnRevisionParce createFromParcel(Parcel in) {
            return new CoachOnRevisionParce(in);
        }

        @Override
        public CoachOnRevisionParce[] newArray(int size) {
            return new CoachOnRevisionParce[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(coachNumber);
        dest.writeString(coachWorker);
        dest.writeByte((byte) (coachSkudopp ? 1 : 0));
        dest.writeByte((byte) (coachAutomaticDoor ? 1 : 0));
        dest.writeString(revisionTime.toString());
    }
}
