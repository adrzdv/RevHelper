package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
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
    private boolean isQualityPassport;
    private Boolean isPrice;
    private boolean isAutoinformator;
    private boolean isRadio;
    private Boolean isInform;

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
        String price = in.readString();
        if (price != null) {
            isPrice = Boolean.parseBoolean(price);
        } else {
            isPrice = null;
        }
        isAutoinformator = in.readBoolean();
        isRadio = in.readBoolean();
        String inform = in.readString();
        if (inform != null) {
            isInform = Boolean.parseBoolean(inform);
        } else {
            isInform = null;
        }
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
        if (isPrice != null) {
            dest.writeString(isPrice.toString());
        } else {
            dest.writeString(null);
        }
        dest.writeBoolean(isAutoinformator);
        dest.writeBoolean(isRadio);
        if (isInform != null) {
            dest.writeString(isInform.toString());
        } else {
            dest.writeString(null);
        }
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

    public boolean isQualityPassport() {
        return isQualityPassport;
    }

    public void setQualityPassport(boolean qualityPassport) {
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

    public void setIsInform(Boolean isInform) {
        this.isInform = isInform;
    }

    public Boolean getIsInform() {
        return isInform;
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
