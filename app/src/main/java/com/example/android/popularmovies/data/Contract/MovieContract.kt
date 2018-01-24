package com.example.android.popularmovies.data.Contract

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by msk-1196 on 7/13/17.
 */

class MovieContract : BaseContract() {

    class MovieEntry {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build()

            val TABLE_NAME = "movie"

            val _ID = "_id"
            val COLUMN_MOVIEDB_ID = "moviedb_id"
            val COLUMN_TITLE = "title"
            val COLUMN_OVERVIEW = "overview"
            val COLUMN_BACKDROP = "backdrop"
            val COLUMN_POSTER = "poster"
            val COLUMN_VOTE_AVG = "vote_avg"
            val COLUMN_VOTE_COUNT = "vote_count"
            val COLUMN_POPULAITY = "popularity"
            val COLUMN_RELEASE_DATE = "release_date"
            val COLUMN_IS_FAVORITE = "is_favorite"


            fun buildMovieWithMovieDBId(movieDBId: Long): Uri {
                return CONTENT_URI.buildUpon().appendPath(java.lang.Long.toString(movieDBId)).build()
            }

            fun buildTrailerUri(movieDBId: Long): Uri {
                return CONTENT_URI.buildUpon().appendPath(java.lang.Long.toString(movieDBId))
                        .appendPath(TrailerContract.PATH_TRAILER).build()
            }

            fun buildReviewrUri(movieDBId: Long): Uri {
                return CONTENT_URI.buildUpon().appendPath(java.lang.Long.toString(movieDBId))
                        .appendPath(ReviewContract.PATH_REVIEW).build()
            }
        }


    }

    companion object {
        const val PATH_MOVIE = "movie"
    }
}
