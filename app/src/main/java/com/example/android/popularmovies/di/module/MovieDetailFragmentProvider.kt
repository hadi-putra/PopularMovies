package com.example.android.popularmovies.di.module

import com.example.android.popularmovies.ui.fragment.MovieDetailFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by msk-1196 on 7/15/17.
 */
@Module
abstract class MovieDetailFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(MovieDetailModule::class))
    internal abstract fun bindMovieDetailFragment(): MovieDetailFragment
}
