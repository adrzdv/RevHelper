package com.example.revhelper.mapper;

import com.example.revhelper.dto.CoachOnRevisionParce;
import com.example.revhelper.model.CoachOnRevision;

public class CoachMapper {

    public static CoachOnRevisionParce fromCoachOnRevisionToParcelable(CoachOnRevision coach) {
        return new CoachOnRevisionParce(coach.getCoachNumber(),
                coach.getCoachWorker(),
                coach.isCoachSkudopp(),
                coach.isCoachAutomaticDoor(),
                coach.getRevisionTime());
    }

    public static CoachOnRevision fromParcelableToCoachOnRevision(CoachOnRevisionParce coach) {
        return new CoachOnRevision(coach.getCoachNumber(),
                coach.getCoachWorker(),
                coach.isCoachSkudopp(),
                coach.isCoachAutomaticDoor(),
                coach.getRevisionTime());
    }
}
