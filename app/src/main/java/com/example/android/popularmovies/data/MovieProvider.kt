package com.example.android.popularmovies.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

import com.example.android.popularmovies.data.Contract.BaseContract
import com.example.android.popularmovies.data.Contract.MovieContract
import com.example.android.popularmovies.data.Contract.ReviewContract
import com.example.android.popularmovies.data.Contract.TrailerContract

import javax.inject.Inject

import dagger.android.AndroidInjection

/**
 * Created by msk-1196 on 7/23/17.
 */

class MovieProvider : ContentProvider() {

    @Inject lateinit var dbHelper: MovieDbHelper

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor: Cursor?
        val sqLiteDatabase = dbHelper!!.readableDatabase

        when (sUriMatcher.match(uri)) {
            CODE_MOVIE -> cursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME, projection,
                    selection, selectionArgs, null, null, sortOrder)
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
        cursor?.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val sqLiteDatabase = dbHelper!!.writableDatabase
        when (sUriMatcher.match(uri)) {
            CODE_MOVIE -> {
                sqLiteDatabase.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, contentValues)
                context!!.contentResolver.notifyChange(uri, null)
                return MovieContract.MovieEntry.buildMovieWithMovieDBId(
                        contentValues!!.getAsLong(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID)!!)
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val sqLiteDatabase = dbHelper!!.writableDatabase
        val totalRow: Int
        when (sUriMatcher.match(uri)) {
            CODE_MOVIE -> totalRow = sqLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,
                    selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        if (totalRow > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return totalRow
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?,
                        strings: Array<String>?): Int {
        return 0
    }

    companion object {
        val CODE_MOVIE = 100
        val CODE_MOVIE_WITH_ID = 101
        val CODE_MOVIE_REVIEW = 200
        val CODE_REVIEW_WITH_ID = 201
        val CODE_MOVIE_VIDEO = 300

        private val sUriMatcher = buildUriMatcher()

        private fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = BaseContract.AUTHORITY
            matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE)
            matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID)
            matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/" + TrailerContract.PATH_TRAILER,
                    CODE_MOVIE_VIDEO)
            matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/" + ReviewContract.PATH_REVIEW,
                    CODE_MOVIE_REVIEW)
            matcher.addURI(authority, ReviewContract.PATH_REVIEW + "/*", CODE_REVIEW_WITH_ID)
            return matcher
        }
    }
}
