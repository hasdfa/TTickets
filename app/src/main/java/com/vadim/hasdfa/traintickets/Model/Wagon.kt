package com.vadim.hasdfa.traintickets.Model

import android.os.Parcel
import android.os.Parcelable

/**
* Created by Raksha Vadim on 30.09.2017.
*/
data class Wagon(
        var wagonType: WagonType,
        var places: ArrayList<Place>
): Parcelable {
    constructor(parcel: Parcel): this(
            WagonType.valueOf(parcel.readString()),
            parcel.readPlaces())

    enum class WagonType {
        SV, Coupe, Platzkart
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(wagonType.name)
        parcel.writeTypedList(places)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wagon> {
        fun create(wagonType: WagonType = WagonType.Platzkart,
                   places: ArrayList<Place> = arrayListOf()): Wagon {
            return Wagon(wagonType, places)
        }

        private fun Parcel.readPlaces(): ArrayList<Place>{
            val places = ArrayList<Place>()
            this.readTypedList(places, Place.CREATOR)
            return places
        }

        override fun createFromParcel(parcel: Parcel): Wagon {
            return Wagon(parcel)
        }

        override fun newArray(size: Int): Array<Wagon?> {
            return arrayOfNulls(size)
        }
    }
}