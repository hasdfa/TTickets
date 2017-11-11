package com.vadim.hasdfa.traintickets._core

import android.content.Context
import android.support.v7.app.AppCompatActivity

/**
* Created by Raksha Vadim on 22.09.17.
*/
open class ActivityBase() : AppCompatActivity(), BaseFunctions {
    override var isAnimatingIn1: Boolean = false

    override fun context(): Context {
        return this
    }
}