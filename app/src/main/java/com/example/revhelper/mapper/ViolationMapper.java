package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.ViolationDtoParce;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.model.dto.ViolationForCoach;

public class ViolationMapper {

    public static ViolationDtoParce fromEntityToParce(Violation violation) {
        return new ViolationDtoParce(violation.getId(),
                violation.getCode(),
                violation.getName(),
                violation.getRevisionType(), 1);
    }

    public static Violation fromParceToEntity(ViolationDtoParce violation) {
        return new Violation(violation.getId(),
                violation.getCode(),
                violation.getName(),
                violation.getRevisionType());
    }

    public static ViolationForCoach fromEntityToForCouch(Violation violation) {
        return new ViolationForCoach(violation.getId(),
                violation.getCode(), violation.getName(), violation.getRevisionType(), 1);
    }

    public static ViolationDtoParce fromForCoachToParce(ViolationForCoach violation) {
        return new ViolationDtoParce(violation.getId(),
                violation.getCode(),
                violation.getName(),
                violation.getRevisionType(),
                violation.getAmount());
    }

    public static ViolationForCoach fromParceToCoach(ViolationDtoParce violation) {
        return new ViolationForCoach(violation.getId(),
                violation.getCode(),
                violation.getName(),
                violation.getRevisionType(),
                violation.getAmount());

    }
}
