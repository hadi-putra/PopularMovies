package com.example.android.popularmovies.di.component;

import com.example.android.popularmovies.App;
import com.example.android.popularmovies.di.module.ActivityBuilder;
import com.example.android.popularmovies.di.module.AppModule;
import com.example.android.popularmovies.di.module.ContentProviderBuilder;
import com.example.android.popularmovies.di.module.DataModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Created by msk-1196 on 6/24/17.
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class,
        AndroidInjectionModule.class, ActivityBuilder.class, ContentProviderBuilder.class})
public interface AppComponent extends AndroidInjector<App> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App>{}
}
