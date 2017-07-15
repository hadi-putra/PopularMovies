package com.example.android.popularmovies.di.module;

import com.example.android.popularmovies.ui.activity.MainActivity;
import com.example.android.popularmovies.ui.activity.MovieDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by msk-1196 on 7/14/17.
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MovieGridFragmentProvider.class, MovieDetailFragmentProvider.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = MovieDetailFragmentProvider.class)
    abstract MovieDetailActivity bindMovieDetailActivity();
}
