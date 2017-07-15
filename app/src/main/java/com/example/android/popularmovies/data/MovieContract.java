package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by msk-1196 on 7/13/17.
 */

public class MovieContract extends BaseContract {
    public static final String PATH_TASKS = "movie";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIEDB_ID = "moviedb_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
    }
}
