package com.example.android.popularmovies.ui.activity

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import com.example.android.popularmovies.R
import com.example.android.popularmovies.ui.fragment.MovieDetailFragment


import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class MovieDetailActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        if (savedInstanceState == null) {
            val intent = intent
            if (intent.hasExtra("content")) {
                val fragment = MovieDetailFragment.newInstance(intent.getParcelableExtra("content"))
                supportFragmentManager.beginTransaction()
                        .replace(R.id.movie_detail_fragment, fragment, MOVIE_FRAGMENT_DETAIL_TAG)
                        .commit()
            }
        }

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    companion object {
        private const val MOVIE_FRAGMENT_DETAIL_TAG = "fragment_movie_detail"
    }
}
