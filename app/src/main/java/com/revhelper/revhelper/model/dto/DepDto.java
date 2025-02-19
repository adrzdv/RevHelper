package com.revhelper.revhelper.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DepDto implements Parcelable {

    private int id;
    private String name;
    private BranchDto branch;

    public DepDto(int id, String name, BranchDto branch) {
        this.id = id;
        this.name = name;
        this.branch = branch;
    }

    protected DepDto(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<DepDto> CREATOR = new Creator<DepDto>() {
        @Override
        public DepDto createFromParcel(Parcel in) {
            return new DepDto(in);
        }

        @Override
        public DepDto[] newArray(int size) {
            return new DepDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

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

    public BranchDto getBranch() {
        return branch;
    }

    public void setBranch(BranchDto branch) {
        this.branch = branch;
    }
}
