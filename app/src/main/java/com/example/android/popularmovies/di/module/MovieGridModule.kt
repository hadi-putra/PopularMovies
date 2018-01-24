package com.example.android.popularmovies.di.module

import com.example.android.popularmovies.ui.fragment.MovieGridFragment
import com.example.android.popularmovies.ui.view.MainView

import dagger.Binds
import dagger.Module

/**
 * Created by msk-1196 on 6/24/17.
 */

@Module
abstract class MovieGridModule {

    @Binds
    internal abstract fun provideMainView(fragment: MovieGridFragment): MainView
}
