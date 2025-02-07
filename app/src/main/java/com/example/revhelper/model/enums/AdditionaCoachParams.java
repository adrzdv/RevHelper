package com.example.revhelper.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum AdditionaCoachParams {
    AUTO_DOOR("Автоматические двери"),
    SKUDOPP("Система контроля управления доступом, охраны пассажирского поезда"),
    PROGRESS("Прогрессивные нормы");

    private final String additionalParamTitle;

    AdditionaCoachParams(String additionalParamTitle) {
        this.additionalParamTitle = additionalParamTitle;
    }

    public String getAdditionalParamTitle() {
        return additionalParamTitle;
    }

    public static List<String> getCoachParamsList() {
        List<String> stringParams = new ArrayList<>();
        for (AdditionaCoachParams additionalParams : AdditionaCoachParams.values()) {
            stringParams.add(additionalParams.getAdditionalParamTitle());
        }
        return stringParams;
    }

    public static AdditionaCoachParams fromString(String additionalParamTitle) {
        for (AdditionaCoachParams additionalParams : AdditionaCoachParams.values()) {
            if (additionalParams.additionalParamTitle.equalsIgnoreCase(additionalParamTitle)) {
                return additionalParams;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип проверки: " + additionalParamTitle);
    }

}
