package org.mozilla.rocket.extension

import android.content.Context
import android.util.TypedValue
import android.view.View

fun Context.dpToPx(value: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

fun View.dpToPx(value: Float): Int = context.dpToPx(value)