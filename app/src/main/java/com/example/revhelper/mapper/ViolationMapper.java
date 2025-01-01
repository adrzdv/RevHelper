package com.example.revhelper.mapper;

import com.example.revhelper.dto.ViolationDtoParce;
import com.example.revhelper.model.Violation;

public class ViolationMapper {

    public static ViolationDtoParce fromEntityToParce(Violation violation) {
        return new ViolationDtoParce(violation.getId(),
                violation.getName(),
                violation.getRevisionType());
    }

    public static Violation fromParceToEntity(ViolationDtoParce violation) {
        return new Violation(violation.getId(),
                violation.getName(),
                violation.getRevisionType());
    }
}
