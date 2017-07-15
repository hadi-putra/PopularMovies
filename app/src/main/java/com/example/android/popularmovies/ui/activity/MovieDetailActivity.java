package com.example.android.popularmovies.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.presenter.MovieDetailPresenter;
import com.example.android.popularmovies.ui.fragment.MovieDetailFragment;
import com.example.android.popularmovies.ui.view.MovieDetailView;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MovieDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String MOVIE_FRAGMENT_DETAIL_TAG = "fragment_movie_detail";

    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null){
            Intent intent = getIntent();
            if (intent.hasExtra("content")){
                MovieDetailFragment fragment = MovieDetailFragment.newInstance(intent.getParcelableExtra("content"));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_fragment, fragment, MOVIE_FRAGMENT_DETAIL_TAG)
                        .commit();
            }
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
