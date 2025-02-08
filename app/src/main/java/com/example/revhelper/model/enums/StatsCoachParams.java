package com.example.revhelper.model.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for main statistic parameters in coach
 */
public enum StatsCoachParams {
    AUTO_DOOR("Автоматические двери"),
    SKUDOPP("Система контроля управления доступом, охраны пассажирского поезда"),
    PROGRESS("Прогрессивные нормы");

    private final String additionalParamTitle;

    StatsCoachParams(String additionalParamTitle) {
        this.additionalParamTitle = additionalParamTitle;
    }

    public String getAdditionalParamTitle() {
        return additionalParamTitle;
    }

    public static List<String> getCoachParamsList() {
        List<String> stringParams = new ArrayList<>();
        for (StatsCoachParams additionalParams : StatsCoachParams.values()) {
            stringParams.add(additionalParams.getAdditionalParamTitle());
        }
        return stringParams;
    }

    public static StatsCoachParams fromString(String additionalParamTitle) {
        for (StatsCoachParams additionalParams : StatsCoachParams.values()) {
            if (additionalParams.additionalParamTitle.equalsIgnoreCase(additionalParamTitle)) {
                return additionalParams;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип проверки: " + additionalParamTitle);
    }

}
