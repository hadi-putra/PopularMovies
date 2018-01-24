package com.example.android.popularmovies.util

import android.content.ContentValues
import android.net.Uri

import com.example.android.popularmovies.data.Contract.MovieContract
import com.example.android.popularmovies.model.MovieModel

/**
 * Created by msk-1196 on 7/24/17.
 */

object MovieUtil {
    @Transient
    private val BASE_PHOTO_PATH = "http://image.tmdb.org/t/p/w185"
    @Transient
    private val BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w342"
    @Transient
    private val YOUTUBE_THUMBNAIL_PATH = "http://img.youtube.com/vi/"
    @Transient
    private val YOUTUBE_VIDEO_PATH = "http://www.youtube.com/watch?v="


    fun getFullPosterPath(posterPath: String?): String? {
        return if (posterPath == null)
            null
        else
            BASE_PHOTO_PATH + posterPath
    }

    fun getFullBackdropPath(backdropPath: String?): String? {
        return if (backdropPath == null)
            null
        else
            BASE_BACKDROP_PATH + backdropPath
    }

    fun movieToContentValues(movie: MovieModel): ContentValues {
        return ContentValues().apply {
            put(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID, movie.id)
            put(MovieContract.MovieEntry.COLUMN_TITLE, movie.title)
            put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.overview)
            put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.backdropPath)
            put(MovieContract.MovieEntry.COLUMN_POSTER, movie.posterPath)
            put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, movie.voteAverage)
            put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.voteCount)
            put(MovieContract.MovieEntry.COLUMN_POPULAITY, movie.popularity)
            put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate)
            put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, if (movie.isFavorite) 1 else 0)
        }
    }

    fun getYoutubeThumbnailPath(videoKey: String): String {
        return YOUTUBE_THUMBNAIL_PATH + videoKey + "/default.jpg"
    }

    fun customNullAuthor(author: String?): String {
        return if (author == null || author.isEmpty()) "No name" else author
    }

    fun getVideoUri(key: String): Uri {
        return Uri.parse(YOUTUBE_VIDEO_PATH + key)
    }
}
