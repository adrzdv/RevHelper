package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcelable;

import java.time.LocalDate;
import java.util.Map;

@SuppressLint("NewApi")
public class OrderDtoParcelable implements Parcelable {
    private String number;
    private LocalDate date;
    private String route;
    private String revisionType;
    private TrainDtoParcelable train;
    private Map<String, String> crewLeaders;
    private Map<String, CoachOnRevision> coachMap;
    private boolean isQualityPassport;
    private boolean isPrice;

}
