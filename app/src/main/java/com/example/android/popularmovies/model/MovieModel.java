package com.example.android.popularmovies.model;

import com.squareup.moshi.Json;

/**
 * Created by msk-1196 on 6/18/17.
 */

public class MovieModel {
    public static final transient String BASE_PHOTO_PATH = "http://image.tmdb.org/t/p/w185";
    private String title;
    @Json(name = "vote_average") private double voteAverage;
    @Json(name = "poster_path") private String posterPath;
    @Json(name = "release_date") private String releaseDate;
    private String overview;

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

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
