package com.example.android.popularmovies.ui.view

import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.model.ReviewModel
import com.example.android.popularmovies.model.TrailerModel

/**
 * Created by msk-1196 on 7/6/17.
 */

interface MovieDetailView {
    fun showDetailMovie(movie: MovieModel)

    fun showTrailer(trailers: List<TrailerModel>)

    fun showReview(reviews: List<ReviewModel>)

    fun toggleFavoriteFab(favorite: Boolean)
}
