package com.ademir.exotransitionin

import android.os.Parcel
import android.os.Parcelable

data class Video(val thumb: String, val url: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(thumb)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { Video(it) }
    }

}