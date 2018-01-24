package com.example.android.popularmovies.data.Contract

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by msk-1196 on 7/16/17.
 */

class TrailerContract : BaseContract() {

    class TrailerEntry {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build()

            val TABLE_NAME = "movie_trailer"

            val _ID = "_id"
            val COLUMN_MOVIEDB_ID = "moviedb_id"
            val COLUMN_NAME = "name"
            val COLUMN_TRAILER_ID = "trailer_id"
            val COLUMN_KEY = "key"
            val COLUMN_SITE = "site"
            val COLUMN_TYPE = "type"
        }
    }

    companion object {
        const val PATH_TRAILER = "movie_trailer"
    }
}
