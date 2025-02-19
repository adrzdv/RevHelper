package com.revhelper.revhelper.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.revhelper.revhelper.model.entity.Attribute;

import java.util.List;

@Dao
public interface AttributeDao {
    @Query("SELECT * FROM violation_attribute WHERE id_violation = :violationId")
    List<Attribute> getAttribsForViolation(int violationId);
}
