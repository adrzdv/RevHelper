package com.example.revhelper.services;

import com.example.revhelper.model.dto.CoachOnRevision;
import com.example.revhelper.model.dto.ViolationForCoach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInserter {

    public Map<String, CoachOnRevision> makeNewMap(Map<String, CoachOnRevision> existingMap,
                                                   Map<String, CoachOnRevision> recievedMap) {

        Map<String, CoachOnRevision> resultMap = new HashMap<>(existingMap);

        for (String key : recievedMap.keySet()) {
            if (resultMap.containsKey(key)) {

                CoachOnRevision coach = makeNewCoachFromTwo(resultMap.get(key), recievedMap.get(key));
                resultMap.put(coach.getCoachNumber(), coach);

            } else {
                resultMap.put(key, recievedMap.get(key));
            }
        }

        return existingMap;

    }

    private CoachOnRevision makeNewCoachFromTwo(CoachOnRevision existingCoach, CoachOnRevision receivedCoach) {

        CoachOnRevision coach = new CoachOnRevision.Builder().build();

        LocalDateTime existingStart = existingCoach.getRevisionTime();
        LocalDateTime existingEnd = existingCoach.getRevisionEndTime();
        LocalDateTime receivedStart = receivedCoach.getRevisionTime();
        LocalDateTime receivedEnd = receivedCoach.getRevisionEndTime();

        if (existingStart.isBefore(receivedStart)) {
            coach.setRevisionTime(existingStart);
        } else {
            coach.setRevisionTime(receivedStart);
        }

        if (existingEnd.isAfter(receivedEnd)) {
            coach.setRevisionEndTime(existingEnd);
        } else {
            coach.setRevisionEndTime(receivedEnd);
        }

        List<ViolationForCoach> newList = new ArrayList<>(existingCoach.getViolationList());

        for (ViolationForCoach violation : receivedCoach.getViolationList()) {
            if (!newList.contains(violation)) {
                newList.add(violation);
            }
        }

        coach.setViolationList(newList);

        return coach;
    }

}
