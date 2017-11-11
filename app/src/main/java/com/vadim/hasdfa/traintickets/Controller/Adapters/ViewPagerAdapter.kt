package com.vadim.hasdfa.traintickets.Controller.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.TextView
import com.vadim.hasdfa.traintickets.Model.Wagon
import com.vadim.hasdfa.traintickets.R

/**
* Created by Raksha Vadim on 03.10.2017.
*/
class ViewPagerAdapter(fm: FragmentManager, private val array: ArrayList<Wagon>) : FragmentPagerAdapter(fm) {
    private var callBack: (Int) -> Unit = {
        throw NotImplementedError()
    }

    public fun setCallBack(callback: (Int) -> Unit){
        this.callBack = callback
    }

    override fun getItem(position: Int): Fragment {
        if (position < 0) {
            notifyDataSetChanged()
        }
        return PlaceholderFragment.newInstance(position, array, callBack)
    }

    override fun getCount(): Int {
        return array.size
    }

    override fun getPageWidth(position: Int): Float {
        return 0.75f
    }

    class PlaceholderFragment: android.support.v4.app.Fragment() {
        var position = 0
        var array: ArrayList<Wagon> = ArrayList()

        private var callBack: (Int) -> Unit = {
            throw NotImplementedError()
        }

        override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?,
                                  savedInstanceState: android.os.Bundle?): android.view.View? {
            if (position < 1) {
                throw NotImplementedError()
            }

            val rootView = inflater!!
                    .inflate(R.layout.c_item, container, false)
            val textView: TextView = rootView.findViewById(R.id.mainTextView)
            textView.text = "Вагон $position"

            rootView.setOnClickListener {
                callBack(position)
            }

            return rootView
        }

        companion object {
            fun newInstance(sectionNumber: Int, array: ArrayList<Wagon>, callBack: (Int) -> Unit): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                fragment.position = sectionNumber + 1
                fragment.array = array
                fragment.callBack = callBack
                return fragment
            }
        }
    }
}
