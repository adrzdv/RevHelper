package com.example.revhelper.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ViolationAttribute implements Parcelable {
    private String attrib;
    private int amount;

    public ViolationAttribute(String attrib, int amount) {
        this.attrib = attrib;
        this.amount = amount;
    }

    protected ViolationAttribute(Parcel in) {
        attrib = in.readString();
        amount = in.readInt();
    }

    public static final Creator<ViolationAttribute> CREATOR = new Creator<ViolationAttribute>() {
        @Override
        public ViolationAttribute createFromParcel(Parcel in) {
            return new ViolationAttribute(in);
        }

        @Override
        public ViolationAttribute[] newArray(int size) {
            return new ViolationAttribute[size];
        }
    };

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAttrib(String attrib) {
        this.attrib = attrib;
    }

    public String getAttrib() {
        return this.attrib;
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(attrib);
        dest.writeInt(amount);
    }
}
