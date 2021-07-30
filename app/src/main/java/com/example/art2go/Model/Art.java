package com.example.art2go.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Art implements Parcelable {
    public String name, description, author, imagePath, points, latitude, longitude;

    public Art(){

    };
    public Art(String name, String description, String author, String imagePath, String points, String latitude, String longitude) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.imagePath = imagePath;
        this.points = points;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    protected Art(Parcel in) {
        name = in.readString();
        description = in.readString();
        author = in.readString();
        imagePath = in.readString();
        points = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Art> CREATOR = new Creator<Art>() {
        @Override
        public Art createFromParcel(Parcel in) {
            return new Art(in);
        }

        @Override
        public Art[] newArray(int size) {
            return new Art[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeString(imagePath);
        dest.writeString(points);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
