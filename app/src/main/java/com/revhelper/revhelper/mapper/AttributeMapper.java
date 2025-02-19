package com.revhelper.revhelper.mapper;

import com.revhelper.revhelper.model.dto.ViolationAttribute;
import com.revhelper.revhelper.model.entity.Attribute;

public class AttributeMapper {
    public static ViolationAttribute fromEntityToDto(Attribute attribute) {
        return new ViolationAttribute(attribute.getName(), 1);
    }
}
