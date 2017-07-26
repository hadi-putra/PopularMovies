package com.example.android.popularmovies.ui.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.ui.fragment.MovieDetailFragment;
import com.example.android.popularmovies.ui.fragment.MovieGridFragment;
import com.example.android.popularmovies.util.Sort;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        MovieGridFragment.MovieGridFragmentListener{
    private static final String MOVIE_FRAGMENT_DETAIL_TAG = "fragment_movie_detail";

    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;
    private boolean isTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTwoPaneMode = findViewById(R.id.movie_detail_fragment) != null;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public boolean isTwoPaneMode() {
        return isTwoPaneMode;
    }

    @Override
    public void setSelectedMovie(MovieModel movie) {
        if (movie != null) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_fragment, fragment, MOVIE_FRAGMENT_DETAIL_TAG)
                    .commit();
        }
    }
}
