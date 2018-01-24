package com.example.android.popularmovies.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

import com.example.android.popularmovies.R
import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.model.ReviewModel
import com.example.android.popularmovies.model.TrailerModel
import com.example.android.popularmovies.presenter.MovieDetailPresenter
import com.example.android.popularmovies.ui.view.MovieDetailView
import com.example.android.popularmovies.util.MovieUtil
import com.squareup.picasso.Picasso

import java.util.ArrayList
import java.util.Locale

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_detail.*

/**
 * Created by msk-1196 on 7/15/17.
 */

class MovieDetailFragment : Fragment(), MovieDetailView {

    @Inject lateinit var mPresenter: MovieDetailPresenter
    private var scrollState: IntArray? = IntArray(2)

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            scrollState = savedInstanceState.getIntArray(SCROLL_STATE_KEY)
            mPresenter!!.setVideos(savedInstanceState.getParcelableArrayList(VIDEOS_STATE_KEY)!!)
            mPresenter!!.setReviews(savedInstanceState.getParcelableArrayList(REVIEWS_STATE_KEY)!!)
        }

        mPresenter!!.loadTrailer()
        mPresenter!!.loadReview()
        if (scrollState != null && scrollState!!.size == 2) {
            scrollView.post { scrollView.scrollTo(scrollState!![0], scrollState!![1]) }
        }

        mPresenter!!.checkFavorite()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_favorite.setOnClickListener { _ -> mPresenter!!.toggleFavorite() }

        val args = arguments
        if (args != null) {
            mPresenter!!.setDetailForMovie(args.getParcelable(CONTENT_KEY)!!)
        }
    }

    override fun showDetailMovie(movie: MovieModel) {
        activity.title = movie.title
        title.text = movie.title
        release_date.text = movie.releaseDate
        // show the vote_average
        rate_avg.text = String.format(Locale.getDefault(),
                "%.1f/10", movie.voteAverage)
        overview.text = movie.overview
        rate_count.text = movie.voteCount.toString()
        if (movie.isFavorite)
            fab_favorite.setImageResource(R.drawable.ic_favorite_fill)
        else
            fab_favorite.setImageResource(R.drawable.ic_favorite_border)

        if (movie.backdropPath != null) {
            Picasso.with(activity)
                    .load(MovieUtil.getFullBackdropPath(movie.backdropPath))
                    .into(backdrop)
        }

        if (movie.posterPath != null) {
            Picasso.with(activity)
                    .load(MovieUtil.getFullPosterPath(movie.posterPath))
                    .into(movie_poster)
        }
    }

    override fun showTrailer(trailers: List<TrailerModel>) {
        if (trailers.isEmpty()) {
            video_container.visibility = View.GONE
            return
        }

        for (i in 1 until video_container.childCount)
            video_container.removeViewAt(i)

        val layoutInflater = LayoutInflater.from(activity)

        for (trailer in trailers) {
            val view = layoutInflater.inflate(R.layout.item_video_layout,
                    video_container, false)
            val mThumbnailImageView = view.findViewById<View>(R.id.thumbnail) as ImageView
            if (trailer.site!!.equals("YouTube", ignoreCase = true))
                Picasso.with(activity)
                        .load(MovieUtil.getYoutubeThumbnailPath(trailer.key!!))
                        .into(mThumbnailImageView)

            val mVideoTitle = view.findViewById<View>(R.id.video_title) as TextView
            mVideoTitle.text = String.format(Locale.getDefault(), "[%s] %s", trailer.type,
                    trailer.name)
            view.tag = trailer
            view.setOnClickListener { view1 ->
                val t = view1.tag as TrailerModel
                if (t.site!!.equals("YouTube", ignoreCase = true)) {
                    val intent = Intent(Intent.ACTION_VIEW, MovieUtil.getVideoUri(t.key!!))
                    if (intent.resolveActivity(activity.packageManager) != null)
                        startActivity(intent)
                } else {
                    Toast.makeText(context, R.string.video_not_supported, Toast.LENGTH_SHORT).show()
                }
            }

            video_container.addView(view)

        }

        video_container.visibility = View.VISIBLE
        activity.invalidateOptionsMenu()

    }

    override fun showReview(reviews: List<ReviewModel>) {
        if (reviews.isEmpty()) {
            review_container.visibility = View.GONE
            return
        }

        for (i in 1 until review_container.childCount)
            review_container.removeViewAt(i)

        val layoutInflater = LayoutInflater.from(activity)

        for (review in reviews) {
            val view = layoutInflater.inflate(R.layout.item_review_layout,
                    review_container, false)

            val mAuthorTextView = view.findViewById<View>(R.id.review_author) as TextView
            mAuthorTextView.text = MovieUtil.customNullAuthor(review.author)

            val mContentTextView = view.findViewById<View>(R.id.review_body) as TextView
            mContentTextView.text = review.content

            review_container.addView(view)

        }

        review_container.visibility = View.VISIBLE
    }

    override fun toggleFavoriteFab(favorite: Boolean) {
        fab_favorite.setImageResource(if (favorite)
            R.drawable.ic_favorite_fill
        else
            R.drawable.ic_favorite_border)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState!!.putIntArray(SCROLL_STATE_KEY, intArrayOf(scrollView.scrollX, scrollView.scrollY))
        outState.putParcelableArrayList(VIDEOS_STATE_KEY, ArrayList(mPresenter!!.videos!!))
        outState.putParcelableArrayList(REVIEWS_STATE_KEY, ArrayList(mPresenter!!.reviews!!))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.detail_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        if (mPresenter!!.videos != null && mPresenter!!.videos!!.size > 0)
            menu!!.findItem(R.id.action_share).isVisible = true
        else
            menu!!.findItem(R.id.action_share).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_share -> {
                share(mPresenter!!.videos!![0])
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun share(trailerModel: TrailerModel) {
        val intent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(getString(R.string.share_template, String.format(Locale.getDefault(),
                        "[%s] %s", trailerModel.type, trailerModel.name),
                        MovieUtil.getVideoUri(trailerModel.key!!).toString()))
                .intent
        startActivity(Intent.createChooser(intent, getString(R.string.action_share)))
    }

    companion object {
        const val CONTENT_KEY = "content.movie"
        private const val SCROLL_STATE_KEY = "com.example.popularmovies.scrollstate"
        private const val REVIEWS_STATE_KEY = "com.example.popularmovies.reviewsstate"
        private const val VIDEOS_STATE_KEY = "com.example.popularmovies.videosstate"

        fun newInstance(movie: MovieModel): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val args = Bundle()
            args.putParcelable(CONTENT_KEY, movie)
            fragment.arguments = args
            return fragment
        }
    }
}
