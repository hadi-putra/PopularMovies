package com.example.android.popularmovies.data;

import android.net.Uri;

/**
 * Created by msk-1196 on 7/13/17.
 */

public abstract class BaseContract {
    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
