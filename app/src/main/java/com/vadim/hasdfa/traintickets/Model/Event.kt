package com.vadim.hasdfa.traintickets.Model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
* Created by Raksha Vadim on 30.09.2017.
*/
data class Event(
        var train: Train,
        var startStation: String,
        var endStation: String,
        var startTime: Calendar,
        var endTime: Calendar,
        var startPlatform: Int,
        var endPlatform: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Train::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readCalendar(),
            parcel.readCalendar(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(train, flags)
        parcel.writeString(startStation)
        parcel.writeString(endStation)
        parcel.writeLong(startTime.time.time)
        parcel.writeLong(endTime.time.time)
        parcel.writeInt(startPlatform)
        parcel.writeInt(endPlatform)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        private fun Parcel.readCalendar(): Calendar{
            val calendar = Calendar.getInstance()
            calendar.time = Date(this.readLong())
            return calendar
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }

        fun create(train: Train = Train.create(),
                   startStation: String = "",
                   endStation: String = "",
                   startTime: Calendar = Calendar.getInstance(),
                   endTime: Calendar = Calendar.getInstance(),
                   startPlatform: Int = -1,
                   endPlatform: Int = -1): Event{
            return Event(train, startStation, endStation, startTime, endTime, startPlatform, endPlatform)
        }
    }
}