package com.robby.popular_movies_stage_2a.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Robby on 7/29/2017.
 *
 * @author Robby Tan
 */

public class MovieTrailer implements Parcelable {

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_KEY = "key";

    private String id;
    private String name;
    private String key;

    public MovieTrailer() {
    }

    protected MovieTrailer(Parcel in) {
        id = in.readString();
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(key);
    }
}
