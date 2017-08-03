package com.robby.popular_movies_stage_2a.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Robby on 7/27/2017.
 *
 * @author Robby Tan
 */

public class MovieReview implements Parcelable {

    public static final String TAG_ID = "id";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_CONTENT = "content";

    private String id;
    private String author;
    private String content;

    public MovieReview() {
    }

    protected MovieReview(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
