package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class ViolationForCoach implements Parcelable, Comparable<ViolationForCoach> {
    private int id;
    private Integer code;
    private String name;
    private int revisionType;
    private int amount;
    private List<ViolationAttribute> attributes = new ArrayList<>();

    public ViolationForCoach(int id, int code, @NonNull String name, int revisionType, int amount) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.amount = amount;
    }

    public ViolationForCoach(int id, int code, @NonNull String name, int revisionType, int amount, List<ViolationAttribute> attributes) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.amount = amount;
        this.attributes = attributes;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setRevisionType(int revisionType) {
        this.revisionType = revisionType;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getRevisionType() {
        return this.revisionType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ViolationForCoach violation = (ViolationForCoach) obj;
        return id == violation.id && name.equals(violation.name) && code == violation.code;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViolationForCoach> CREATOR = new Creator<ViolationForCoach>() {
        @Override
        public ViolationForCoach createFromParcel(Parcel in) {
            return new ViolationForCoach(in);
        }

        @Override
        public ViolationForCoach[] newArray(int size) {
            return new ViolationForCoach[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeInt(revisionType);
        dest.writeInt(amount);
        dest.writeTypedList(attributes);
    }

    protected ViolationForCoach(Parcel in) {
        id = in.readInt();
        code = in.readInt();
        name = in.readString();
        revisionType = in.readInt();
        amount = in.readInt();
        attributes = in.createTypedArrayList(ViolationAttribute.CREATOR);
    }

    public List<ViolationAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ViolationAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int compareTo(ViolationForCoach o) {
        return this.code.compareTo(o.code);
    }
}
