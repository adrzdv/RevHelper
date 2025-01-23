package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressLint("NewApi")
public class OrderDtoParcelable implements Parcelable {
    private String number;
    private LocalDate date;
    private String route;
    private String revisionType;
    private TrainDtoParcelable train;
    private Map<String, Worker> crewLeaders;
    private Map<String, CoachOnRevision> coachMap;
    private Boolean isQualityPassport;
    private Boolean isPrice;
    private Boolean isAutoinformator;
    private Boolean isRadio;

    public OrderDtoParcelable(String number, LocalDate date, String route, String revisionType) {

        this.number = number;
        this.date = date;
        this.route = route;
        this.revisionType = revisionType;
        this.crewLeaders = new HashMap<>();
        this.coachMap = new HashMap<>();
        this.isQualityPassport = false;

    }

    protected OrderDtoParcelable(Parcel in) {
        number = in.readString();
        String dateString = in.readString();
        if (dateString != null) {
            date = LocalDate.parse(dateString);
        } else {
            date = null;
        }
        route = in.readString();
        revisionType = in.readString();
        crewLeaders = new HashMap<>();
        in.readMap(crewLeaders, Worker.class.getClassLoader());
        coachMap = new HashMap<>();
        in.readMap(coachMap, CoachOnRevision.class.getClassLoader());
        train = in.readParcelable(TrainDtoParcelable.class.getClassLoader());
        isQualityPassport = in.readBoolean();
        isPrice = in.readBoolean();
        isAutoinformator = in.readBoolean();
        isRadio = in.readBoolean();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(number);
        if (date != null) {
            dest.writeString(date.toString());
        } else {
            dest.writeString(null);
        }
        dest.writeString(route);
        dest.writeString(revisionType);
        dest.writeMap(crewLeaders);
        dest.writeMap(coachMap);
        dest.writeParcelable(train, flags);
        dest.writeBoolean(isQualityPassport);
        dest.writeBoolean(isPrice);
        dest.writeBoolean(isAutoinformator);
        dest.writeBoolean(isRadio);
    }

    public static final Creator<OrderDtoParcelable> CREATOR = new Creator<OrderDtoParcelable>() {
        @Override
        public OrderDtoParcelable createFromParcel(Parcel in) {
            return new OrderDtoParcelable(in);
        }

        @Override
        public OrderDtoParcelable[] newArray(int size) {
            return new OrderDtoParcelable[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(String revisionType) {
        this.revisionType = revisionType;
    }

    public TrainDtoParcelable getTrain() {
        return train;
    }

    public void setTrain(TrainDtoParcelable train) {
        this.train = train;
    }

    public Map<String, Worker> getCrewLeaders() {
        return crewLeaders;
    }

    public void setCrewLeaders(Map<String, Worker> crewLeaders) {
        this.crewLeaders = crewLeaders;
    }

    public Map<String, CoachOnRevision> getCoachMap() {
        return coachMap;
    }

    public void setCoachMap(Map<String, CoachOnRevision> coachMap) {
        this.coachMap = coachMap;
    }

    public Boolean isQualityPassport() {
        return isQualityPassport;
    }

    public void setQualityPassport(Boolean qualityPassport) {
        isQualityPassport = qualityPassport;
    }

    public Boolean isPrice() {
        return isPrice;
    }

    public void setPrice(Boolean price) {
        isPrice = price;
    }

    public boolean isAutoinformator() {
        return isAutoinformator;
    }

    public Boolean getIsPrice() {
        return this.isPrice;
    }

    public Boolean getIsQualityPassport() {
        return this.isQualityPassport;
    }

    public Boolean getIsAutoinformator() {
        return this.isAutoinformator;
    }

    public Boolean getRadio() {
        return isRadio;
    }

    public void setRadio(Boolean radio) {
        isRadio = radio;
    }

    public void setAutoinformator(boolean autoinformator) {
        isAutoinformator = autoinformator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDtoParcelable that = (OrderDtoParcelable) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
