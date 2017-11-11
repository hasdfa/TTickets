package com.vadim.hasdfa.traintickets.Controller.Fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.TabHost
import com.vadim.hasdfa.traintickets.Controller.Activities.MainActivity
import com.vadim.hasdfa.traintickets.R
import java.util.*

/**
* Created by Raksha Vadim on 28.09.2017.
*/
class DateRangePickerFragment: DialogFragment(), View.OnClickListener {

    private var onDateRangeSelectedListener: OnDateRangeSelectedListener? = null

    private var tabHost: TabHost? = null
    private var startDatePicker: DatePicker? = null
    private var endDatePicker: DatePicker? = null
    private var butSetDateRange: Button? = null
    internal var is24HourMode: Boolean = false
    private var ticketType: MainActivity.TicketType = MainActivity.TicketType.Single

    private var selectedTab: SelectedTab = SelectedTab.Start

    fun initialize(callback: OnDateRangeSelectedListener,
                   is24HourMode: Boolean,
                   ticketType: MainActivity.TicketType) {
        onDateRangeSelectedListener = callback
        this.is24HourMode = is24HourMode
        this.ticketType = ticketType
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.date_range_picker, container, false)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        tabHost = root.findViewById(R.id.tabHost)
        butSetDateRange = root.findViewById<Button>(R.id.but_set_time_range)
        startDatePicker = root.findViewById(R.id.start_date_picker)
        endDatePicker = root.findViewById(R.id.end_date_picker)
        butSetDateRange!!.setOnClickListener(this)
        tabHost!!.findViewById<TabHost>(R.id.tabHost)
        tabHost!!.setup()
        val startDatePage = tabHost!!.newTabSpec("start")
        startDatePage.setContent(R.id.start_date_group)
        startDatePage.setIndicator(getString(R.string.title_tab_start_date))

        startDatePicker?.minDate = Date().time

        butSetDateRange?.text = "Next"

        val endDatePage = tabHost!!.newTabSpec("end")
        endDatePage.setContent(R.id.end_date_group)
        endDatePage.setIndicator(getString(R.string.title_tab_end_date))

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        tabHost!!.addTab(startDatePage)
        if (ticketType == MainActivity.TicketType.Return) {
            tabHost!!.addTab(endDatePage)
            butSetDateRange?.text = "Next"
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        if (dialog == null)
            return
        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }


    fun setOnDateRangeSelectedListener(callback: OnDateRangeSelectedListener) {
        this.onDateRangeSelectedListener = callback
    }

    override fun onClick(v: View) {
        val startDate = Calendar.getInstance().date(startDatePicker!!.dayOfMonth, startDatePicker!!.month, startDatePicker!!.year)

        if (ticketType == MainActivity.TicketType.Single) {
            dismiss()
            onDateRangeSelectedListener!!.onDateSingleSelected(startDate)
            return
        }

        if (selectedTab == SelectedTab.Start) {
            endDatePicker?.minDate = startDate.time.time

            butSetDateRange?.text = "Select"
            tabHost?.currentTab = 1
            selectedTab = SelectedTab.End
        } else {
            dismiss()
            val endDate = Calendar.getInstance().date(endDatePicker!!.dayOfMonth, endDatePicker!!.month, endDatePicker!!.year)
            onDateRangeSelectedListener!!.onDateRangeSelected(startDate, endDate)
        }
    }

    interface OnDateRangeSelectedListener {
        fun onDateRangeSelected(startDate: Calendar, endDate: Calendar)
        fun onDateSingleSelected(date: Calendar)
    }

    companion object {
        fun newInstance(callback: DateRangePickerFragment.OnDateRangeSelectedListener, ticketType: MainActivity.TicketType, is24HourMode: Boolean = false): DateRangePickerFragment {
            val dateRangePickerFragment = DateRangePickerFragment()
            dateRangePickerFragment.initialize(callback, is24HourMode, ticketType)
            return dateRangePickerFragment
        }
    }

    private fun Calendar.date(dayOfMonth: Int, month: Int, year: Int): Calendar{
        this.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        this.set(Calendar.MONTH, month)
        this.set(Calendar.YEAR, year)
        return this
    }

    private enum class SelectedTab { Start, End }

}// Required empty public constructor