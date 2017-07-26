package com.example.android.popularmovies.presenter;

import android.os.Parcelable;
import android.util.Log;

import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ResponseApi;
import com.example.android.popularmovies.model.ReviewModel;
import com.example.android.popularmovies.model.TrailerModel;
import com.example.android.popularmovies.ui.view.MovieDetailView;
import com.example.android.popularmovies.util.MovieApi;

import java.util.ArrayList;
import java.util.List;

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

    private List<TrailerModel> videos;
    private List<ReviewModel> reviews;

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
        if (selectedMovie == null) return;

        if (videos != null){
            view.showTrailer(videos);
            return;
        }
        compositeDisposable.add(movieRepository.getMovieVideos(selectedMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerModels -> {
                    this.videos = trailerModels;
                    view.showTrailer(trailerModels);
                },throwable -> {

                }));
    }

    public void loadReview() {
        if (selectedMovie == null) return;

        if (reviews != null){
            view.showReview(reviews);
            return;
        }
        compositeDisposable.add(movieRepository.getMovieReviews(selectedMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviews -> {
                    this.reviews = reviews;
                    view.showReview(reviews);
                },throwable -> {

                }));
    }

    public void toggleFavorite() {
        selectedMovie.setFavorite(!selectedMovie.isFavorite());
        movieRepository.toggleFavorite(selectedMovie);
        view.toggleFavoriteFab(selectedMovie.isFavorite());
    }

    public List<TrailerModel> getVideos() {
        return videos;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setVideos(ArrayList<TrailerModel> videos) {
        this.videos = videos;
    }

    public void setReviews(ArrayList<ReviewModel> reviews) {
        this.reviews = reviews;
    }

    public void checkFavorite() {
        compositeDisposable.add(movieRepository.getFavoriteIds()
                .map(ids -> ids.contains(selectedMovie.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favorite -> {
                    selectedMovie.setFavorite(favorite);
                    view.toggleFavoriteFab(favorite);
                }, throwable -> {}));
    }
}
