package com.example.android.popularmovies.model

import com.squareup.moshi.Json

import java.util.ArrayList

/**
 * Created by msk-1196 on 6/19/17.
 */

data class ResponseApi<E>(
    var page: Int = 0,
    @Json(name = "total_results")
    var totalResults: Int = 0,
    @Json(name = "total_pages")
    var totalPages: Int = 0,
    var results: List<E> = ArrayList()
)
