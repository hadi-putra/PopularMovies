package com.example.android.popularmovies.util;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ResponseApi;
import com.example.android.popularmovies.model.ReviewModel;
import com.example.android.popularmovies.model.TrailerModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by msk-1196 on 6/18/17.
 */

public interface MovieApi {

    @GET("movie/{sort}")
    Observable<ResponseApi<MovieModel>> getMovies(@Path("sort") String sort);

    @GET("movie/{id}/reviews")
    Flowable<ResponseApi<ReviewModel>> getReviewByMovieId(@Path("id") long id);

    @GET("movie/{id}/videos")
    Flowable<ResponseApi<TrailerModel>> getTrailersByMovieId(@Path("id") long id);
}
