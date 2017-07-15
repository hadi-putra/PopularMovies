package com.example.android.popularmovies.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.presenter.MovieDetailPresenter;
import com.example.android.popularmovies.ui.view.MovieDetailView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by msk-1196 on 7/15/17.
 */

public class MovieDetailFragment extends Fragment implements MovieDetailView {
    public static final String CONTENT_KEY = "content.movie";
    @BindView(R.id.backdrop) ImageView mBackdropImageView;
    @BindView(R.id.movie_poster) ImageView mPosterImageView;
    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.release_date) TextView mReleaseDateTextView;
    @BindView(R.id.rate) TextView mRatingTextView;
    @BindView(R.id.overview) TextView mOverviewTextView;

    @Inject MovieDetailPresenter mPresenter;

    public static MovieDetailFragment newInstance(MovieModel movie){
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(CONTENT_KEY, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null){
            mPresenter.setDetailForMovie(args.getParcelable(CONTENT_KEY));
            mPresenter.loadTrailer();
            mPresenter.loadReview();
        }
    }

    @Override
    public void showDetailMovie(MovieModel movie) {
        getActivity().setTitle(movie.getTitle());
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mRatingTextView.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());

        if (movie.getBackdropPath() != null){
            Picasso.with(getActivity())
                    .load(movie.getBackdropPath())
                    .into(mBackdropImageView);
        }

        if (movie.getPosterPath() != null){
            Picasso.with(getActivity())
                    .load(movie.getPosterPath())
                    .into(mPosterImageView);
        }
    }

}
