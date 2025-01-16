package com.example.revhelper.services;

import java.util.regex.Pattern;

public class CheckService {

    public boolean checkTrainRegex(String string) {
        String reg = "\\d{3}[А-Я]$";
        return Pattern.matches(reg, string);
    }

    public boolean checkCoachRegex(String string) {
        String reg = "\\d{3}-\\d{5}";
        String reg2 = "\\d{5}";
        return Pattern.matches(reg, string) || Pattern.matches(reg2, string);
    }

    public boolean checkWorkerDataRegex(String string) {
        String reg = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$";
        return Pattern.matches(reg, string);
    }
}
