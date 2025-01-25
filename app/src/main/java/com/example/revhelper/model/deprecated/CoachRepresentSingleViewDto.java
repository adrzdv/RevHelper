package com.example.revhelper.model.deprecated;

@Deprecated
public class CoachRepresentSingleViewDto {
    private String coachNumber;

    public CoachRepresentSingleViewDto(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

}
