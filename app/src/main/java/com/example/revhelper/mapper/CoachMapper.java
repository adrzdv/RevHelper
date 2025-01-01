package com.example.revhelper.mapper;

import com.example.revhelper.dto.CoachOnRevisionParce;
import com.example.revhelper.model.CoachOnRevision;

import java.util.stream.Collectors;

public class CoachMapper {

    public static CoachOnRevisionParce fromCoachOnRevisionToParcelable(CoachOnRevision coach) {
        return new CoachOnRevisionParce(coach.getCoachNumber(),
                coach.getCoachWorker(),
                coach.isCoachSkudopp(),
                coach.isCoachAutomaticDoor(),
                coach.getRevisionTime(),
                coach.isCoachEnergySystem(),
                coach.getViolationList().stream()
                        .map(ViolationMapper::fromEntityToParce)
                        .collect(Collectors.toList()));
    }

    public static CoachOnRevision fromParcelableToCoachOnRevision(CoachOnRevisionParce coach) {
        return new CoachOnRevision(coach.getCoachNumber(),
                coach.getCoachWorker(),
                coach.isCoachSkudopp(),
                coach.isCoachAutomaticDoor(),
                coach.isCoachEnergySystem(),
                coach.getRevisionTime(),
                coach.getViolationList().stream()
                        .map(ViolationMapper::fromParceToEntity)
                        .collect(Collectors.toList()));
    }
}
