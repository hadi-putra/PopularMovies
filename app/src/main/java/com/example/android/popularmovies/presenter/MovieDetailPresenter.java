package com.example.android.popularmovies.presenter;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.view.MovieDetailView;

import javax.inject.Inject;

/**
 * Created by msk-1196 on 7/6/17.
 */

public class MovieDetailPresenter {
    private MovieDetailView view;

    @Inject
    public MovieDetailPresenter(MovieDetailView view) {
        this.view = view;
    }

    public void setDetailForMovie(MovieModel movie) {
        view.showDetailMovie(movie);
    }
}
