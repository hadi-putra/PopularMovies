package com.example.android.popularmovies.presenter;

import android.util.Log;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ResponseApi;
import com.example.android.popularmovies.model.TrailerModel;
import com.example.android.popularmovies.ui.view.MovieDetailView;
import com.example.android.popularmovies.util.MovieApi;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by msk-1196 on 7/6/17.
 */

public class MovieDetailPresenter {
    private MovieDetailView view;
    private final CompositeDisposable compositeDisposable;
    private final MovieApi movieApi;
    private MovieModel selectedMovie;

    @Inject
    public MovieDetailPresenter(MovieDetailView view, MovieApi movieApi) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        this.movieApi = movieApi;
    }

    public void setDetailForMovie(MovieModel movie) {
        this.selectedMovie = movie;
        view.showDetailMovie(movie);
    }

    public void loadTrailer() {
        compositeDisposable.add(movieApi.getTrailersByMovieId(selectedMovie.getId())
                .subscribeOn(Schedulers.newThread())
                .map(ResponseApi::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerModels -> {
                    Log.e("TRAILERS", trailerModels.toString());
                },throwable -> {

                }));
    }

    public void loadReview() {
        compositeDisposable.add(movieApi.getReviewByMovieId(selectedMovie.getId())
                .subscribeOn(Schedulers.newThread())
                .map(ResponseApi::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerModels -> {
                    Log.e("REVIEWS", trailerModels.toString());
                },throwable -> {

                }));
    }
}
