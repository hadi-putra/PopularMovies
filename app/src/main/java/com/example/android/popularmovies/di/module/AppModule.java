package com.example.android.popularmovies.di.module;

import android.content.Context;

import com.example.android.popularmovies.App;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.util.MovieApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by msk-1196 on 6/24/17.
 */

@Module
public class AppModule {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    @Singleton
    @Provides
    Context provideContext(App application){
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder(Context context){
        OkHttpClient.Builder okHttp = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        Interceptor addKeyInterceptor = (chain -> {
            Request request = chain.request();
            HttpUrl httpUrl = request.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.THEMOVIEDB_KEY).build();
            request = request.newBuilder().url(httpUrl).build();
            return chain.proceed(request);
        });

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttp.addInterceptor(interceptor);
        if (!okHttp.interceptors().contains(addKeyInterceptor))
            okHttp.addInterceptor(addKeyInterceptor);

        return okHttp;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient.Builder okHttpBuilder){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    MovieApi provideMovieApi(Retrofit retrofit){
        return retrofit.create(MovieApi.class);
    }

}
