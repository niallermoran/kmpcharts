package com.tryingtorun.kmpcharts


class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()



actual fun Double.toCurrencyString(): String {
    return "%.2f"
}