package com.example.android.popularmovies.presenter;

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
    private static final String POPULAR_QUERY = "popular";
    private static final String TOP_RATED_QUERY = "top_rated";

    private MainView view;
    private MovieApi movieApi;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public MainPresenter(MainView view, MovieApi movieApi) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        this.movieApi = movieApi;
    }

    public void loadMovie(Sort selectedSort) {
        view.showLoading();
        Observable<ResponseApi<MovieModel>> request =
                movieApi.getMovies(selectedSort == Sort.RATING? TOP_RATED_QUERY : POPULAR_QUERY);
        if (request != null){
            compositeDisposable.add(request
                    .subscribeOn(Schedulers.newThread())
                    .map(ResponseApi::getResults)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movieModels -> {
                        view.hideLoading();
                        view.setMovies(movieModels);
                    }, throwable ->{
                        view.hideLoading();
                        throwable.printStackTrace();
                    }));
        }
    }

    public void clearSubscription() {
        view.hideLoading();
        compositeDisposable.dispose();
    }
}
