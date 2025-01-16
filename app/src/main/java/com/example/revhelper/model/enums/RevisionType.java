package com.example.revhelper.model.enums;

public enum RevisionType {
    IN_TRANSIT("В пути"),
    AT_START_POINT("В пункте формирования"),
    AT_TURNROUND_POINT("В пункте оборота");

    private final String revisionTypeTitle;

    RevisionType(String revisionTypeTitle) {
        this.revisionTypeTitle = revisionTypeTitle;
    }

    public String getRevisionTypeTitle() {
        return revisionTypeTitle;
    }

    public static RevisionType fromString(String revisionTypeTitle) {
        for (RevisionType revisionType : RevisionType.values()) {
            if (revisionType.revisionTypeTitle.equalsIgnoreCase(revisionTypeTitle)) {
                return revisionType;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип проверки: " + revisionTypeTitle);
    }
}
