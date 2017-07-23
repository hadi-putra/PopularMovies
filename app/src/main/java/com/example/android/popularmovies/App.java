package com.example.android.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
import android.util.Log;

import com.example.android.popularmovies.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasContentProviderInjector;

/**
 * Created by msk-1196 on 6/24/17.
 */

public class App extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<App> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
