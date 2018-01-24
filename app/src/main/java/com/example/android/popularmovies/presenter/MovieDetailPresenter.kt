package com.example.android.popularmovies.presenter

import android.os.Parcelable
import android.util.Log

import com.example.android.popularmovies.data.MovieRepository
import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.model.ResponseApi
import com.example.android.popularmovies.model.ReviewModel
import com.example.android.popularmovies.model.TrailerModel
import com.example.android.popularmovies.ui.view.MovieDetailView
import com.example.android.popularmovies.util.MovieApi

import java.util.ArrayList

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by msk-1196 on 7/6/17.
 */

class MovieDetailPresenter @Inject
constructor(private val view: MovieDetailView, private val movieRepository: MovieRepository) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var selectedMovie: MovieModel? = null

    var videos: List<TrailerModel>? = null
        private set
    var reviews: List<ReviewModel>? = null
        private set

    fun setDetailForMovie(movie: MovieModel) {
        this.selectedMovie = movie
        view.showDetailMovie(movie)
    }

    fun loadTrailer() {
        if (selectedMovie == null) return

        if (videos != null) {
            view.showTrailer(videos!!)
            return
        }
        compositeDisposable.add(movieRepository.getMovieVideos(selectedMovie!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ trailerModels ->
                    this.videos = trailerModels
                    view.showTrailer(trailerModels)
                }) { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun loadReview() {
        if (selectedMovie == null) return

        if (reviews != null) {
            view.showReview(reviews!!)
            return
        }
        compositeDisposable.add(movieRepository.getMovieReviews(selectedMovie!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ reviews ->
                    this.reviews = reviews
                    view.showReview(reviews)
                }) { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun toggleFavorite() {
        selectedMovie!!.isFavorite = !selectedMovie!!.isFavorite
        movieRepository.toggleFavorite(selectedMovie!!)
        view.toggleFavoriteFab(selectedMovie!!.isFavorite)
    }

    fun setVideos(videos: ArrayList<TrailerModel>) {
        this.videos = videos
    }

    fun setReviews(reviews: ArrayList<ReviewModel>) {
        this.reviews = reviews
    }

    fun checkFavorite() {
        compositeDisposable.add(movieRepository.favoriteIds
                .map { ids -> ids.contains(selectedMovie!!.id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ favorite ->
                    selectedMovie!!.isFavorite = favorite
                    view.toggleFavoriteFab(favorite)
                }) { throwable -> throwable.printStackTrace()})
    }
}
