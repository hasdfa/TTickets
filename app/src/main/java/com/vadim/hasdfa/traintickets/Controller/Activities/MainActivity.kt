package com.vadim.hasdfa.traintickets.Controller.Activities

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.vadim.hasdfa.traintickets.Controller.Adapters.EventsAdapter
import com.vadim.hasdfa.traintickets.Controller.Fragments.DateRangePickerFragment
import com.vadim.hasdfa.traintickets.Model.GenerateData
import com.vadim.hasdfa.traintickets.R
import com.vadim.hasdfa.traintickets._core.ActivityBase
import com.vadim.hasdfa.traintickets._core.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import me.grantland.widget.AutofitHelper
import java.util.*
import kotlin.collections.HashMap

/**
* Created by Raksha Vadim on 22.09.17.
*/
class MainActivity : ActivityBase() {

    private var selectedTab = SelectedTab.PlanBuy
    private var selectedType = TicketType.Return

    private val divider = 2

    private val actionBarSize by lazy {
        (underline.y + dp(8)).toInt()
    }

    private var mAdapter: EventsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AutofitHelper.create(textViewFrom)
        AutofitHelper.create(textViewTo)

        launch(CommonPool) {
            GenerateData.run {
                shared(resources.getStringArray(R.array.RailwayStations)).run {
                    generateDataAsync(250, 10) {
                        runOnUiThread {
                            mAdapter = EventsAdapter(it)
                            mRecyclerView.isNestedScrollingEnabled = false
                            mRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                            mRecyclerView.adapter = mAdapter
//                          mAdapter.search()
                            mAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }



        val lp = underline.layoutParams as ViewGroup.MarginLayoutParams
        lp.width = (planBuy.length() * planBuy.textSize / divider).toInt()
        lp.marginStart = 20
        Log.d(lp.width)
        underline.layoutParams = lp

        val orgTH = toolbar.layoutParams.height
        var toolbarHeight: Int

        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY

        date1.text = calendar.toStringDate()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        date2.text = calendar.toStringDate()

        textViewTo.setAdapter(
                ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.RailwayStations)
                )
        )
        textViewFrom.setAdapter(
                ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.RailwayStations)
                )
        )

        planBuy.setOnClickListener {
            if (selectedTab == SelectedTab.PlanBuy || isAnimating) return@setOnClickListener
            isAnimating = true

            val ulp = underline.layoutParams as ConstraintLayout.LayoutParams

            makeAnimation(ulp.width, (planBuy.length() * planBuy.textSize / divider).toInt()) {
                ulp.width = it.animatedValue as Int
                underline.layoutParams = ulp
            }
            makeAnimation(ulp.marginStart, 20) {
                ulp.marginStart = it.animatedValue as Int
                underline.layoutParams = ulp
            }

            if (selectedType == TicketType.Single) {
                selectedType = TicketType.Return
                leftBack.callOnClick()
            } else {
                selectedType = TicketType.Single
                rightBack.callOnClick()
            }

            planBuy.setTextColor(colorAccent)
            liveTimes.setTextColor(colorPrimaryLight)


            selectedTab = SelectedTab.PlanBuy
        }
        liveTimes.setOnClickListener {
            if (selectedTab == SelectedTab.LiveTimes || isAnimating) return@setOnClickListener
            isAnimating = true

            val ulp = underline.layoutParams as ConstraintLayout.LayoutParams

            makeAnimation(ulp.width, (liveTimes.length() * liveTimes.textSize / divider).toInt()) {
                ulp.width = it.animatedValue as Int
                underline.layoutParams = ulp
            }
            makeAnimation(ulp.marginStart, ((planBuy.length() * planBuy.textSize / divider) + (23 * resources.displayMetrics.density)).toInt()) {
                ulp.marginStart = it.animatedValue as Int
                underline.layoutParams = ulp
            }

            val tlp = toolbar.layoutParams
            makeAnimation(tlp.height, actionBarSize) {
                tlp.height = it.animatedValue as Int
                toolbar.layoutParams = tlp
            }

            liveTimes.setTextColor(colorAccent)
            planBuy.setTextColor(colorPrimaryLight)

            selectedTab = SelectedTab.LiveTimes
        }




        leftBack.setOnClickListener {
            if (selectedType == TicketType.Single) return@setOnClickListener

            selectType(
                    colorAccent,
                    Color.WHITE,
                    Color.WHITE,
                    colorPrimary
            )

            val tlp = toolbar.layoutParams
            toolbarHeight = (date2.y - dp(5)).toInt()
            makeAnimation(tlp.height, toolbarHeight) {
                tlp.height = it.animatedValue as Int
                toolbar.layoutParams = tlp
            }

            selectedType = TicketType.Single
        }
        rightBack.setOnClickListener {
            if (selectedType == TicketType.Return) return@setOnClickListener

            selectType(
                    Color.WHITE,
                    colorAccent,
                    colorPrimary,
                    Color.WHITE
            )

            val tlp = toolbar.layoutParams
            toolbarHeight = orgTH
            makeAnimation(tlp.height, toolbarHeight) {
                tlp.height = it.animatedValue as Int
                toolbar.layoutParams = tlp
            }

            selectedType = TicketType.Return
        }

        val selectDate = {
            val dateRangePickerFragment = DateRangePickerFragment.newInstance(object: DateRangePickerFragment.OnDateRangeSelectedListener {
                override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                    date1.text = startDate.toStringDate()
                    date2.text = endDate.toStringDate()
                }
                override fun onDateSingleSelected(date: Calendar) {
                    date1.text = date.toStringDate()
                    date2.text = date.toStringDate()
                }
            }, selectedType)
            dateRangePickerFragment.show(supportFragmentManager, "datePicker")
        }

        date1.setOnClickListener { selectDate() }
        calendar1.setOnClickListener { selectDate() }

        date2.setOnClickListener { selectDate() }
        calendar2.setOnClickListener { selectDate() }


        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private fun selectType(clr1: Int, clr2: Int, clr3: Int, clr4: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leftBack.backgroundTintList = ColorStateList.valueOf(clr1)
            rightBack.backgroundTintList = ColorStateList.valueOf(clr2)
        } else {
            leftBack.setBackgroundColor(clr1)
            rightBack.setBackgroundColor(clr2)
        }

        simpleArrow.setColorFilter(clr3)
        doubleArrow.setColorFilter(clr4)
    }

    enum class SelectedTab { PlanBuy, LiveTimes }
    enum class TicketType { Single, Return }

    private fun Calendar.toStringDate(): String {
        val dayOfWeek = getDisplayNames(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
                .sortByKeyAndSwap()
                .values
                .toList()[get(Calendar.DAY_OF_WEEK)-1]

        val month = getDisplayNames(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                .sortByKeyAndSwap()
                .values
                .toList()[get(Calendar.MONTH)]

        return "$dayOfWeek, ${get(Calendar.DAY_OF_MONTH)} $month"
    }


    /**
     * Сортирует Map по ключам и меняет их местами со значениями
     */
    private fun <K, V> Map<K, V>.sortByKeyAndSwap(): Map<V, K> {
        val newMap = HashMap<V, K>()

        this.keys.distinct().forEach {
            newMap.put(this[it]!!, it)
        }
        this.forEach { newMap.put(it.value, it.key) }

        return newMap
    }
}
