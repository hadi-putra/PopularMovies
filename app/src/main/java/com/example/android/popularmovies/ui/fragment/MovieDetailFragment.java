package com.example.android.popularmovies.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ReviewModel;
import com.example.android.popularmovies.model.TrailerModel;
import com.example.android.popularmovies.presenter.MovieDetailPresenter;
import com.example.android.popularmovies.ui.view.MovieDetailView;
import com.example.android.popularmovies.util.MovieUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
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
    private static final String SCROLL_STATE_KEY = "com.example.popularmovies.scrollstate";
    private static final String REVIEWS_STATE_KEY = "com.example.popularmovies.reviewsstate";
    private static final String VIDEOS_STATE_KEY = "com.example.popularmovies.videosstate";

    @BindView(R.id.scrollView) ScrollView mScrollView;
    @BindView(R.id.backdrop) ImageView mBackdropImageView;
    @BindView(R.id.movie_poster) ImageView mPosterImageView;
    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.release_date) TextView mReleaseDateTextView;
    @BindView(R.id.rate_avg) TextView mRatingTextView;
    @BindView(R.id.rate_count) TextView mRatingCountTextView;
    @BindView(R.id.overview) TextView mOverviewTextView;
    @BindView(R.id.favorite) CheckBox mFavorite;
    @BindView(R.id.video_container) LinearLayout mVideoContainer;
    @BindView(R.id.review_container) LinearLayout mReviewContainer;

    @Inject MovieDetailPresenter mPresenter;
    private int[] scrollState = new int[2];

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

        mFavorite.setOnCheckedChangeListener((CompoundButton compoundButton, boolean check) -> {
            mPresenter.toggleFavorite(check);
        });

        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getIntArray(SCROLL_STATE_KEY);
            mPresenter.setVideos(savedInstanceState.getParcelableArrayList(VIDEOS_STATE_KEY));
            mPresenter.setReviews(savedInstanceState.getParcelableArrayList(REVIEWS_STATE_KEY));
        }

        Bundle args = getArguments();
        if (args != null){
            mPresenter.setDetailForMovie(args.getParcelable(CONTENT_KEY));
            mPresenter.loadTrailer();
            mPresenter.loadReview();
            if (scrollState != null && scrollState.length == 2) {
                mScrollView.post(() -> mScrollView.scrollTo(scrollState[0], scrollState[1]));
            }
        }
    }

    @Override
    public void showDetailMovie(MovieModel movie) {
        getActivity().setTitle(movie.getTitle());
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mRatingTextView.setText(String.format(Locale.getDefault(), "%.1f", movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
        mRatingCountTextView.setText(String.valueOf(movie.getVoteCount()));
        mFavorite.setChecked(movie.isFavorite());

        if (movie.getBackdropPath() != null){
            Picasso.with(getActivity())
                    .load(MovieUtil.getFullBackdropPath(movie.getBackdropPath()))
                    .into(mBackdropImageView);
        }

        if (movie.getPosterPath() != null){
            Picasso.with(getActivity())
                    .load(MovieUtil.getFullPosterPath(movie.getPosterPath()))
                    .into(mPosterImageView);
        }
    }

    @Override
    public void showTrailer(List<TrailerModel> trailers) {
        if (trailers == null || trailers.size() == 0){
            mVideoContainer.setVisibility(View.GONE);
            return;
        }

        for (int i = 1; i < mVideoContainer.getChildCount(); ++i)
            mVideoContainer.removeViewAt(i);

        final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        for (TrailerModel trailer : trailers){
            final View view = layoutInflater.inflate(R.layout.item_video_layout, mVideoContainer, false);
            final ImageView mThumbnailImageView = (ImageView) view.findViewById(R.id.thumbnail);
            if (trailer.getSite().equalsIgnoreCase("YouTube"))
                Picasso.with(getActivity())
                    .load(MovieUtil.getYoutubeThumbnailPath(trailer.getKey()))
                    .into(mThumbnailImageView);

            final TextView mVideoTitle = (TextView) view.findViewById(R.id.video_title);
            mVideoTitle.setText(String.format(Locale.getDefault(), "[%s] %s", trailer.getType(),
                    trailer.getName()));

            mVideoContainer.addView(view);

        }

        mVideoContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public void showReview(List<ReviewModel> reviews) {
        if (reviews == null || reviews.size() == 0){
            mReviewContainer.setVisibility(View.GONE);
            return;
        }

        for (int i = 1; i < mReviewContainer.getChildCount(); ++i)
            mReviewContainer.removeViewAt(i);

        final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        for (ReviewModel review : reviews){
            final View view = layoutInflater.inflate(R.layout.item_review_layout, mReviewContainer, false);

            final TextView mAuthorTextView = (TextView) view.findViewById(R.id.review_author);
            mAuthorTextView.setText(MovieUtil.customNullAuthor(review.getAuthor()));

            final TextView mContentTextView = (TextView) view.findViewById(R.id.review_body);
            mContentTextView.setText(review.getContent());

            mReviewContainer.addView(view);

        }

        mReviewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray(SCROLL_STATE_KEY, new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
        outState.putParcelableArrayList(VIDEOS_STATE_KEY, new ArrayList<>(mPresenter.getVideos()));
        outState.putParcelableArrayList(REVIEWS_STATE_KEY, new ArrayList<>(mPresenter.getReviews()));
    }
}
