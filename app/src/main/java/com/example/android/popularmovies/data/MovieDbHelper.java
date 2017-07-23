package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.Contract.MovieContract;
import com.example.android.popularmovies.data.Contract.ReviewContract;
import com.example.android.popularmovies.data.Contract.TrailerContract;

/**
 * Created by msk-1196 on 7/16/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "popular_movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_MOVIE =
                "CREATE TABLE IF NOT EXISTS "+ MovieContract.MovieEntry.TABLE_NAME+" (" +
                        MovieContract.MovieEntry._ID                +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MovieEntry.COLUMN_MOVIEDB_ID  +" INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE       +" TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW    +" TEXT, " +
                        MovieContract.MovieEntry.COLUMN_BACKDROP    +" TEXT, " +
                        MovieContract.MovieEntry.COLUMN_POSTER      +" TEXT, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVG    +" REAL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_COUNT  +" INTEGER, " +
                        MovieContract.MovieEntry.COLUMN_POPULAITY   +" REAL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE+" TEXT, " +
                        MovieContract.MovieEntry.COLUMN_IS_FAVORITE +" INTEGER NOT NULL DEFAULT 0, " +
                        "UNIQUE ("+MovieContract.MovieEntry.COLUMN_MOVIEDB_ID+") ON CONFLICT REPLACE) " ;

        final String SQL_CREATE_TABLE_REVIEW =
                "CREATE TABLE IF NOT EXISTS "+ ReviewContract.RevieEntry.TABLE_NAME+" (" +
                        ReviewContract.RevieEntry._ID               +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewContract.RevieEntry.COLUMN_MOVIEDB_ID +" INTEGER NOT NULL, " +
                        ReviewContract.RevieEntry.COLUMN_REVIEW_ID  +" TEXT NOT NULL, " +
                        ReviewContract.RevieEntry.COLUMN_AUTHOR     +" TEXT, " +
                        ReviewContract.RevieEntry.COLUMN_CONTENT    +" TEXT, " +
                        ReviewContract.RevieEntry.COLUMN_URL        +" TEXT) " ;

        final String SQL_CREATE_TABLE_TRAILER =
                "CREATE TABLE IF NOT EXISTS "+ TrailerContract.TrailerEntry.TABLE_NAME+" (" +
                        TrailerContract.TrailerEntry._ID                +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TrailerContract.TrailerEntry.COLUMN_MOVIEDB_ID  +" INTEGER NOT NULL, " +
                        TrailerContract.TrailerEntry.COLUMN_TRAILER_ID  +" TEXT NOT NULL, " +
                        TrailerContract.TrailerEntry.COLUMN_NAME        +" TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_KEY         +" TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_TYPE        +" TEXT, " +
                        TrailerContract.TrailerEntry.COLUMN_SITE        +" TEXT) " ;

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_REVIEW);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TRAILER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
