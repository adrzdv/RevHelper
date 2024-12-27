package com.example.revhelper.dto;

public class CoachRepresentViewDto {
    private String coachNumber;
    private String coachWorker;

    public CoachRepresentViewDto(String coachNumber, String coachWorker) {
        this.coachNumber = coachNumber;
        this.coachWorker = coachWorker;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public String getCoachWorker() {
        return coachWorker;
    }
}
