package nuntium.fhooe.at.nuntium.Utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import nuntium.fhooe.at.nuntium.R

fun View.shakeErrorView() {
    if(!this.hasFocus()) this.backgroundTintList = ContextCompat.getColorStateList(this.context, R.color.colorAccent)

    val animation = ObjectAnimator.ofFloat(this, "x", -20f, 20f).apply {
        duration = 150
        repeatCount = 2
    }

    animation.interpolator = AccelerateDecelerateInterpolator()

    animation.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            this@shakeErrorView.x = 0f
        }
    })
    animation.start()

    this.setOnFocusChangeListener { view, hasFocus ->
        if(hasFocus) view.backgroundTintList = ContextCompat.getColorStateList(view.context, R.color.edittext_color)
    }
}