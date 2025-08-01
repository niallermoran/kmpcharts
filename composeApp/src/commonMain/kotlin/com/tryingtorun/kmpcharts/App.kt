package com.tryingtorun.kmpcharts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tryingtorun.kmpcharts.library.AxisConfig
import com.tryingtorun.kmpcharts.library.BarChart
import com.tryingtorun.kmpcharts.library.BarChartConfig
import com.tryingtorun.kmpcharts.library.ChartConfig
import com.tryingtorun.kmpcharts.library.LineChart
import com.tryingtorun.kmpcharts.library.LineChartConfig
import com.tryingtorun.kmpcharts.library.PopupConfig
import com.tryingtorun.kmpcharts.library.RangeRectangleConfig
import com.tryingtorun.kmpcharts.library.Sample
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {


    MaterialTheme {


        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding).padding(12.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(text = "Platform: ${getPlatform().name}")

                Text(text = "Tap and Drag Charts to see Popup")

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Monthly Average Irish Temperatures (Celsius)",
                    textAlign = TextAlign.Center,
                    fontWeight = Bold,
                    modifier = Modifier.padding(6.dp)
                )


                Box(modifier = Modifier.fillMaxWidth().height(300.dp).padding(12.dp)) {


                    val data = Sample.irelandMonthlyTemperatureData
                    val max = data.maxOf { it.yValue }


                    BarChart(
                        data = data,
                        config = BarChartConfig(
                            chartConfig = ChartConfig(
                                rangeRectangleConfig = RangeRectangleConfig(
                                    display = true,
                                    minY = 10f,
                                    maxY = 18f,
                                    label = "Grass Growth Range",
                                ),
                                leftGutterWidth = 15.dp, // use a small gutter so that the first and last bar are clearly visible. Change this based on your data set
                                rightGutterWidth = 15.dp,
                                bottomAxisConfig = AxisConfig(
                                    valueFormatter = {
                                        when (it.toInt()) {
                                            in 1..12 -> it.toInt().toMonthShortName()
                                                .take(3)

                                            else -> throw IllegalArgumentException("Month number must be between 1 and 12")
                                        }
                                    },
                                    numberOfLabelsToShow = 4, // don't want clutter on the bottom axis then change this, depending on your data set
                                    shiftLastLabel = false, // shift the last label to the left to avoid clipping if not using rightgutterwidth above
                                ),
                                leftAxisConfig = AxisConfig(
                                    minValue = 0.0, // make sure we can see a bar for the smallest value by using an even smaller value for the axis (we could calculate this)
                                    maxValue = if( max > 18.0 ) max else 20.0,
                                    valueFormatter = {
                                        "${it.toInt()}°C"
                                    },
                                    numberOfLabelsToShow = 5,
                                )
                            )
                        )
                    )
                }

                Text(
                    text = "2024 Bitcoin Price Trend",
                    fontWeight = Bold,
                    textAlign = TextAlign.Center
                )

                Box(modifier = Modifier.fillMaxWidth().height(300.dp).padding(12.dp)) {


                    val data = Sample.bitcoinWeekly2024

                    LineChart(
                        data = data,
                        lineChartConfig = LineChartConfig(
                            chartConfig = ChartConfig(
                                bottomAxisConfig = AxisConfig(
                                    valueFormatter = {
                                        it.toInt().toDateString()
                                    },
                                    numberOfLabelsToShow = 4, // don't want clutter on the bottom axis then change this, depending on your data set
                                    shiftLastLabel = true, // shift the last label to the left to avoid clipping if not using rightgutterwidth above
                                ),
                                leftAxisConfig = AxisConfig(
                                    valueFormatter = {
                                        it.toCurrencyString()
                                    },
                                    numberOfLabelsToShow = 5,
                                ),
                                popupConfig = PopupConfig(
                                    valueFormatter = {
                                        it.toCurrencyString()
                                    },
                                )
                            )
                        )
                    )
                }


            }
        }
    }
}



fun Int.toMonthShortName(): String {
    return when(this){
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
}

fun Int.toDateString(): String {
    val instant = Instant.fromEpochSeconds(this.toLong())
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dt.date.dayOfMonth} ${(dt.date.month.ordinal+1).toMonthShortName()}"
}


