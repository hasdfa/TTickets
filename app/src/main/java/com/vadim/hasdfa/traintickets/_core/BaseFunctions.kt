package com.vadim.hasdfa.traintickets._core

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.GridLayoutManager
import com.vadim.hasdfa.traintickets.R

/**
* Created by Raksha Vadim on 22.09.17.
*/
interface BaseFunctions {
    fun context(): Context

// MARK: UI helpers

    @SuppressLint("RestrictedApi")
    fun showAlert(message: String?, onDismiss: () -> Unit){
        val alertDialog = AlertDialog.Builder(ContextThemeWrapper(context(), R.style.AlertDialogCustom))
        var alert: AlertDialog? = null
        val messages = message ?: return
        alertDialog.setTitle(messages)
        alertDialog.setPositiveButton("OK", { _, _ ->
            alert?.dismiss()
            onDismiss()
        })
        alertDialog.setOnDismissListener { onDismiss() }
        alert = alertDialog.create()
        alert.show()
    }

    fun getAdaptiveGridLM(): GridLayoutManager {
        val columnCount = when {
            isTablet -> if(isLandscape) 4 else 3
            isMidTablet -> if(isLandscape) 5 else 4
            isBigTablet -> if(isLandscape) 6 else 5
            isLandscape -> 4
            else -> 3
        }
        return GridLayoutManager(context(), columnCount)
    }


    fun dp(dp: Int): Float {
        return dp * context().resources.displayMetrics.density
    }

    var isAnimating: Boolean

    fun makeAnimation(from: Int, to: Int, duration: Long = 300, after: (Animator?) -> Unit = {}, with: (ValueAnimator) -> Unit){
        val animator = ValueAnimator.ofInt(from, to)
        animator.addUpdateListener { with(it) }
        animator.addListener(object: Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                isAnimating = false
                after(p0)
            }
            override fun onAnimationStart(p0: Animator?) { isAnimating = true }
            override fun onAnimationCancel(p0: Animator?) {
                isAnimating = false
                after(p0)
            }
            override fun onAnimationRepeat(p0: Animator?) { isAnimating = true }
        })
        animator.duration = duration
        animator.start()
    }

    // MARK: deviceInfo
    val configuration: Configuration
        get() { return context().resources.configuration }

    val isLandscape: Boolean
        get() { return with(configuration) { screenWidthDp > screenHeightDp } }

    val isTablet: Boolean get() {
        return configuration.smallestScreenWidthDp >= 620
    }
    val isSmallTablet: Boolean get() {
        return configuration.smallestScreenWidthDp in 620..700
    }
    val isMidTablet: Boolean get() {
        return configuration.smallestScreenWidthDp in 700..799
    }
    val isBigTablet: Boolean get() {
        return configuration.smallestScreenWidthDp >= 800
    }


    // MARK: Extensions
    fun Int.toStringOrNaN() = if (this <= 0) { "NaN" } else "$this"


    // MARK: resources
    @ColorInt
    fun color(@ColorRes colorId: Int): Int {
        return context().resources.getColor(colorId)
    }
    fun str(@StringRes stringId: Int): String {
        return context().resources.getString(stringId)
    }
    fun drawable(@DrawableRes drawableId: Int): Drawable {
        return context().resources.getDrawable(drawableId)
    }

    val colorPrimary        : Int get() {return color(R.color.colorPrimary)}
    val colorPrimaryDark    : Int get() {return color(R.color.colorPrimaryDark)}
    val colorPrimaryLight   : Int get() {return color(R.color.colorPrimaryLight)}
    val colorAccent         : Int get() {return color(R.color.colorAccent)}
}