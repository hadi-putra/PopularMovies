package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by msk-1196 on 6/18/17.
 */

public class MovieModel implements Parcelable {
    private static final transient String BASE_PHOTO_PATH = "http://image.tmdb.org/t/p/w185";
    private static final transient String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w342";
    private String title;
    @Json(name = "backdrop_path") private String backdropPath;
    @Json(name = "vote_average") private double voteAverage;
    @Json(name = "poster_path") private String posterPath;
    @Json(name = "release_date") private String releaseDate;
    private String overview;

    public MovieModel(Parcel parcel) {
        title = parcel.readString();
        backdropPath = parcel.readString();
        voteAverage = parcel.readDouble();
        posterPath = parcel.readString();
        releaseDate = parcel.readString();
        overview = parcel.readString();
    }

    public MovieModel() {
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        if (posterPath == null) return null;
        else return BASE_PHOTO_PATH+posterPath;
    }

    public String getBackdropPath() {
        if (backdropPath == null) return null;
        else return BASE_BACKDROP_PATH+backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(backdropPath);
        parcel.writeDouble(voteAverage);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public MovieModel createFromParcel(Parcel parcel) {
            return new MovieModel(parcel);
        }

        @Override
        public MovieModel[] newArray(int i) {
            return new MovieModel[i];
        }
    };
}
