package com.example.revhelper.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class BranchDto implements Parcelable {
    private int id;
    private String name;

    public BranchDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected BranchDto(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BranchDto> CREATOR = new Creator<BranchDto>() {
        @Override
        public BranchDto createFromParcel(Parcel in) {
            return new BranchDto(in);
        }

        @Override
        public BranchDto[] newArray(int size) {
            return new BranchDto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
