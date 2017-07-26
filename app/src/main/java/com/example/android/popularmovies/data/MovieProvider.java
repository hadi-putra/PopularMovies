package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.popularmovies.data.Contract.BaseContract;
import com.example.android.popularmovies.data.Contract.MovieContract;
import com.example.android.popularmovies.data.Contract.ReviewContract;
import com.example.android.popularmovies.data.Contract.TrailerContract;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by msk-1196 on 7/23/17.
 */

public class MovieProvider extends ContentProvider {
    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;
    public static final int CODE_MOVIE_REVIEW = 200;
    public static final int CODE_REVIEW_WITH_ID = 201;
    public static final int CODE_MOVIE_VIDEO = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = BaseContract.AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE+"/#", CODE_MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIE+"/#/"+ TrailerContract.PATH_TRAILER,
                CODE_MOVIE_VIDEO);
        matcher.addURI(authority, MovieContract.PATH_MOVIE+"/#/"+ ReviewContract.PATH_REVIEW,
                CODE_MOVIE_REVIEW);
        matcher.addURI(authority, ReviewContract.PATH_REVIEW+"/*", CODE_REVIEW_WITH_ID);
        return matcher;
    }

    @Inject MovieDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        AndroidInjection.inject(this);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                cursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                sqLiteDatabase.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME,
                        null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                return MovieContract.MovieEntry.buildMovieWithMovieDBId(
                        contentValues.getAsLong(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int totalRow;
        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                totalRow = sqLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (totalRow > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return totalRow;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                      @Nullable String[] strings) {
        return 0;
    }
}
