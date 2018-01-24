package com.example.android.popularmovies.data

import android.content.AsyncQueryHandler
import android.content.ContentResolver
import android.database.Cursor

import com.example.android.popularmovies.data.Contract.MovieContract
import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.model.ReviewModel
import com.example.android.popularmovies.model.TrailerModel
import com.example.android.popularmovies.util.MovieApi
import com.example.android.popularmovies.util.MovieUtil
import com.example.android.popularmovies.util.Sort
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.SqlBrite

import java.util.ArrayList
import java.util.HashSet

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by msk-1196 on 7/23/17.
 */

class MovieRepository(private val movieApi: MovieApi,
                      private val briteContentResolver: BriteContentResolver,
                      private val contentResolver: ContentResolver) {

    private var mSavedMovieIdsSubject: BehaviorSubject<Set<Long>>? = null

    val favoriteIds: Observable<Set<Long>>
        get() {
            if (mSavedMovieIdsSubject == null) {
                mSavedMovieIdsSubject = BehaviorSubject.create()
                favoriteIds().subscribe(mSavedMovieIdsSubject!!)
            }
            return mSavedMovieIdsSubject!!.hide()
        }

    val favorites: Observable<List<MovieModel>>
        get() = briteContentResolver.createQuery(MovieContract.MovieEntry.CONTENT_URI, MOVIE_PROJECTION, null, null, null, true)
                .map<List<MovieModel>>(PROJECTION_MAP)
                .subscribeOn(Schedulers.io())

    private val PROJECTION_MAP = { query: SqlBrite.Query ->
        val movies = ArrayList<MovieModel>()
        val cursor = query.run()
        if (cursor != null) {
            while (cursor!!.moveToNext()) {
                movies.add(MovieModel().apply {
                    id = cursor!!.getLong(INDEX_MOVIEDB_ID)
                    title = cursor!!.getString(INDEX_MOVIE_TITLE)
                    overview = cursor!!.getString(INDEX_MOVIE_OVERVIEW)
                    backdropPath = cursor!!.getString(INDEX_MOVIE_BACKDROP)
                    posterPath = cursor!!.getString(INDEX_MOVIE_POSTER)
                    voteAverage = cursor!!.getDouble(INDEX_MOVIE_VOTE_AVG)
                    voteCount = cursor!!.getInt(INDEX_MOVIE_VOTE_COUNT)
                    popularity = cursor!!.getDouble(INDEX_MOVIE_POPULARITY)
                    releaseDate = cursor!!.getString(INDEX_MOVIE_RELEASE_DATE)
                    isFavorite = cursor!!.getInt(INDEX_MOVIE_IS_FAVORITE) == 1
                })
            }
            cursor!!.close()
        }
        movies
    }

    private val MOVIEDB_ID_PROJECTION_MAP = { query: SqlBrite.Query ->
        val moviedbIds = HashSet<Long>()
        val cursor = query.run()
        if (cursor != null) {
            while (cursor!!.moveToNext()) {
                moviedbIds.add(cursor!!.getLong(INDEX_MOVIEDB_ID))
            }
            cursor!!.close()
        }
        moviedbIds
    }

    init {
        this.briteContentResolver.setLoggingEnabled(true)
    }

    fun getSortedMovies(selectedSort: Sort): Observable<List<MovieModel>> {
        return movieApi.getMovies(if (selectedSort == Sort.RATING) TOP_RATED_QUERY else POPULAR_QUERY)
                .map( { it.results })
                .withLatestFrom(this.favoriteIds, BiFunction{
                    movies: List<MovieModel>, favoriteMovieDbIds: Set<Long> ->
                    movies.map { it.apply { isFavorite = favoriteMovieDbIds.contains(id) } } }
                )
                .subscribeOn(Schedulers.io())
    }

    private fun favoriteIds(): Observable<Set<Long>> {
        return briteContentResolver.createQuery(MovieContract.MovieEntry.CONTENT_URI, MOVIEDB_ID_PROJECTION, null, null, null, true)
                .map<Set<Long>>(MOVIEDB_ID_PROJECTION_MAP)
                .subscribeOn(Schedulers.io())
    }

    fun getMovieReviews(movieId: Long): Flowable<List<ReviewModel>> {
        return movieApi.getReviewByMovieId(movieId)
                .map({ it.results })
                .subscribeOn(Schedulers.io())
    }

    fun getMovieVideos(movieId: Long): Flowable<List<TrailerModel>> {
        return movieApi.getTrailersByMovieId(movieId)
                .map({ it.results })
                .subscribeOn(Schedulers.io())
    }

    fun toggleFavorite(movie: MovieModel) {
        val asyncQueryHandler = object : AsyncQueryHandler(contentResolver) {

        }
        if (movie.isFavorite) {
            asyncQueryHandler.startInsert(-1, null, MovieContract.MovieEntry.CONTENT_URI,
                    MovieUtil.movieToContentValues(movie))
        } else {
            asyncQueryHandler.startDelete(-1, null,
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIEDB_ID + "=?",
                    arrayOf(java.lang.Long.toString(movie.id)))
        }
    }

    fun getMoviesFromCursor(cursor: Cursor?): List<MovieModel> {
        val movies = ArrayList<MovieModel>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                movies.add(MovieModel().apply {
                    id = cursor.getLong(INDEX_MOVIEDB_ID)
                    title = cursor.getString(INDEX_MOVIE_TITLE)
                    overview = cursor.getString(INDEX_MOVIE_OVERVIEW)
                    backdropPath = cursor.getString(INDEX_MOVIE_BACKDROP)
                    posterPath = cursor.getString(INDEX_MOVIE_POSTER)
                    voteAverage = cursor.getDouble(INDEX_MOVIE_VOTE_AVG)
                    voteCount = cursor.getInt(INDEX_MOVIE_VOTE_COUNT)
                    popularity = cursor.getDouble(INDEX_MOVIE_POPULARITY)
                    releaseDate = cursor.getString(INDEX_MOVIE_RELEASE_DATE)
                    isFavorite = cursor.getInt(INDEX_MOVIE_IS_FAVORITE) == 1
                })
            }
        }
        return movies
    }

    companion object {
        private val POPULAR_QUERY = "popular"
        private val TOP_RATED_QUERY = "top_rated"

        val MOVIE_PROJECTION = arrayOf(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID, MovieContract.MovieEntry.COLUMN_TITLE, MovieContract.MovieEntry.COLUMN_OVERVIEW, MovieContract.MovieEntry.COLUMN_BACKDROP, MovieContract.MovieEntry.COLUMN_POSTER, MovieContract.MovieEntry.COLUMN_VOTE_AVG, MovieContract.MovieEntry.COLUMN_VOTE_COUNT, MovieContract.MovieEntry.COLUMN_POPULAITY, MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MovieContract.MovieEntry.COLUMN_IS_FAVORITE)

        private val MOVIEDB_ID_PROJECTION = arrayOf(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID)

        val INDEX_MOVIEDB_ID = 0
        val INDEX_MOVIE_TITLE = 1
        val INDEX_MOVIE_OVERVIEW = 2
        val INDEX_MOVIE_BACKDROP = 3
        val INDEX_MOVIE_POSTER = 4
        val INDEX_MOVIE_VOTE_AVG = 5
        val INDEX_MOVIE_VOTE_COUNT = 6
        val INDEX_MOVIE_POPULARITY = 7
        val INDEX_MOVIE_RELEASE_DATE = 8
        val INDEX_MOVIE_IS_FAVORITE = 9
    }
}

/*private fun <T1, T2, R> Observable<T1>.withLatestFrom(other: Observable<T1>,
                                                      combiner: (T1, T2) -> R): Observable<R>
        = withLatestFrom(other, BiFunction { t1: T1, t2 -> combiner.invoke(t1, t2) })*/
