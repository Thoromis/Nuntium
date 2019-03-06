package nuntium.fhooe.at.nuntium.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import nuntium.fhooe.at.nuntium.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

fun View.shakeErrorView() {
    if(!this.hasFocus()) this.backgroundTintList = ContextCompat.getColorStateList(this.context, R.color.colorAccent)
    val initialX = this.x
    val animation = ObjectAnimator.ofFloat(this, "x", -20f + initialX, 20f + initialX).apply {
        duration = 150
        repeatCount = 2
    }

    animation.interpolator = AccelerateDecelerateInterpolator()

    animation.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            this@shakeErrorView.x = initialX
        }
    })
    animation.start()

    this.setOnFocusChangeListener { view, hasFocus ->
        if(hasFocus) view.backgroundTintList = ContextCompat.getColorStateList(view.context, R.color.edittext_color)
    }
}

fun String.isValidEmail(): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    return Pattern.compile(expression, Pattern.CASE_INSENSITIVE).matcher(this).matches()
}

fun Date.parseString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return format.format(this)
}

fun String.parseDate(): Date? {
    //2019-03-04T12:18:28.967Z
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(this)
    } else {
        null
    }
}