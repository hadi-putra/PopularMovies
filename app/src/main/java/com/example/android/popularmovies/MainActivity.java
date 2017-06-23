package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.presenter.MainPresenter;
import com.example.android.popularmovies.view.MainView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{
    @BindView(R.id.rv_grid_movie_poster) RecyclerView mGridPosterRecyclerView;
    private PosterAdapter mPosterAdapter;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mGridPosterRecyclerView.setLayoutManager(layoutManager);
        mGridPosterRecyclerView.setHasFixedSize(true);
        mPosterAdapter = new PosterAdapter();
        mGridPosterRecyclerView.setAdapter(mPosterAdapter);
    }

    @Override
    public void setMovies(List<MovieModel> movieModels) {
        Log.e(this.getClass().getName(), movieModels.size()+"");
        mPosterAdapter.setMovies(movieModels);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = new MainPresenter(this);
        mPresenter.loadMovie(BuildConfig.THEMOVIEDB_KEY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.clearSubscription();
    }
}
