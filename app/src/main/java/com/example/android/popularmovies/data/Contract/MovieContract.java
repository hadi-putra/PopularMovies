package com.example.android.popularmovies.data.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by msk-1196 on 7/13/17.
 */

public class MovieContract extends BaseContract {
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIEDB_ID = "moviedb_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE_AVG = "vote_avg";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POPULAITY = "popularity";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";


        public static Uri buildMovieWithMovieDBId(long movieDBId){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movieDBId)).build();
        }

        public static Uri buildTrailerUri(long movieDBId){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movieDBId))
                    .appendPath(TrailerContract.PATH_TRAILER).build();
        }

        public static Uri buildReviewrUri(long movieDBId){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movieDBId))
                    .appendPath(ReviewContract.PATH_REVIEW).build();
        }


    }
}
