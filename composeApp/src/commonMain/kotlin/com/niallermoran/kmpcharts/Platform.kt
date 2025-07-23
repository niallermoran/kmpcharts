package com.niallermoran.kmpcharts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform