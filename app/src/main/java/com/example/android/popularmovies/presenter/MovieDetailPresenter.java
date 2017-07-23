package com.example.android.popularmovies.presenter;

import android.util.Log;

import com.example.android.popularmovies.data.MovieRepository;
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
    private final MovieRepository movieRepository;
    private MovieModel selectedMovie;

    @Inject
    public MovieDetailPresenter(MovieDetailView view, MovieRepository movieRepository) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        this.movieRepository = movieRepository;
    }

    public void setDetailForMovie(MovieModel movie) {
        this.selectedMovie = movie;
        view.showDetailMovie(movie);
    }

    public void loadTrailer() {
        compositeDisposable.add(movieRepository.getMovieVideos(selectedMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerModels -> {
                    Log.e("TRAILERS", trailerModels.toString());
                },throwable -> {

                }));
    }

    public void loadReview() {
        compositeDisposable.add(movieRepository.getMovieReviews(selectedMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerModels -> {
                    Log.e("REVIEWS", trailerModels.toString());
                },throwable -> {

                }));
    }

    public void toggleFavorite(boolean isFavorite) {
        selectedMovie.setFavorite(isFavorite);
        movieRepository.toggleFavorite(isFavorite, selectedMovie);
    }
}
