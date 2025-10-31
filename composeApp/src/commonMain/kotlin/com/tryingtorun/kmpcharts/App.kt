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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tryingtorun.kmpcharts.library.AxisConfig
import com.tryingtorun.kmpcharts.library.BarChart
import com.tryingtorun.kmpcharts.library.BarChartConfig
import com.tryingtorun.kmpcharts.library.BottomAxisTicksAndLabelsDrawMethod
import com.tryingtorun.kmpcharts.library.ChartConfig
import com.tryingtorun.kmpcharts.library.HorizontalGuideLineConfig
import com.tryingtorun.kmpcharts.library.LineChart
import com.tryingtorun.kmpcharts.library.LineChartConfig
import com.tryingtorun.kmpcharts.library.PopupConfig
import com.tryingtorun.kmpcharts.library.RangeRectangleConfig
import com.tryingtorun.kmpcharts.library.Sample
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@Composable
@Preview
fun App() {

    AppTheme {

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
                    val averageTemp = Sample.irelandMonthlyTemperatureData.sumOf { it.yValue } / data.size
                    val max = data.maxOf { it.yValue }
                    val min = data.minOf { it.yValue }



                    val brushes = data.map { it ->
                        val red = Random.nextFloat()
                        val green = Random.nextFloat()
                        val blue = Random.nextFloat()
                        SolidColor(Color(red = red, green = green, blue = blue))
                    }


                    BarChart(
                        data = data,
                        config = BarChartConfig(
                            labelFormatter = { i, data ->

                                if( i % 2 == 0 )  // show label for every second bar only to avoid clutter
                                    ""
                                else
                                    "${data.yValue.toInt()}°C"
                            },
                            barFillBrushes = brushes,
                            chartConfig = ChartConfig(
                                defaultAxisTextAndLineColor = MaterialTheme.colorScheme.onSurface,
                                horizontalGuideLines = listOf(
                                    HorizontalGuideLineConfig(
                                        label = "Avg: ${averageTemp.toInt()} ℃",
                                        labelStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 10.sp
                                        ),
                                        labelPosition = HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE,
                                        yValue = averageTemp.toFloat()
                                    ),
                                    HorizontalGuideLineConfig(
                                        label = "Min: ${min.toInt()} ℃",
                                        labelPosition = HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE,
                                        labelStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 10.sp
                                        ),
                                        color = Color.Blue,
                                        yValue = min.toFloat()
                                    ),
                                    HorizontalGuideLineConfig(
                                        label = "Max: ${max.toInt()} ℃",
                                        labelPosition = HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE,
                                        labelStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 10.sp
                                        ),
                                        color = Color.Red   ,
                                        yValue = max.toFloat()
                                    )
                                ),
                                rangeRectangleConfig = RangeRectangleConfig(
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
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                leftAxisConfig = AxisConfig(
                                    minValue = 0.0, // make sure we can see a bar for the smallest value by using an even smaller value for the axis (we could calculate this)
                                    maxValue = if (max > 18.0) max else 20.0,
                                    valueFormatter = {
                                        "${it.toInt()}°C"
                                    },
                                    numberOfLabelsToShow = 5,
                                    color = MaterialTheme.colorScheme.onSurface
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

                    val avg = data.sumOf { it.yValue } / data.size
                    val min = data.minOf { it.yValue }
                    val max = data.maxOf { it.yValue }


                    LineChart(
                        data = data,
                        lineChartConfig = LineChartConfig(
                            chartConfig = ChartConfig(
                                defaultAxisTextAndLineColor = MaterialTheme.colorScheme.onSurface,
                                horizontalGuideLines = listOf(
                                    HorizontalGuideLineConfig(
                                        label = "Avg: $${avg.toInt()}",
                                        labelPosition = HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE,
                                        yValue = avg.toFloat(),
                                        labelStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 10.sp
                                        ),
                                    ),
                                    HorizontalGuideLineConfig(
                                        label = "Min: $${min.toInt()}",
                                        labelPosition = HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE,
                                        color = Color.Blue,
                                        yValue = min.toFloat(),
                                        labelStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 10.sp
                                        ),
                                    ),
                                    HorizontalGuideLineConfig(
                                        label = "Max: $${max.toInt()}",
                                        labelPosition = HorizontalGuideLineConfig.LabelPosition.LEFT_UNDER,
                                        color = Color.Red   ,
                                        yValue = max.toFloat(),
                                        labelStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 10.sp
                                        ),
                                    )
                                ),
                                bottomAxisMethod = BottomAxisTicksAndLabelsDrawMethod.DIVIDE_EQUALLY,
                                bottomAxisConfig = AxisConfig(
                                    display = true,
                                    valueFormatter = {
                                        it.toInt().toDateString()
                                    },
                                    numberOfLabelsToShow = 5, // don't want clutter on the bottom axis then change this, depending on your data set
                                    shiftLastLabel = true, // shift the last label to the left to avoid clipping if not using rightgutterwidth above
                                    shiftFirstLabel = true,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                leftAxisConfig = AxisConfig(
                                    display = false,
                                    valueFormatter = {
                                        it.toCurrencyString()
                                    },
                                    numberOfLabelsToShow = 10,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                popupConfig = PopupConfig(
                                    valueFormatter = {
                                        it.toCurrencyString()
                                    },
                                    valueTextColor = MaterialTheme.colorScheme.onSurface,
                                    summaryTextColor = MaterialTheme.colorScheme.onSurface,
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
    return when (this) {
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

@OptIn(ExperimentalTime::class)
fun Int.toDateString(): String {

    val instant = Instant.fromEpochSeconds(this.toLong())
    val dt = instant.toLocalDateTime(TimeZone.UTC)
    return  "${dt.date.day} ${(dt.date.month.ordinal + 1).toMonthShortName()}"
}


