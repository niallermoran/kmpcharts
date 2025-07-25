This is a Kotlin Multiplatform charts project targeting Android and iOS.

To Setup on your Compose/Kotlin multiplatform project:

* Add the latest version from Maven Central to the [versions] section of your libs.versions.toml file, e.g. `kmpcharts = "0.2.3-alpha"`
* Add the following then to your libraries section kmpcharts = { module = "com.tryingtorun:kmpcharts", version.ref = "kmpcharts" }
* In the commonMain section add  implementation(libs.kmpcharts)

To Use the Library:

* Currently the library only contains the following chart types with more planned
  * LineChart
  * BarChart
* Each chart accepts a list of ChartDataPoint objects and a config object.
* You can generate a chart once you have data with minimal configuration.
* Each config object will contain any chart specific configuration plus common configuration objects that you can use across charts for consistency, e.g. axis configurations.
* Each chart supports the following capabilities as of the time of writing:
  * Data provided as a combination of X and Y Doubles and a summary string that you can define, which gets shown on popups.
  * Each axis can have it's own formatter to dictate how the relevant Double value is presented on screen.
  * Each axis can be controlled in terms of ticks/axis line and label display.
  * Each chart can have a popup box that gets displayed when a user taps and drags their finger horizontally across the chart, snapping (with haptic feedback) to exact data points.
  * Each chart can have a range rectangle displayed with control over styling as well as adding some text. This is useful for displaying target ranges.
  
To see a complete example of how to setup the charts please see the sampleapp in `composeApp` folder of the repo. It is a multiplatform app that uses the kmpcharts library for iOS and Android.

The charts library was created to fulfill the requirements of the tryingtorun app. So if you are a runner and want to simplify your running data into meaningful guidance then check it out at [www.tryingtorun.com](https://www.tryingtorun.com) 
