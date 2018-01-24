package com.example.android.popularmovies.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by msk-1196 on 7/12/17.
 */

class TrailerModel(
    var id: String? = null,
    var key: String? = null,
    var name: String? = null,
    var site: String? = null,
    var type: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(key)
        parcel.writeString(name)
        parcel.writeString(site)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrailerModel> {
        override fun createFromParcel(parcel: Parcel): TrailerModel {
            return TrailerModel(parcel)
        }

        override fun newArray(size: Int): Array<TrailerModel?> {
            return arrayOfNulls(size)
        }
    }
}
