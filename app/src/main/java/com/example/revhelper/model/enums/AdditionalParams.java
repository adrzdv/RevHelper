package com.example.revhelper.model.enums;

public enum AdditionalParams {
    AUTO_DOOR("Автоматические двери"),
    SKUDOPP("Система контроля управления доступом, охраны пассажирского поезда"),
    PROGRESS("Прогрессивные нормы");

    private final String additionalParamTitle;

    AdditionalParams(String additionalParamTitle) {
        this.additionalParamTitle = additionalParamTitle;
    }

    public String getAdditionalParamTitle() {
        return additionalParamTitle;
    }

    public static AdditionalParams fromString(String additionalParamTitle) {
        for (AdditionalParams additionalParams : AdditionalParams.values()) {
            if (additionalParams.additionalParamTitle.equalsIgnoreCase(additionalParamTitle)) {
                return additionalParams;
            }
        }
        throw new IllegalArgumentException("Неизвестный тип проверки: " + additionalParamTitle);
    }

}
