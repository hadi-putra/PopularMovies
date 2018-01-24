package com.example.android.popularmovies.util

import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.model.ResponseApi
import com.example.android.popularmovies.model.ReviewModel
import com.example.android.popularmovies.model.TrailerModel

import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by msk-1196 on 6/18/17.
 */

interface MovieApi {

    @GET("movie/{sort}")
    fun getMovies(@Path("sort") sort: String): Observable<ResponseApi<MovieModel>>

    @GET("movie/{id}/reviews")
    fun getReviewByMovieId(@Path("id") id: Long): Flowable<ResponseApi<ReviewModel>>

    @GET("movie/{id}/videos")
    fun getTrailersByMovieId(@Path("id") id: Long): Flowable<ResponseApi<TrailerModel>>
}
