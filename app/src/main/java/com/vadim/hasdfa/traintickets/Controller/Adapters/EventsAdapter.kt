package com.vadim.hasdfa.traintickets.Controller.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vadim.hasdfa.traintickets.Controller.Activities.PickPlaceActivity
import com.vadim.hasdfa.traintickets.Model.Event
import com.vadim.hasdfa.traintickets.R
import com.vadim.hasdfa.traintickets._core.ActivityBase
import com.vadim.hasdfa.traintickets._core.Log
import java.util.*

/**
* Created by Raksha Vadim on 02.10.2017.
*/

class EventsAdapter(private val data: List<Event>): RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        Log.d("onCreateViewHolder:$viewType")
        return ViewHolder(
                LayoutInflater.from(parent?.context)
                        .inflate(R.layout.event_simple_item, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val event = data[position]
        holder?.let {
            with(event) {
                it.timeFrom.text = getTimeByCalendar(startTime)
                it.timeTo.text = getTimeByCalendar(endTime)

                it.cityFrom.text = startStation
                it.cityTo.text = endStation

                it.dateFrom.text = "${startTime.get(Calendar.DAY_OF_MONTH)}.${startTime.get(Calendar.MONTH)}.${startTime.get(Calendar.YEAR)}"
                it.dateTo.text = "${endTime.get(Calendar.DAY_OF_MONTH)}.${endTime.get(Calendar.MONTH)}.${endTime.get(Calendar.YEAR)}"

                val time = endTime.time.time - startTime.time.time

                it.ourTrip.text = getOurTripTime(time)

                it.showMore.text = "from ₴${train.minPrice} >"

                it.showMore.setOnClickListener {
                    val context = it.context

                    val intent = Intent(context, PickPlaceActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("event", event)

                    intent.putExtras(bundle)
                    context.startActivity(intent)
                    (context as? ActivityBase)
                            ?.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                }
            }
        }
    }

    companion object {
        fun getTimeByCalendar(date: Calendar): String {
            val hours = date.get(Calendar.HOUR_OF_DAY)
            val minutes = date.get(Calendar.MINUTE)

            var fStr = when (hours) {
                0 -> "00"
                in 1..9 -> "0$hours"
                else -> "$hours"
            }

            fStr += ":"

            fStr += when (minutes) {
                0 -> "00"
                in 1..9 -> "0$minutes"
                else -> "$minutes"
            }

            return fStr
        }

        fun getOurTripTime(time: Long): String {
            val ourTripDate = Calendar.getInstance()
            ourTripDate.time = Date(time)

            val hrStr = "h"
            val mStr = "min"
            val dayStr = "days"
            val yearsStr = "years"

            var ourTripStr = ""
            if (ourTripDate.get(Calendar.YEAR)-1970 > 0) {
                ourTripStr += "${ourTripDate.get(Calendar.YEAR)-1970} $yearsStr "
            }
            if (ourTripDate.get(Calendar.MONTH) > 0) {
                ourTripStr += "${ourTripDate.get(Calendar.MONTH)} $mStr "
            }
            if (ourTripDate.get(Calendar.DAY_OF_MONTH) > 1) {
                ourTripStr += "${ourTripDate.get(Calendar.DAY_OF_MONTH) - 1} $dayStr "
            }
            if (ourTripDate.get(Calendar.HOUR_OF_DAY) > 0) {
                val m = ourTripDate.get(Calendar.HOUR_OF_DAY)
                if (m in 1..9) ourTripStr += "0$m $hrStr "
                else if (m > 0) ourTripStr += "$m $hrStr "
            }
            if (ourTripDate.get(Calendar.MINUTE) > 0) {
                val m = ourTripDate.get(Calendar.MINUTE)
                if (m in 1..9) ourTripStr += "0$m $mStr "
                else if (m > 0) ourTripStr += "$m $mStr "
            }

            return ourTripStr
        }
    }


    fun search(cityFrom: String? = null, cityTo: String? = null, dateFrom: Date? = null, dateTo: Date? = null) {
        cityFrom?.let {

        }
        cityTo?.let {

        }
        dateFrom?.let {

        }
        dateTo?.let {

        }
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount:${data.count()}")
        return data.count()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val timeFrom: TextView = itemView.findViewById(R.id.timeFrom)
        val timeTo: TextView = itemView.findViewById(R.id.timeTo)

        val cityFrom: TextView = itemView.findViewById(R.id.cityFrom)
        val cityTo: TextView = itemView.findViewById(R.id.cityTo)

        val dateFrom: TextView = itemView.findViewById(R.id.dateFrom)
        val dateTo: TextView = itemView.findViewById(R.id.dateTo)

        val ourTrip: TextView = itemView.findViewById(R.id.tripTime)
        val showMore: TextView = itemView.findViewById(R.id.showMore)
    }
}