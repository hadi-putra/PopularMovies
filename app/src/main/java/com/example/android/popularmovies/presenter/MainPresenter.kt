package com.example.android.popularmovies.presenter

import android.database.Cursor

import com.example.android.popularmovies.data.MovieRepository
import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.model.ResponseApi
import com.example.android.popularmovies.util.MovieApi
import com.example.android.popularmovies.util.Sort
import com.example.android.popularmovies.ui.view.MainView

import javax.inject.Inject

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by msk-1196 on 6/18/17.
 */

class MainPresenter @Inject
constructor(private val view: MainView, private val movieRepository: MovieRepository) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadMovie(selectedSort: Sort) {
        view.showLoading()
        compositeDisposable.clear()
        compositeDisposable.add(movieRepository.getSortedMovies(selectedSort)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieModels ->
                    view.hideLoading()
                    view.setMovies(movieModels)
                }) { throwable ->
                    view.hideLoading()
                    throwable.printStackTrace()
                })
    }

    fun clearSubscription() {
        view.hideLoading()
        compositeDisposable.dispose()
    }

    fun loadFavorite() {
        view.showLoading()
        compositeDisposable.clear()
        compositeDisposable.add(movieRepository.favorites
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieModels ->
                    view.hideLoading()
                    view.setMovies(movieModels)
                }) { throwable ->
                    view.hideLoading()
                    throwable.printStackTrace()
                })
    }

    fun showFavorite(data: Cursor) {
        view.setMovies(movieRepository.getMoviesFromCursor(data))
    }
}
