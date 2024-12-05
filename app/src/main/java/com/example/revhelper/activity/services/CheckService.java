package com.example.revhelper.activity.services;

import java.util.regex.Pattern;

public class CheckService {

    public boolean checkTrainRegex(String string) {
        String reg = "\\d{3}[А-Я]$";
        return Pattern.matches(reg, string);
    }

    public boolean checkCoachRegex(String string) {
        String reg = "\\d{3}-\\d{5}";
        return Pattern.matches(reg, string);
    }
}
