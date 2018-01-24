package com.example.android.popularmovies.ui.view

import com.example.android.popularmovies.model.MovieModel

/**
 * Created by msk-1196 on 6/18/17.
 */

interface MainView {
    fun setMovies(movieModels: List<MovieModel>)

    fun showLoading()

    fun hideLoading()
}
