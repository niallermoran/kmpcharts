package com.tryingtorun.kmpcharts

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun Double.toCurrencyString(): String {
    TODO("Not yet implemented")
}