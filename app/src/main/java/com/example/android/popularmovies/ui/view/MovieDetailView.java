package com.example.android.popularmovies.ui.view;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ReviewModel;
import com.example.android.popularmovies.model.TrailerModel;

import java.util.List;

/**
 * Created by msk-1196 on 7/6/17.
 */

public interface MovieDetailView {
    void showDetailMovie(MovieModel movie);

    void showTrailer(List<TrailerModel> trailers);

    void showReview(List<ReviewModel> reviews);

    void toggleFavoriteFab(boolean favorite);
}
