package com.example.android.popularmovies.di.module;

import android.app.Activity;

import com.example.android.popularmovies.MovieDetailActivity;
import com.example.android.popularmovies.di.component.MovieDetailComponent;
import com.example.android.popularmovies.view.MovieDetailView;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by msk-1196 on 7/6/17.
 */
@Module(subcomponents = {MovieDetailComponent.class})
public abstract class MovieDetailModule {
    @Binds
    @IntoMap
    @ActivityKey(MovieDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
        bindMovieDetailActivityInjectorFactory(MovieDetailComponent.Builder builder);

    @Binds
    abstract MovieDetailView provideMovieDetailView(MovieDetailActivity activity);
}
