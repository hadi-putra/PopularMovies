package com.example.android.popularmovies

import com.example.android.popularmovies.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by msk-1196 on 6/24/17.
 */

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<App> {
        return DaggerAppComponent.builder().create(this)
    }
}
