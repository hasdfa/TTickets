package com.vadim.hasdfa.traintickets._core

/**
* Created by Raksha Vadim on 27.09.2017.
*/
class Log private constructor() {
    companion object {
        public fun d(tag: String, message: Any) {
            android.util.Log.d(tag, message.toString())
        }
        public fun d(message: Any) {
            android.util.Log.d("myLog", message.toString())
        }
        public fun e(tag: String, message: Any) {
            android.util.Log.e(tag, message.toString())
        }
        public fun e(message: Any) {
            android.util.Log.e("myLog", message.toString())
        }
    }
}