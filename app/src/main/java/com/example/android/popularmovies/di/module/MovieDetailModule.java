package com.example.android.popularmovies.di.module;

import com.example.android.popularmovies.ui.activity.MovieDetailActivity;
import com.example.android.popularmovies.ui.fragment.MovieDetailFragment;
import com.example.android.popularmovies.ui.view.MovieDetailView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by msk-1196 on 7/6/17.
 */
@Module
public abstract class MovieDetailModule {
    @Binds
    abstract MovieDetailView provideMovieDetailView(MovieDetailFragment fragment);
}
