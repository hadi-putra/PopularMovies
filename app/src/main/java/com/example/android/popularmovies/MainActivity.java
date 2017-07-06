package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.presenter.MainPresenter;
import com.example.android.popularmovies.util.Sort;
import com.example.android.popularmovies.view.MainView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements MainView,
        PosterAdapter.PosterAdapterOnClickHandler {
    private static final String GRID_STATE_KEY = "com.example.popularmovies.gridstate";
    private static final String SORT_STATE_KEY = "com.example.popularmovies.sortstate";

    @BindView(R.id.rv_grid_movie_poster) RecyclerView mGridPosterRecyclerView;
    @BindView(R.id.pb_loading_movies) ProgressBar mLoadingProgressBar;

    private PosterAdapter mPosterAdapter;
    @Inject MainPresenter mPresenter;
    private GridLayoutManager mGridLayoutManager;
    private Parcelable mStateGrid;
    private Sort selectedSort = Sort.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_item),
                GridLayoutManager.VERTICAL, false);
        mGridPosterRecyclerView.setLayoutManager(mGridLayoutManager);
        mGridPosterRecyclerView.setHasFixedSize(true);
        mPosterAdapter = new PosterAdapter(this);
        mGridPosterRecyclerView.setAdapter(mPosterAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (selectedSort){
            case RATING:
                menu.findItem(R.id.action_top_rated).setChecked(true);
                break;
            case POPULAR:
                menu.findItem(R.id.action_popular).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_popular:
                if (selectedSort != Sort.POPULAR) {
                    item.setChecked(true);
                    onSortOptionChanged(Sort.POPULAR);
                }
                return true;
            case R.id.action_top_rated:
                if (selectedSort != Sort.RATING) {
                    item.setChecked(true);
                    onSortOptionChanged(Sort.RATING);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSortOptionChanged(Sort option) {
        mStateGrid = null;
        setTitle(option == Sort.POPULAR? R.string.app_name : R.string.top_rated_title);
        selectedSort = option;
        mPresenter.loadMovie(option);
    }

    @Override
    public void setMovies(List<MovieModel> movieModels) {
        mPosterAdapter.setMovies(movieModels);
        if (mStateGrid != null)
            mGridLayoutManager.onRestoreInstanceState(mStateGrid);
        else
            mGridLayoutManager.scrollToPositionWithOffset(0,0);
    }

    @Override
    public void showLoading() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mGridPosterRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mGridPosterRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setTitle(selectedSort == Sort.RATING? R.string.top_rated_title : R.string.app_name);
        mPresenter.loadMovie(selectedSort);
    }

    @Override
    protected void onDestroy() {
        mPresenter.clearSubscription();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mStateGrid = mGridLayoutManager.onSaveInstanceState();
        outState.putSerializable(SORT_STATE_KEY, selectedSort);
        outState.putParcelable(GRID_STATE_KEY, mStateGrid);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStateGrid = savedInstanceState.getParcelable(GRID_STATE_KEY);
            selectedSort = (Sort)savedInstanceState.get(SORT_STATE_KEY);
        }
    }

    @Override
    public void onClick(MovieModel movie) {
        Intent destinationIntent = new Intent(this, MovieDetailActivity.class);
        destinationIntent.putExtra("content", movie);
        startActivity(destinationIntent);
    }
}
