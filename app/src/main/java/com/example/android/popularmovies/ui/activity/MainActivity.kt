package com.example.android.popularmovies.ui.activity

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.example.android.popularmovies.R
import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.ui.fragment.MovieDetailFragment
import com.example.android.popularmovies.ui.fragment.MovieGridFragment

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class MainActivity : AppCompatActivity(),
        HasSupportFragmentInjector,
        MovieGridFragment.MovieGridFragmentListener {

    override fun isTwoPaneMode(): Boolean  = this.isTwoPaneMode

    private val handler = Handler()

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    private var isTwoPaneMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.isTwoPaneMode = findViewById<View>(R.id.movie_detail_fragment) != null
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    override fun setSelectedMovie(movie: MovieModel) {
        handler.post {
            if (movie != null) {
                val fragment = MovieDetailFragment.newInstance(movie)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.movie_detail_fragment, fragment, MOVIE_FRAGMENT_DETAIL_TAG)
                        .commit()
            }
        }

    }

    companion object {
        private const val MOVIE_FRAGMENT_DETAIL_TAG = "fragment_movie_detail"
    }
}
