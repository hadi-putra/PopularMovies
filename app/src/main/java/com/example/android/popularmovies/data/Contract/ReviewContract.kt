package com.example.android.popularmovies.data.Contract

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by msk-1196 on 7/16/17.
 */

class ReviewContract : BaseContract() {

    class RevieEntry {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build()

            val TABLE_NAME = "movie_review"

            val _ID = "_id"
            val COLUMN_MOVIEDB_ID = "moviedb_id"
            val COLUMN_AUTHOR = "author"
            val COLUMN_CONTENT = "content"
            val COLUMN_URL = "url"
            val COLUMN_REVIEW_ID = "review_id"
        }
    }

    companion object {
        const val PATH_REVIEW = "movie_review"
    }
}
