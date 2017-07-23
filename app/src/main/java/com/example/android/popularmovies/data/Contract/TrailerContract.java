package com.example.android.popularmovies.data.Contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by msk-1196 on 7/16/17.
 */

public class TrailerContract extends BaseContract {
    public static final String PATH_TRAILER = "movie_trailer";

    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String TABLE_NAME = "movie_trailer";

        public static final String COLUMN_MOVIEDB_ID = "moviedb_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_TYPE = "type";
    }
}
