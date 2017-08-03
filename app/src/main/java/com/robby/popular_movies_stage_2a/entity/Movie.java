package com.robby.popular_movies_stage_2a.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.robby.popular_movies_stage_2a.BuildConfig;

/**
 * Created by Robby on 7/2/2017.
 *
 * @author Robby Tan
 */

public class Movie implements Parcelable {

    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_POSTER = "poster_path";
    public static final String TAG_RELEASE_DATE = "release_date";
    public static final String TAG_VOTE_AVERAGE = "vote_average";

    private String id;
    private double voteAverage;
    private String title;
    private String posterPath;
    private byte[] poster;
    private String overview;
    private String releaseDate;


    public Movie() {
    }


    protected Movie(Parcel in) {
        id = in.readString();
        voteAverage = in.readDouble();
        title = in.readString();
        posterPath = in.readString();
        poster = in.createByteArray();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return BuildConfig.POSTER_IMAGE + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeDouble(voteAverage);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeByteArray(poster);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
