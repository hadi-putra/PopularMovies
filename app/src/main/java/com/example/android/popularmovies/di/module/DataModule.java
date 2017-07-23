package com.example.android.popularmovies.di.module;

import android.content.ContentResolver;

import com.example.android.popularmovies.App;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.squareup.sqlbrite2.BriteContentResolver;
import com.squareup.sqlbrite2.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by msk-1196 on 7/23/17.
 */
@Module
public class DataModule {

    @Provides
    @Singleton
    MovieDbHelper provideDbHelper(App application){
        return new MovieDbHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite(){
        return new SqlBrite.Builder().build();
    }

    @Provides
    @Singleton
    ContentResolver provideContentResolver(App application){
        return application.getContentResolver();
    }

    @Provides
    @Singleton
    BriteContentResolver provideBriteContentResolver(SqlBrite sqlBrite, ContentResolver contentResolver){
        return sqlBrite.wrapContentProvider(contentResolver, Schedulers.io());
    }
}
