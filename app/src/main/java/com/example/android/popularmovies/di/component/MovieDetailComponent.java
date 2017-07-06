package com.example.android.popularmovies.di.component;

import com.example.android.popularmovies.MovieDetailActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by msk-1196 on 7/6/17.
 */
@Subcomponent
public interface MovieDetailComponent extends AndroidInjector<MovieDetailActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MovieDetailActivity>{}
}
