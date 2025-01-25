package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("NewApi")
public class ViolationForCoach implements Parcelable, Comparable<ViolationForCoach> {
    private int id;
    private Integer code;
    private String name;
    private int revisionType;
    private int amount;
    private List<ViolationAttribute> attributes;
    private boolean isResolved = false;

    public ViolationForCoach(int id, int code, @NonNull String name, int revisionType, int amount) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.amount = amount;
        attributes = new ArrayList<>();
    }

    public ViolationForCoach(int id, int code, @NonNull String name, int revisionType, int amount, List<ViolationAttribute> attributes) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.amount = amount;
        this.attributes = attributes;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeInt(revisionType);
        dest.writeInt(amount);
        dest.writeTypedList(attributes);
        dest.writeBoolean(isResolved);
    }

    protected ViolationForCoach(Parcel in) {
        id = in.readInt();
        code = in.readInt();
        name = in.readString();
        revisionType = in.readInt();
        amount = in.readInt();
        attributes = in.createTypedArrayList(ViolationAttribute.CREATOR);
        isResolved = in.readBoolean();
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

    public List<ViolationAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ViolationAttribute> attributes) {
        this.attributes = attributes;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    @Override
    public int compareTo(ViolationForCoach o) {
        return this.code.compareTo(o.code);
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
        return id == violation.id && Objects.equals(code, violation.code);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(code);
        result = 31 * result + code.hashCode();
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


}
