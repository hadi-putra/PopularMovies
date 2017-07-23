package com.example.android.popularmovies.data.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by msk-1196 on 7/16/17.
 */

public class ReviewContract extends BaseContract {
    public static final String PATH_REVIEW = "movie_review";

    public static final class RevieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String TABLE_NAME = "movie_review";

        public static final String COLUMN_MOVIEDB_ID = "moviedb_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_REVIEW_ID = "review_id";
    }
}
