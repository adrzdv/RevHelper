package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.deprecated.CoachRepresentViewDto;

public class CoachMapper {

    public static CoachRepresentViewDto fromOnRevisionToRepresentDto(CoachOnRevision coach) {
        return new CoachRepresentViewDto(coach.getCoachNumber(), null);
    }
}
