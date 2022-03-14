package com.jejec.mymoviedb.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import com.faltenreich.skeletonlayout.Skeleton
import com.google.android.material.snackbar.Snackbar
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.util.Constant.DATE_FORMAT
import com.jejec.mymoviedb.util.Constant.DATE_YEAR
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

fun Context.convertDpToPx(dp: Int): Int {
    return (dp * (this.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun Int?.fromMinutesToHHmm(): String {
    this?.let {
        val hours = TimeUnit.MINUTES.toHours(this.toLong())
        val remainMinutes = this - TimeUnit.HOURS.toMinutes(hours)
        return String.format("%02dh %02dm", hours, remainMinutes)
    }
    return "Exception format"
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.snackWithAction(message: String, indefinite: Boolean = false, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(
        this,
        message,
        if (indefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
    )
    snack.f()
    snack.show()
}

fun String.formatDate(): String? {
    try {
        val input = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val output = SimpleDateFormat(DATE_YEAR, Locale.getDefault())
        val getAbbreviate = input.parse(this)
        getAbbreviate?.let {
            return output.format(it)
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return ""
}