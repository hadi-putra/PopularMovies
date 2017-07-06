package com.example.android.popularmovies.di.component;

import com.example.android.popularmovies.App;
import com.example.android.popularmovies.di.module.AppModule;
import com.example.android.popularmovies.di.module.MainModule;
import com.example.android.popularmovies.di.module.MovieDetailModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by msk-1196 on 6/24/17.
 */
@Singleton
@Component(modules = {AppModule.class, AndroidInjectionModule.class, MainModule.class, MovieDetailModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance Builder application(App application);
        AppComponent build();
    }
    void inject(App app);
}
