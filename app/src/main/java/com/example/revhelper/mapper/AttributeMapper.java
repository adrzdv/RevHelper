package com.example.revhelper.mapper;

import com.example.revhelper.model.dto.ViolationAttribute;
import com.example.revhelper.model.entity.Attribute;

public class AttributeMapper {
    public static ViolationAttribute fromEntityToDto(Attribute attribute) {
        return new ViolationAttribute(attribute.getName(), 1);
    }
}
