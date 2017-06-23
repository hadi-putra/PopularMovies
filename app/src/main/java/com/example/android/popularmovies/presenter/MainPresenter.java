package com.example.android.popularmovies.presenter;

import android.util.Log;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ResponseApi;
import com.example.android.popularmovies.util.MovieApi;
import com.example.android.popularmovies.util.Sort;
import com.example.android.popularmovies.view.MainView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by msk-1196 on 6/18/17.
 */

public class MainPresenter {
    private MainView view;
    private Sort selectedSort;
    private MovieApi movieApi;
    private CompositeDisposable compositeDisposable;

    public MainPresenter(MainView view) {
        this.view = view;
        selectedSort = Sort.POPULAR;

        compositeDisposable = new CompositeDisposable();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        movieApi = retrofit.create(MovieApi.class);
    }

    public void loadMovie(String key) {
        Observable<ResponseApi<MovieModel>> request = null;
        if (selectedSort == Sort.POPULAR){
            request = movieApi.getByPopular("popular", key);
        } else if (selectedSort == Sort.RATING){
            request = movieApi.getByPopular("top_rated", key);
        }

        if (request != null){

            compositeDisposable.add(request
                    .subscribeOn(Schedulers.computation())
                    .map(ResponseApi::getResults)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movieModels -> {
                        Log.e(this.getClass().getName(), "called");
                        view.setMovies(movieModels);
                    }, throwable -> throwable.printStackTrace()));
        }
    }

    public void clearSubscription() {
        compositeDisposable.dispose();
    }
}
