package com.example.android.popularmovies.presenter;

import android.database.Cursor;

import com.example.android.popularmovies.data.MovieRepository;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ResponseApi;
import com.example.android.popularmovies.util.MovieApi;
import com.example.android.popularmovies.util.Sort;
import com.example.android.popularmovies.ui.view.MainView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by msk-1196 on 6/18/17.
 */

public class MainPresenter {

    private MainView view;
    private MovieRepository movieRepository;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public MainPresenter(MainView view, MovieRepository movieRepository) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        this.movieRepository = movieRepository;
    }

    public void loadMovie(Sort selectedSort) {
        view.showLoading();
        compositeDisposable.clear();
        compositeDisposable.add(movieRepository.getSortedMovies(selectedSort)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(movieModels -> {
                            view.hideLoading();
                            view.setMovies(movieModels);
                        }, throwable ->{
                            view.hideLoading();
                            throwable.printStackTrace();
                        }));
    }

    public void clearSubscription() {
        view.hideLoading();
        compositeDisposable.dispose();
    }

    public void loadFavorite() {
        view.showLoading();
        compositeDisposable.clear();
        compositeDisposable.add(movieRepository.getFavorites()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieModels -> {
                    view.hideLoading();
                    view.setMovies(movieModels);
                }, throwable ->{
                    view.hideLoading();
                    throwable.printStackTrace();
                }));
    }

    public void showFavorite(Cursor data) {
        view.setMovies(movieRepository.getMoviesFromCursor(data));
    }
}
