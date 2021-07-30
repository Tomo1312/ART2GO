package com.example.art2go.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class User implements Parcelable {

    public String name;
    public String surname;
    public String userName;
    public String userId;
    public int userPoints;
    public String userEmail;
    public String unlockedSculptures;
    public boolean banned;

    public User() {}
    public User(String name, String surname, String userName, String userEmail, String unlockedSculptures, int userPoints) {
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.userEmail = userEmail;
        this.unlockedSculptures = unlockedSculptures;
        this.userPoints = userPoints;
        this.banned = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected User(Parcel in) {
        userName = in.readString();
        userPoints = in.readInt();
        userEmail = in.readString();
        unlockedSculptures = in.readString();
        userId = in.readString();
        banned = in.readBoolean();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userBodovi) {
        this.userPoints = userBodovi;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUnlockedSculptures() {
        return unlockedSculptures;
    }

    public void setUnlockedSculptures(String unlockedSculptures) {
        this.unlockedSculptures = unlockedSculptures;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeInt(userPoints);
        dest.writeString(userEmail);
        dest.writeString(unlockedSculptures);
    }
}
