package com.example.android.popularmovies.ui.fragment

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.example.android.popularmovies.PosterAdapter
import com.example.android.popularmovies.R
import com.example.android.popularmovies.data.Contract.MovieContract
import com.example.android.popularmovies.data.MovieRepository
import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.presenter.MainPresenter
import com.example.android.popularmovies.ui.activity.MovieDetailActivity
import com.example.android.popularmovies.ui.view.MainView
import com.example.android.popularmovies.util.Sort

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection

import com.example.android.popularmovies.util.Sort.FAVORITE
import com.example.android.popularmovies.util.Sort.POPULAR
import com.example.android.popularmovies.util.Sort.RATING
import kotlinx.android.synthetic.main.fragment_movies.*

/**
 * Created by msk-1196 on 7/14/17.
 */

class MovieGridFragment : Fragment(), MainView, PosterAdapter.PosterAdapterOnClickHandler {

    private var mPosterAdapter: PosterAdapter? = null
    @Inject lateinit var mPresenter: MainPresenter
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mStateGrid: Parcelable? = null
    private var mListener: MovieGridFragmentListener? = null
    private var selectedSort = Sort.POPULAR
    private var selectedMovie: MovieModel? = null

    private val favoriteLoader = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
            return if (id == ID_FAVORITE_LOADER) {
                CursorLoader(activity,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MovieRepository.MOVIE_PROJECTION, null, null, null)
            } else
                throw RuntimeException("Loader Not Implemented: " + id)
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
            if (selectedSort === FAVORITE)
                mPresenter!!.showFavorite(data)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            if (selectedSort === FAVORITE)
                mPosterAdapter!!.setMovies(null!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mGridLayoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.grid_item),
                GridLayoutManager.VERTICAL, false)
        rv_grid_movie_poster.layoutManager = mGridLayoutManager
        rv_grid_movie_poster.setHasFixedSize(true)
        mPosterAdapter = PosterAdapter(this)
        rv_grid_movie_poster.adapter = mPosterAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            mStateGrid = savedInstanceState.getParcelable(GRID_STATE_KEY)
            selectedSort = savedInstanceState.get(SORT_STATE_KEY) as Sort
            selectedMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY)

            activity.setTitle(getTitle(selectedSort))
        }
        if (selectedSort === FAVORITE)
            loadFavorite()
        else
            mPresenter!!.loadMovie(selectedSort)
    }

    private fun loadFavorite() {
        val manager = activity.supportLoaderManager
        val loader = manager.getLoader<Cursor>(ID_FAVORITE_LOADER)
        if (loader == null)
            manager.initLoader(ID_FAVORITE_LOADER, null, favoriteLoader)
        else
            manager.restartLoader(ID_FAVORITE_LOADER, null, favoriteLoader)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        if (context is MovieGridFragmentListener) {
            mListener = context
        } else {
            throw IllegalArgumentException("Activity has to implement MovieGridFragmentListener!")
        }
    }

    override fun onDestroyView() {
        mPresenter!!.clearSubscription()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        mStateGrid = mGridLayoutManager!!.onSaveInstanceState()
        outState!!.putSerializable(SORT_STATE_KEY, selectedSort)
        outState.putParcelable(GRID_STATE_KEY, mStateGrid)
        outState.putParcelable(SELECTED_MOVIE_KEY, selectedMovie)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        when (selectedSort) {
            RATING -> menu!!.findItem(R.id.action_top_rated).isChecked = true
            POPULAR -> menu!!.findItem(R.id.action_popular).isChecked = true
            FAVORITE -> menu!!.findItem(R.id.action_favorite).isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_popular -> {
                if (selectedSort !== POPULAR) {
                    item.isChecked = true
                    onSortOptionChanged(POPULAR)
                }
                return true
            }
            R.id.action_top_rated -> {
                if (selectedSort !== RATING) {
                    item.isChecked = true
                    onSortOptionChanged(RATING)
                }
                return true
            }
            R.id.action_favorite -> {
                if (selectedSort !== FAVORITE) {
                    item.isChecked = true
                    onSortOptionChanged(FAVORITE)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun onSortOptionChanged(option: Sort) {
        selectedMovie = null
        mStateGrid = null
        activity.setTitle(getTitle(option))
        selectedSort = option
        if (option === FAVORITE)
            loadFavorite()
        else
            mPresenter!!.loadMovie(option)
    }

    private fun getTitle(option: Sort): Int {
        return when (option) {
            RATING -> R.string.top_rated_title
            FAVORITE -> R.string.favorite_title
            else -> R.string.app_name
        }
    }

    override fun setMovies(movieModels: List<MovieModel>) {
        mPosterAdapter!!.setMovies(movieModels)
        if (mStateGrid != null)
            mGridLayoutManager!!.onRestoreInstanceState(mStateGrid)
        else
            mGridLayoutManager!!.scrollToPositionWithOffset(0, 0)
        if (selectedMovie == null && movieModels.size > 0) {
            selectedMovie = movieModels[0]
        }
        if (mListener!!.isTwoPaneMode() && selectedMovie != null) {
            val movie = selectedMovie
            mListener!!.setSelectedMovie(movie!!)
        }
    }

    override fun showLoading() {
        pb_loading_movies.visibility = (View.VISIBLE)
        rv_grid_movie_poster.visibility = (View.INVISIBLE)
    }

    override fun hideLoading() {
        pb_loading_movies.visibility = (View.INVISIBLE)
        rv_grid_movie_poster.visibility = (View.VISIBLE)
    }

    override fun onClick(movie: MovieModel) {
        selectedMovie = movie
        if (mListener!!.isTwoPaneMode()) {
            mListener!!.setSelectedMovie(movie)
        } else {
            val destinationIntent = Intent(context, MovieDetailActivity::class.java)
            destinationIntent.putExtra("content", movie)
            startActivity(destinationIntent)
        }
    }


    interface MovieGridFragmentListener {
        fun isTwoPaneMode(): Boolean
        fun setSelectedMovie(movie: MovieModel)
    }

    companion object {
        private val GRID_STATE_KEY = "com.example.popularmovies.gridstate"
        private val SORT_STATE_KEY = "com.example.popularmovies.sortstate"
        private val SELECTED_MOVIE_KEY = "com.example.popularmovies.selectedmovie"

        private val ID_FAVORITE_LOADER = 115
    }
}
