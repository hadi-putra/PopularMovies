package com.example.android.popularmovies.di.module

import com.example.android.popularmovies.data.MovieProvider

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by msk-1196 on 7/23/17.
 */
@Module
abstract class ContentProviderBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindMovieProvider(): MovieProvider
}
