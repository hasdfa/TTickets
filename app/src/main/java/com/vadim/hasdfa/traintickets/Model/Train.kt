package com.vadim.hasdfa.traintickets.Model

import android.os.Parcel
import android.os.Parcelable

/**
* Created by Raksha Vadim on 29.09.2017.
*/
data class Train(
        var number: Int,
        var wagons: ArrayList<Wagon>
): Parcelable {
    var minPrice: Int = 0
    var midPrice: Int = 0
    var maxPrice: Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readWagons()) {
        minPrice = parcel.readInt()
        midPrice = parcel.readInt()
        maxPrice = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeTypedList(wagons)
        parcel.writeInt(minPrice)
        parcel.writeInt(midPrice)
        parcel.writeInt(maxPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Train> {
        fun create(number: Int = 0,
                   wagons: ArrayList<Wagon> = arrayListOf()): Train {
            return Train(number, wagons)
        }

        private fun Parcel.readWagons(): ArrayList<Wagon>{
            val wagons = ArrayList<Wagon>()
            this.readTypedList(wagons, Wagon.CREATOR)
            return wagons
        }

        override fun createFromParcel(parcel: Parcel): Train {
            return Train(parcel)
        }

        override fun newArray(size: Int): Array<Train?> {
            return arrayOfNulls(size)
        }
    }
}