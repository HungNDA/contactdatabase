package com.example.contactdatabaseapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ContactModel implements Parcelable {
    private String name;
    private String dob;
    private String email;
    private Bitmap profileImage;

    public ContactModel(String name, String dob, String email, Bitmap profileImage) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    // Parcelable implementation
    protected ContactModel(Parcel in) {
        name = in.readString();
        dob = in.readString();
        email = in.readString();
        profileImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel in) {
            return new ContactModel(in);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dob);
        dest.writeString(email);
        dest.writeParcelable(profileImage, flags);
    }
}
