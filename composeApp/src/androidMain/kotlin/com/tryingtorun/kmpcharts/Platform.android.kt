package com.tryingtorun.kmpcharts

import android.os.Build
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun Double.toCurrencyString(): String {
    val formatter = DecimalFormat("$#,##0")
    return formatter.format(this)
}
