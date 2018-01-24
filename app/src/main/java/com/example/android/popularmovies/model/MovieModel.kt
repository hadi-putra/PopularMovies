package com.example.android.popularmovies.model

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable

import com.squareup.moshi.Json

/**
 * Created by msk-1196 on 6/18/17.
 */

data class MovieModel(
    var id: Long = 0,
    var title: String? = null,
    @Json(name = "backdrop_path")
    var backdropPath: String? = null,
    @Json(name = "vote_average")
    var voteAverage: Double = 0.toDouble(),
    @Json(name = "vote_count")
    var voteCount: Int = 0,
    var popularity: Double = 0.toDouble(),
    @Json(name = "poster_path")
    var posterPath: String? = null,
    @Json(name = "release_date")
    var releaseDate: String? = null,
    var overview: String? = null,
    var isFavorite: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(backdropPath)
        parcel.writeDouble(voteAverage)
        parcel.writeInt(voteCount)
        parcel.writeDouble(popularity)
        parcel.writeString(posterPath)
        parcel.writeString(releaseDate)
        parcel.writeString(overview)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieModel> {
        override fun createFromParcel(parcel: Parcel): MovieModel {
            return MovieModel(parcel)
        }

        override fun newArray(size: Int): Array<MovieModel?> {
            return arrayOfNulls(size)
        }
    }
}
