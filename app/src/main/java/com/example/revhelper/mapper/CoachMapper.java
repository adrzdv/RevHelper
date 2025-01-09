package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.CoachOnRevisionParce;
import com.example.revhelper.model.dto.CoachOnRevision;

import java.util.stream.Collectors;

public class CoachMapper {

    public static CoachOnRevisionParce fromCoachOnRevisionToParcelable(CoachOnRevision coach) {
        return new CoachOnRevisionParce(coach.getCoachNumber(),
                coach.getCoachWorker(),
                coach.isCoachSkudopp(),
                coach.isCoachAutomaticDoor(),
                coach.isCoachProgressive(),
                coach.getRevisionTime(),
                coach.isCoachEnergySystem(),
                coach.getViolationList().stream()
                        .map(ViolationMapper::fromForCoachToParce)
                        .collect(Collectors.toList()));
    }

    public static CoachOnRevision fromParcelableToCoachOnRevision(CoachOnRevisionParce coach) {
        return new CoachOnRevision(coach.getCoachNumber(),
                coach.getCoachWorker(),
                coach.isCoachSkudopp(),
                coach.isCoachAutomaticDoor(),
                coach.isCoachEnergySystem(),
                coach.isCoachProgressive(),
                coach.getRevisionTime(),
                coach.getViolationList().stream()
                        .map(ViolationMapper::fromParceToCoach)
                        .collect(Collectors.toList()));
    }
}
