package com.revhelper.revhelper.mapper;

import com.revhelper.revhelper.model.dto.ViolationDto;
import com.revhelper.revhelper.model.entity.Violation;
import com.revhelper.revhelper.model.dto.ViolationForCoach;

public class ViolationMapper {

    public static ViolationForCoach fromEntityToForCouch(Violation violation) {
        return new ViolationForCoach(violation.getId(),
                violation.getCode(),
                violation.getName(),
                1,
                1);
    }

    public static ViolationDto fromEntityToDto(Violation violation) {
        return new ViolationDto(violation.getId(),
                violation.getCode(),
                violation.getName(),
                violation.getInTransit() == 1,
                violation.getAtStartPoint() == 1,
                violation.getAtTurnroundPoint() == 1,
                violation.getAtTicketOffice() == 1,
                violation.getConflictiveCode(),
                violation.getActive() == 1);
    }

    public static ViolationForCoach fromDtoFromForCoach(ViolationDto violation) {
        return new ViolationForCoach(violation.getId(),
                violation.getCode(),
                violation.getName(),
                0,
                1);
    }
}
