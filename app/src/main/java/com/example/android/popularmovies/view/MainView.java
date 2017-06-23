package com.example.android.popularmovies.view;

import com.example.android.popularmovies.model.MovieModel;

import java.util.List;

/**
 * Created by msk-1196 on 6/18/17.
 */

public interface MainView {
    void setMovies(List<MovieModel> movieModels);
}
