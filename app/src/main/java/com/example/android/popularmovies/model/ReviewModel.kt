package com.example.android.popularmovies.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by msk-1196 on 7/12/17.
 */

class ReviewModel(
    var id: String? = null,
    var author: String? = null,
    var content: String? = null,
    private var url: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(author)
        parcel.writeString(content)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReviewModel> {
        override fun createFromParcel(parcel: Parcel): ReviewModel {
            return ReviewModel(parcel)
        }

        override fun newArray(size: Int): Array<ReviewModel?> {
            return arrayOfNulls(size)
        }
    }
}
