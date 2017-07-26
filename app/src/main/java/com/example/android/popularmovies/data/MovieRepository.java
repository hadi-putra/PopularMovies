package com.example.android.popularmovies.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.data.Contract.MovieContract;
import com.example.android.popularmovies.model.MovieModel;
import com.example.android.popularmovies.model.ResponseApi;
import com.example.android.popularmovies.model.ReviewModel;
import com.example.android.popularmovies.model.TrailerModel;
import com.example.android.popularmovies.util.MovieApi;
import com.example.android.popularmovies.util.MovieUtil;
import com.example.android.popularmovies.util.Sort;
import com.squareup.sqlbrite2.BriteContentResolver;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by msk-1196 on 7/23/17.
 */

public class MovieRepository {
    private static final String POPULAR_QUERY = "popular";
    private static final String TOP_RATED_QUERY = "top_rated";

    private final MovieApi movieApi;
    private final BriteContentResolver briteContentResolver;
    private final ContentResolver contentResolver;

    private BehaviorSubject<Set<Long>> mSavedMovieIdsSubject;

    private static final String[] MOVIE_PROJECTION = new String[]{
            MovieContract.MovieEntry.COLUMN_MOVIEDB_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_BACKDROP,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_VOTE_AVG,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_POPULAITY,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_IS_FAVORITE
    };

    private static final String[] MOVIEDB_ID_PROJECTION = new String[]{
            MovieContract.MovieEntry.COLUMN_MOVIEDB_ID
    };

    private static final int INDEX_MOVIEDB_ID = 0;
    private static final int INDEX_MOVIE_TITLE = 1;
    private static final int INDEX_MOVIE_OVERVIEW = 2;
    private static final int INDEX_MOVIE_BACKDROP = 3;
    private static final int INDEX_MOVIE_POSTER = 4;
    private static final int INDEX_MOVIE_VOTE_AVG = 5;
    private static final int INDEX_MOVIE_VOTE_COUNT = 6;
    private static final int INDEX_MOVIE_POPULARITY = 7;
    private static final int INDEX_MOVIE_RELEASE_DATE = 8;
    private static final int INDEX_MOVIE_IS_FAVORITE = 9;

    public MovieRepository(MovieApi movieApi, BriteContentResolver briteContentResolver,
                           ContentResolver contentResolver) {
        this.movieApi = movieApi;
        this.briteContentResolver = briteContentResolver;
        this.briteContentResolver.setLoggingEnabled(true);
        this.contentResolver = contentResolver;
    }

    public Observable<List<MovieModel>> getSortedMovies(Sort selectedSort) {
        return movieApi.getMovies(selectedSort == Sort.RATING? TOP_RATED_QUERY : POPULAR_QUERY)
                .map(ResponseApi::getResults)
                .withLatestFrom(getFavoriteIds(), ((movies, favoriteMovieDbIds) -> {
                    for (MovieModel movie : movies){
                        movie.setFavorite(favoriteMovieDbIds.contains(movie.getId()));
                    }
                    return movies;
                }))
                .subscribeOn(Schedulers.io());
    }

    private Observable<Set<Long>> getFavoriteIds() {
        if (mSavedMovieIdsSubject == null){
            mSavedMovieIdsSubject = BehaviorSubject.create();
            favoriteIds().subscribe(mSavedMovieIdsSubject);
        }
        return mSavedMovieIdsSubject.hide();
    }

    private Observable<Set<Long>> favoriteIds() {
        return briteContentResolver.createQuery(MovieContract.MovieEntry.CONTENT_URI, MOVIEDB_ID_PROJECTION,
                null, null, null, true)
                .map(MOVIEDB_ID_PROJECTION_MAP)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<MovieModel>> getFavorites() {
        return briteContentResolver.createQuery(MovieContract.MovieEntry.CONTENT_URI, MOVIE_PROJECTION,
                null, null, null, true)
                .map(PROJECTION_MAP)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<ReviewModel>> getMovieReviews(long movieId){
        return movieApi.getReviewByMovieId(movieId)
                .map(ResponseApi::getResults)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<TrailerModel>> getMovieVideos(long movieId){
        return movieApi.getTrailersByMovieId(movieId)
                .map(ResponseApi::getResults)
                .subscribeOn(Schedulers.io());
    }

    private Function<SqlBrite.Query, List<MovieModel>> PROJECTION_MAP = query -> {
        List<MovieModel> movies = new ArrayList<>();
        Cursor cursor = query.run();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MovieModel movie = new MovieModel();
                movie.setId(cursor.getLong(INDEX_MOVIEDB_ID));
                movie.setTitle(cursor.getString(INDEX_MOVIE_TITLE));
                movie.setOverview(cursor.getString(INDEX_MOVIE_OVERVIEW));
                movie.setBackdropPath(cursor.getString(INDEX_MOVIE_BACKDROP));
                movie.setPosterPath(cursor.getString(INDEX_MOVIE_POSTER));
                movie.setVoteAverage(cursor.getDouble(INDEX_MOVIE_VOTE_AVG));
                movie.setVoteCount(cursor.getInt(INDEX_MOVIE_VOTE_COUNT));
                movie.setPopularity(cursor.getDouble(INDEX_MOVIE_POPULARITY));
                movie.setReleaseDate(cursor.getString(INDEX_MOVIE_RELEASE_DATE));
                movie.setFavorite(cursor.getInt(INDEX_MOVIE_IS_FAVORITE) == 1);
                movies.add(movie);
            }
            cursor.close();
        }
        return movies;
    };

    private Function<SqlBrite.Query, Set<Long>> MOVIEDB_ID_PROJECTION_MAP = query -> {
        Set<Long> moviedbIds = new HashSet<>();
        Cursor cursor = query.run();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                moviedbIds.add(cursor.getLong(INDEX_MOVIEDB_ID));
            }
            cursor.close();
        }
        return moviedbIds;
    };

    public void toggleFavorite(boolean isFavorite, MovieModel movie) {
        AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(contentResolver) {};
        if (isFavorite) {
            Log.e(getClass().getSimpleName(), "Favorite");
            asyncQueryHandler.startInsert(-1, null, MovieContract.MovieEntry.CONTENT_URI,
                    MovieUtil.movieToContentValues(movie));
        } else {
            Log.e(getClass().getSimpleName(), "Unfavorite");
            asyncQueryHandler.startDelete(-1, null,
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIEDB_ID+"=?",
                    new String[]{Long.toString(movie.getId())});
        }
    }
}
