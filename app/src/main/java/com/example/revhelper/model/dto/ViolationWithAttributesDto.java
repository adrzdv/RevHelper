package com.example.revhelper.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ViolationWithAttributesDto implements Parcelable {
    private int id;
    private int code;
    private String name;
    private int revisionType;
    private int amount;
    private List<ViolationAttribute> attributes;

    public ViolationWithAttributesDto(int id, int code, String name, int revisionType, int amount, List<ViolationAttribute> attributes) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.revisionType = revisionType;
        this.amount = amount;
        this.attributes = attributes;
    }

    protected ViolationWithAttributesDto(Parcel in) {
        id = in.readInt();
        code = in.readInt();
        name = in.readString();
        revisionType = in.readInt();
        amount = in.readInt();
        attributes = in.createTypedArrayList(ViolationAttribute.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeInt(revisionType);
        dest.writeInt(amount);
        dest.writeTypedList(attributes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViolationWithAttributesDto> CREATOR = new Creator<ViolationWithAttributesDto>() {
        @Override
        public ViolationWithAttributesDto createFromParcel(Parcel in) {
            return new ViolationWithAttributesDto(in);
        }

        @Override
        public ViolationWithAttributesDto[] newArray(int size) {
            return new ViolationWithAttributesDto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRevisionType() {
        return revisionType;
    }

    public void setRevisionType(int revisionType) {
        this.revisionType = revisionType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<ViolationAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ViolationAttribute> attributes) {
        this.attributes = attributes;
    }
}
