package com.vadim.hasdfa.traintickets.Controller

import android.content.Intent
import android.os.Bundle
import com.vadim.hasdfa.traintickets.Controller.Activities.MainActivity
import com.vadim.hasdfa.traintickets._core.ActivityBase

/**
* Created by Raksha Vadim on 22.09.17.
*/
class SplashScreen: ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}