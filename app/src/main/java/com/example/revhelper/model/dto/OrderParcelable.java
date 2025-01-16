package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class OrderParcelable implements Parcelable {

    private String number;
    private String route;
    private LocalDate date;
    private Map<String, String> directors = new HashMap<>();
    private TrainDtoParcelable train;
    private Map<String, CoachOnRevision> coachMap = new HashMap<>();
    private String revisionType;

    public OrderParcelable() {

    }

    public OrderParcelable(String number, String route, LocalDate date,
                           Map<String, String> directors, TrainDtoParcelable train,
                           Map<String, CoachOnRevision> coachMap, String revisionType) {
        this.number = number;
        this.route = route;
        this.date = date;
        this.directors = directors;
        this.train = train;
        this.coachMap = coachMap;
        this.revisionType = revisionType;
    }

    public Map<String, String> getDirectors() {
        return this.directors;
    }

    public String getNumber() {
        return this.number;
    }

    public String getRoute() {
        return this.route;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public TrainDtoParcelable getTrain() {
        return this.train;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDirectors(Map<String, String> directors) {
        this.directors = directors;
    }

    public void setTrain(TrainDtoParcelable train) {
        this.train = train;
    }

    protected OrderParcelable(Parcel in) {
        number = in.readString();
        route = in.readString();
        date = LocalDate.parse(in.readString());
        directors = new HashMap<>();
        coachMap = new HashMap<>();
        in.readMap(directors, String.class.getClassLoader());
        train = in.readParcelable(TrainDtoParcelable.class.getClassLoader());
        in.readMap(coachMap, CoachOnRevision.class.getClassLoader());
        revisionType = in.readString();
    }

    public static final Creator<OrderParcelable> CREATOR = new Creator<OrderParcelable>() {
        @Override
        public OrderParcelable createFromParcel(Parcel in) {
            return new OrderParcelable(in);
        }

        @Override
        public OrderParcelable[] newArray(int size) {
            return new OrderParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, CoachOnRevision> getCoachMap() {
        return coachMap;
    }

    public void setCoachMap(Map<String, CoachOnRevision> coachMap) {
        this.coachMap = coachMap;
    }

    public String getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(String revisionType) {
        this.revisionType = revisionType;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(route);
        dest.writeString(date.toString());
        dest.writeMap(directors);
        dest.writeParcelable(train, flags);
        dest.writeMap(coachMap);
        dest.writeString(revisionType);
    }
}
