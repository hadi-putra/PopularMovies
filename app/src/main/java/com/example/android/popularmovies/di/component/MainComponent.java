package com.example.android.popularmovies.di.component;

import com.example.android.popularmovies.MainActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by msk-1196 on 6/24/17.
 */

@Subcomponent
public interface MainComponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}
}
