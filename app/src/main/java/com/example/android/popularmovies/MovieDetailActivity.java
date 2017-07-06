package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.presenter.MovieDetailPresenter;
import com.example.android.popularmovies.view.MovieDetailView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailView {
    @BindView(R.id.backdrop) ImageView mBackdropImageView;
    @BindView(R.id.movie_poster) ImageView mPosterImageView;
    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.release_date) TextView mReleaseDateTextView;
    @BindView(R.id.rate) TextView mRatingTextView;
    @BindView(R.id.overview) TextView mOverviewTextView;

    @Inject MovieDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra("content")){
            mPresenter.setDetailForMovie(intent.getParcelableExtra("content"));
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showDetailMovie(MovieModel movie) {
        setTitle(movie.getTitle());
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mRatingTextView.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());

        if (movie.getBackdropPath() != null){
            Picasso.with(this)
                    .load(movie.getBackdropPath())
                    .into(mBackdropImageView);
        }

        if (movie.getPosterPath() != null){
            Picasso.with(this)
                    .load(movie.getPosterPath())
                    .into(mPosterImageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
