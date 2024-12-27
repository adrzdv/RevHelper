package com.example.revhelper.model;

import java.time.LocalDateTime;

public class CoachOnRevision {
    private String coachNumber;
    private String coachWorker;
    private boolean coachSkudopp;
    private boolean coachAutomaticDoor;
    private LocalDateTime revisionTime;

    public CoachOnRevision(String coachNumber, String coachWorker, boolean coachSkudopp, boolean coachAutomaticDoor, LocalDateTime revisionTime) {
        this.coachAutomaticDoor = coachAutomaticDoor;
        this.coachSkudopp = coachSkudopp;
        this.coachWorker = coachWorker;
        this.coachNumber = coachNumber;
        this.revisionTime = revisionTime;
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
