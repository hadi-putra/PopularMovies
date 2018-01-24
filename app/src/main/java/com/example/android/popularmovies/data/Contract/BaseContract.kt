package com.example.android.popularmovies.data.Contract

import android.net.Uri

/**
 * Created by msk-1196 on 7/13/17.
 */

abstract class BaseContract {
    companion object {
        const val AUTHORITY = "com.example.android.popularmovies"
        val BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY)
    }
}
