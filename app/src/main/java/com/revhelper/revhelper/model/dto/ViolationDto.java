package com.revhelper.revhelper.model.dto;

public class ViolationDto implements Comparable<ViolationDto> {
    private int id;
    private Integer code;
    private String name;
    private boolean inTransit;
    private boolean atStartPoint;
    private boolean atTurnroundPoint;
    private boolean atTicketOffice;
    private int conflictiveCode;
    private boolean isAcitve;

    public ViolationDto(int id, Integer code, String name, boolean inTransit, boolean atStartPoint,
                        boolean atTurnroundPoint, boolean atTicketOffice, int conflictiveCode,
                        boolean isAcitve) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.inTransit = inTransit;
        this.atStartPoint = atStartPoint;
        this.atTurnroundPoint = atTurnroundPoint;
        this.atTicketOffice = atTicketOffice;
        this.conflictiveCode = conflictiveCode;
        this.isAcitve = isAcitve;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ViolationDto violation = (ViolationDto) obj;
        return id == violation.id && name.equals(violation.name) && code.longValue() == violation.code;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(ViolationDto other) {
        return this.code.compareTo(other.code);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInTransit() {
        return inTransit;
    }

    public void setInTransit(boolean inTransit) {
        this.inTransit = inTransit;
    }

    public boolean isAtStartPoint() {
        return atStartPoint;
    }

    public void setAtStartPoint(boolean atStartPoint) {
        this.atStartPoint = atStartPoint;
    }

    public boolean isAtTurnroundPoint() {
        return atTurnroundPoint;
    }

    public void setAtTurnroundPoint(boolean atTurnroundPoint) {
        this.atTurnroundPoint = atTurnroundPoint;
    }

    public boolean isAtTicketOffice() {
        return atTicketOffice;
    }

    public void setAtTicketOffice(boolean atTicketOffice) {
        this.atTicketOffice = atTicketOffice;
    }

    public int getConflictiveCode() {
        return conflictiveCode;
    }

    public void setConflictiveCode(int conflictiveCode) {
        this.conflictiveCode = conflictiveCode;
    }

    public boolean isAcitve() {
        return isAcitve;
    }

    public void setAcitve(boolean acitve) {
        isAcitve = acitve;
    }
}
