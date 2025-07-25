package com.tryingtorun.kmpcharts

interface Platform {
    val name: String
}

expect fun Double.toCurrencyString(): String

expect fun getPlatform(): Platform