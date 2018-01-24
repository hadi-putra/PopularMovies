package com.example.android.popularmovies.di.module

import android.content.ContentResolver
import android.content.Context

import com.example.android.popularmovies.App
import com.example.android.popularmovies.BuildConfig
import com.example.android.popularmovies.data.MovieRepository
import com.example.android.popularmovies.util.MovieApi
import com.squareup.sqlbrite2.BriteContentResolver

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by msk-1196 on 6/24/17.
 */

@Module
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
        val okHttp = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()

        val addKeyInterceptor = Interceptor { chain ->
            var request = chain.request()
            val httpUrl = request.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.THEMOVIEDB_KEY).build()
            request = request.newBuilder().url(httpUrl).build()
            chain.proceed(request)
        }

        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttp.addInterceptor(interceptor)
        if (!okHttp.interceptors().contains(addKeyInterceptor))
            okHttp.addInterceptor(addKeyInterceptor)

        return okHttp
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(okHttpBuilder: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    internal fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideMovieRepository(movieApi: MovieApi, contentResolver: ContentResolver,
                                        briteContentResolver: BriteContentResolver): MovieRepository {
        return MovieRepository(movieApi, briteContentResolver, contentResolver)
    }

    companion object {
        private const val BASE_URL = "http://api.themoviedb.org/3/"
    }

}
