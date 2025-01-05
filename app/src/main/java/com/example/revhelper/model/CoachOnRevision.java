package com.example.revhelper.model;

import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.List;

public class CoachOnRevision {
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

}
