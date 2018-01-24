package com.example.android.popularmovies.di.module

import android.support.v4.app.Fragment

import com.example.android.popularmovies.ui.fragment.MovieGridFragment

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector

import dagger.android.ContributesAndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

/**
 * Created by msk-1196 on 7/14/17.
 */
@Module
abstract class MovieGridFragmentProvider {

    @ContributesAndroidInjector(modules = [MovieGridModule::class])
    internal abstract fun bindMovieGridFragment(): MovieGridFragment
}
