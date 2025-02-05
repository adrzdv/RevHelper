package com.example.revhelper.services;

import java.util.regex.Pattern;

/**
 * Check service class
 */
public class CheckService {

    /**
     * Checking train string for regex matches
     *
     * @param string string for check
     * @return true if match
     */
    public boolean checkTrainRegex(String string) {
        String reg = "\\d{3}[А-Я]$";
        return Pattern.matches(reg, string);
    }

    /**
     * Check coach string for regex match
     *
     * @param string string for check
     * @return true if match
     */
    public boolean checkCoachRegex(String string) {
        String reg = "\\d{3}-\\d{5}";
        String reg2 = "\\d{5}";
        return Pattern.matches(reg, string) || Pattern.matches(reg2, string);
    }

    /**
     * Check worker string for regex match
     *
     * @param string string for check
     * @return true if match
     */
    public boolean checkWorkerDataRegex(String string) {
        String regFull = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$";
        String regShort = "^[А-ЯЁ][а-яё]+ [А-ЯЁ].+[А-ЯЁ].+$";
        String regShortSpaces = "^[А-ЯЁ][а-яё]+ [А-ЯЁ].+ [А-ЯЁ].+$";
        return Pattern.matches(regFull, string) || Pattern.matches(regShort, string)
                || Pattern.matches(regShortSpaces, string);
    }
}
