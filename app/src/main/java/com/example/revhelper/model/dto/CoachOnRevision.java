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
import java.util.Objects;

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
    private String coachWorkerDep;
    private boolean isTrailingCar = false;


    public static class Builder {
        private String coachNumber;
        private String coachWorker;
        private boolean coachSkudopp;
        private boolean coachAutomaticDoor;
        private boolean coachEnergySystem;
        private boolean coachProgressive;
        private LocalDateTime revisionTime;
        private List<ViolationForCoach> violationList = new ArrayList<>();
        private String coachWorkerDep;
        private boolean isTrailingCar;

        public Builder setTrailingCar(boolean isTrailingCar) {
            this.isTrailingCar = isTrailingCar;
            return this;
        }

        public Builder setCoachWorkerDep(String workerDep) {
            this.coachWorkerDep = workerDep;
            return this;
        }

        public Builder setCoachNumber(String coachNumber) {
            this.coachNumber = coachNumber;
            return this;
        }

        public Builder setCoachWorker(String coachWorker) {
            this.coachWorker = coachWorker;
            return this;
        }

        public Builder setCoachSkudopp(boolean coachSkudopp) {
            this.coachSkudopp = coachSkudopp;
            return this;
        }

        public Builder setCoachAutomaticDoor(boolean coachAutomaticDoor) {
            this.coachAutomaticDoor = coachAutomaticDoor;
            return this;
        }

        public Builder setCoachEnergySystem(boolean coachEnergySystem) {
            this.coachEnergySystem = coachEnergySystem;
            return this;
        }

        public Builder setCoachProgressive(boolean coachProgressive) {
            this.coachProgressive = coachProgressive;
            return this;
        }

        public Builder setRevisionTime(LocalDateTime revisionTime) {
            this.revisionTime = revisionTime;
            return this;
        }

        public Builder setViolationList(List<ViolationForCoach> violationList) {
            this.violationList = violationList;
            return this;
        }

        public CoachOnRevision build() {
            return new CoachOnRevision(this);
        }
    }

    public boolean isTrailingCar() {
        return isTrailingCar;
    }

    public void setTrailingCar(boolean trailingCar) {
        isTrailingCar = trailingCar;
    }

    public String getCoachWorkerDep() {
        return coachWorkerDep;
    }

    public void setCoachWorkerDep(String coachWorkerDep) {
        this.coachWorkerDep = coachWorkerDep;
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
        dest.writeString(coachWorkerDep);
        dest.writeBoolean(isTrailingCar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoachOnRevision that = (CoachOnRevision) o;
        return Objects.equals(coachNumber, that.coachNumber);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(coachNumber);
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
        coachWorkerDep = in.readString();
        isTrailingCar = in.readBoolean();
    }

    private CoachOnRevision(Builder builder) {
        this.coachNumber = builder.coachNumber;
        this.coachWorker = builder.coachWorker;
        this.coachSkudopp = builder.coachSkudopp;
        this.coachAutomaticDoor = builder.coachAutomaticDoor;
        this.coachEnergySystem = builder.coachEnergySystem;
        this.coachProgressive = builder.coachProgressive;
        this.revisionTime = builder.revisionTime;
        this.violationList = builder.violationList;
        this.coachWorkerDep = builder.coachWorkerDep;
        this.isTrailingCar = builder.isTrailingCar;
    }

}
