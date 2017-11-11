package com.vadim.hasdfa.traintickets.Model

import android.os.Parcel
import android.os.Parcelable

/**
* Created by Raksha Vadim on 30.09.2017.
*/
data class Place(
        var wagon: Int,
        var number: Int,
        var price: Int,
        var isEnabled: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(wagon)
        parcel.writeInt(number)
        parcel.writeInt(price)
        parcel.writeByte(if (isEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        fun create(wagon: Int = -1,
                   number: Int = -1,
                   price: Int = 0,
                   isEnabled: Boolean = false): Place {
            return Place(wagon, number, price, isEnabled)
        }

        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }
}