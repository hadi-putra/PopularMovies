package com.example.android.popularmovies.util;

import android.content.ContentValues;

import com.example.android.popularmovies.data.Contract.MovieContract;
import com.example.android.popularmovies.model.MovieModel;

/**
 * Created by msk-1196 on 7/24/17.
 */

public class MovieUtil {
    private static final transient String BASE_PHOTO_PATH = "http://image.tmdb.org/t/p/w185";
    private static final transient String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w342";


    public static String getFullPosterPath(String posterPath){
        if (posterPath == null) return null;
        else return BASE_PHOTO_PATH+posterPath;
    }

    public static String getFullBackdropPath(String backdropPath){
        if (backdropPath == null) return null;
        else return BASE_BACKDROP_PATH+backdropPath;
    }

    public static ContentValues movieToContentValues(MovieModel movie){
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieContract.MovieEntry.COLUMN_POPULAITY, movie.getPopularity());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, movie.isFavorite()? 1: 0);
        return cv;
    }
}
