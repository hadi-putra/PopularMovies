package com.example.android.popularmovies.di.module

import android.content.ContentResolver

import com.example.android.popularmovies.App
import com.example.android.popularmovies.data.MovieDbHelper
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.SqlBrite

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers

/**
 * Created by msk-1196 on 7/23/17.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideDbHelper(application: App): MovieDbHelper {
        return MovieDbHelper(application)
    }

    @Provides
    @Singleton
    internal fun provideSqlBrite(): SqlBrite {
        return SqlBrite.Builder().build()
    }

    @Provides
    @Singleton
    internal fun provideContentResolver(application: App): ContentResolver {
        return application.contentResolver
    }

    @Provides
    @Singleton
    internal fun provideBriteContentResolver(sqlBrite: SqlBrite, contentResolver: ContentResolver): BriteContentResolver {
        return sqlBrite.wrapContentProvider(contentResolver, Schedulers.io())
    }
}
