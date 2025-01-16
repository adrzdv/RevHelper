package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.ViolationAttribute;
import com.example.revhelper.model.entity.Violation;
import com.example.revhelper.model.dto.ViolationForCoach;

import java.util.ArrayList;
import java.util.List;

public class ViolationMapper {

    public static ViolationForCoach fromEntityToForCouch(Violation violation) {
        return new ViolationForCoach(violation.getId(),
                violation.getCode(),
                violation.getName(),
                violation.getRevisionType(),
                1);
    }
}
