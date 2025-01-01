package com.example.revhelper.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressLint("NewApi")
public class ViolationDtoParce implements Parcelable {

    private int id;
    private String name;
    private int revisionType;

    public ViolationDtoParce(int id, String name, int revisionType) {
        this.id = id;
        this.name = name;
        this.revisionType = revisionType;
    }

    protected ViolationDtoParce(Parcel in) {
        id = in.readInt();
        name = in.readString();
        revisionType = in.readInt();
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

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(revisionType);
    }
}
