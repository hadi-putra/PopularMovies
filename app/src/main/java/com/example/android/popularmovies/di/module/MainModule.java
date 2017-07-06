package com.example.android.popularmovies.di.module;

import android.app.Activity;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.di.component.MainComponent;
import com.example.android.popularmovies.view.MainView;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by msk-1196 on 6/24/17.
 */

@Module(subcomponents = {MainComponent.class})
public abstract class MainModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
            bindMainActivityInjectorFactory(MainComponent.Builder builder);

    @Binds
    abstract MainView provideMainView(MainActivity activity);
}
