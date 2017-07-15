package com.example.android.popularmovies.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.popularmovies.PosterAdapter;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.presenter.MainPresenter;
import com.example.android.popularmovies.ui.activity.MovieDetailActivity;
import com.example.android.popularmovies.ui.view.MainView;
import com.example.android.popularmovies.util.Sort;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static com.example.android.popularmovies.util.Sort.POPULAR;
import static com.example.android.popularmovies.util.Sort.RATING;

/**
 * Created by msk-1196 on 7/14/17.
 */

public class MovieGridFragment extends Fragment implements MainView, PosterAdapter.PosterAdapterOnClickHandler {
    private static final String GRID_STATE_KEY = "com.example.popularmovies.gridstate";
    private static final String SORT_STATE_KEY = "com.example.popularmovies.sortstate";
    private static final String SELECTED_MOVIE_KEY = "com.example.popularmovies.selectedmovie";

    @BindView(R.id.rv_grid_movie_poster) RecyclerView mGridPosterRecyclerView;
    @BindView(R.id.pb_loading_movies) ProgressBar mLoadingProgressBar;

    private PosterAdapter mPosterAdapter;
    @Inject MainPresenter mPresenter;
    private GridLayoutManager mGridLayoutManager;
    private Parcelable mStateGrid;
    private MovieGridFragmentListener mListener;
    private Sort selectedSort = Sort.POPULAR;
    private MovieModel selectedMovie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mGridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_item),
                GridLayoutManager.VERTICAL, false);
        mGridPosterRecyclerView.setLayoutManager(mGridLayoutManager);
        mGridPosterRecyclerView.setHasFixedSize(true);
        mPosterAdapter = new PosterAdapter(this);
        mGridPosterRecyclerView.setAdapter(mPosterAdapter);

        if (savedInstanceState != null) {
            mStateGrid = savedInstanceState.getParcelable(GRID_STATE_KEY);
            selectedSort = (Sort) savedInstanceState.get(SORT_STATE_KEY);
            selectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY);

            getActivity().setTitle(selectedSort == Sort.POPULAR? R.string.app_name :
                    R.string.top_rated_title);
        }

        mPresenter.loadMovie(selectedSort);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof MovieGridFragmentListener){
            mListener = (MovieGridFragmentListener) context;
        } else {
            throw new IllegalArgumentException("Activity has to implement MovieGridFragmentListener!");
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.clearSubscription();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mStateGrid = mGridLayoutManager.onSaveInstanceState();
        outState.putSerializable(SORT_STATE_KEY, selectedSort);
        outState.putParcelable(GRID_STATE_KEY, mStateGrid);
        outState.putParcelable(SELECTED_MOVIE_KEY, selectedMovie);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        switch (selectedSort){
            case RATING:
                menu.findItem(R.id.action_top_rated).setChecked(true);
                break;
            case POPULAR:
                menu.findItem(R.id.action_popular).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_popular:
                if (selectedSort != POPULAR) {
                    item.setChecked(true);
                    onSortOptionChanged(POPULAR);
                }
                return true;
            case R.id.action_top_rated:
                if (selectedSort != RATING) {
                    item.setChecked(true);
                    onSortOptionChanged(RATING);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSortOptionChanged(Sort option) {
        selectedMovie = null;
        mStateGrid = null;
        getActivity().setTitle(option == Sort.POPULAR? R.string.app_name : R.string.top_rated_title);
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
        if (selectedMovie == null && movieModels.size() > 0){
            selectedMovie = movieModels.get(0);
        }
        if (mListener.isTwoPaneMode())
            mListener.setSelectedMovie(selectedMovie);
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
    public void onClick(MovieModel movie) {
        selectedMovie = movie;
        if (mListener.isTwoPaneMode()){
            mListener.setSelectedMovie(movie);
        } else {
            Intent destinationIntent = new Intent(getContext(), MovieDetailActivity.class);
            destinationIntent.putExtra("content", movie);
            startActivity(destinationIntent);
        }
    }


    public interface MovieGridFragmentListener {
        boolean isTwoPaneMode();
        void setSelectedMovie(MovieModel movie);
    }
}
