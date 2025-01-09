package com.example.revhelper.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressLint("NewApi")
public class ViolationDtoParce implements Parcelable {

    private int id;
    private String name;
    private int code;
    private int revisionType;
    private int amount;

    public ViolationDtoParce(int id, int code, String name, int revisionType, int amount) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.amount = amount;
    }

    protected ViolationDtoParce(Parcel in) {
        id = in.readInt();
        code = in.readInt();
        name = in.readString();
        revisionType = in.readInt();
        amount = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeInt(revisionType);
        dest.writeInt(amount);
    }

    public static final Creator<ViolationDtoParce> CREATOR = new Creator<ViolationDtoParce>() {
        @Override
        public ViolationDtoParce createFromParcel(Parcel in) {
            return new ViolationDtoParce(in);
        }

        @Override
        public ViolationDtoParce[] newArray(int size) {
            return new ViolationDtoParce[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }
}
