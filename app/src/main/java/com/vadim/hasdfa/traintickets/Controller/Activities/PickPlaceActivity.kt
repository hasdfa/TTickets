package com.vadim.hasdfa.traintickets.Controller.Activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.RelativeLayout
import com.vadim.hasdfa.traintickets.Controller.Adapters.EventsAdapter
import com.vadim.hasdfa.traintickets.Controller.Adapters.ViewPagerAdapter
import com.vadim.hasdfa.traintickets.Model.Event
import com.vadim.hasdfa.traintickets.Model.Wagon.WagonType.*
import com.vadim.hasdfa.traintickets.R
import com.vadim.hasdfa.traintickets.View.CarouselEffectTransformer
import com.vadim.hasdfa.traintickets._core.ActivityBase
import kotlinx.android.synthetic.main.activity_pick_place.*
import me.grantland.widget.AutofitTextView
import java.util.*

/**
* Created by Raksha Vadim on 03.10.2017.
*/
class PickPlaceActivity: ActivityBase() {

    private val event by lazy {
        intent?.extras?.getParcelable<Event>("event") ?: throw Exception("Not found")
    }

    private var selectedTab = Coupe

    private val mVPAdapter by lazy {
        ViewPagerAdapter(
                    supportFragmentManager,
                    event.train.wagons
                )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_place)

        ic_back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.push_left_in_xz, R.anim.push_left_out_xz)
        }

        with(event) {
            timeFrom.text = EventsAdapter.getTimeByCalendar(startTime)
            timeTo.text = EventsAdapter.getTimeByCalendar(endTime)

            cityFrom.text = startStation
            cityTo.text = endStation

            dateFrom.text = "${startTime.get(Calendar.DAY_OF_MONTH)}.${startTime.get(Calendar.MONTH)}.${startTime.get(Calendar.YEAR)}"
            dateTo.text = "${endTime.get(Calendar.DAY_OF_MONTH)}.${endTime.get(Calendar.MONTH)}.${endTime.get(Calendar.YEAR)}"

            val time = endTime.time.time - startTime.time.time

            tripTime.text = EventsAdapter.getOurTripTime(time)

            platzkartTV.text = "₴${train.minPrice}.00"
            coupeTV.text = "₴${train.midPrice}.00"
            svTV.text = "₴${train.maxPrice}.00"
        }

        val _1 = Pair(platzkart, platzkartTV)
        val _2 = Pair(coupe, coupeTV)
        val _3 = Pair(sv, svTV)
        platzkart.setOnClickListener {
            if (selectedTab == Platzkart) return@setOnClickListener
            selectedTab = Platzkart
            selectType(_1, _2, _3)
        }
        coupe.setOnClickListener {
            if (selectedTab == Coupe) return@setOnClickListener
            selectedTab = Coupe
            selectType(_2, _1, _3)
        }
        sv.setOnClickListener {
            if (selectedTab == SV) return@setOnClickListener
            selectedTab = SV
            selectType(_3, _2, _1)
        }

        mViewPager.clipChildren = false
        val isLandscape = resources.displayMetrics.widthPixels > resources.displayMetrics.heightPixels
        if (isLandscape)
            mViewPager.pageMargin = dp(-100).toInt()
        else
            mViewPager.pageMargin = dp(-75).toInt()
        mViewPager.offscreenPageLimit = 3

        mViewPager.adapter = mVPAdapter
        mViewPager.setPageTransformer(true, CarouselEffectTransformer(this))
    }

    private fun selectType(selected: Pair<RelativeLayout, AutofitTextView>,
                           second: Pair<RelativeLayout, AutofitTextView>,
                           third: Pair<RelativeLayout, AutofitTextView>){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            selected.first.backgroundTintList = ColorStateList.valueOf(colorAccent)
            second.first.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            third.first.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        } else {
            selected.first.setBackgroundColor(colorAccent)
            second.first.setBackgroundColor(Color.WHITE)
            third.first.setBackgroundColor(Color.WHITE)
        }

        selected.second.setTextColor(Color.WHITE)
        second.second.setTextColor(colorAccent)
        third.second.setTextColor(colorAccent)
    }

    override fun onBackPressed() {
        ic_back.callOnClick()
        super.onBackPressed()
    }

}