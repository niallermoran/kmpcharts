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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
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
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

import kotlinx.datetime.*


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
                    val ninetyFivePercentRange = Sample.getTemperatures95Range()
                    BarChart(   
                        data = Sample.irelandMonthlyTemperatureData,
                        config = BarChartConfig(
                            chartConfig = ChartConfig(
                                rangeRectangleConfig = RangeRectangleConfig(
                                    display = true,
                                    minY = ninetyFivePercentRange.first.toFloat(),
                                    maxY = ninetyFivePercentRange.second.toFloat(),
                                    label = "68% Range - ${ninetyFivePercentRange.first.toInt()}°C-${ninetyFivePercentRange.second.toInt()}°C",

                                ),
                                leftGutterWidth = 15.dp, // use a small gutter so that the first and last bar are clearly visible. Change this based on your data set
                                rightGutterWidth = 15.dp,
                                bottomAxisConfig = AxisConfig(
                                    valueFormatter = {
                                        when (it.toInt()) {
                                            in 1..12 -> Month(it.toInt()).name.toLowerCase(Locale.current)
                                                .take(3)

                                            else -> throw IllegalArgumentException("Month number must be between 1 and 12")
                                        }
                                    },
                                    numberOfLabelsToShow = 4, // don't want clutter on the bottom axis then change this, depending on your data set
                                    shiftLastLabel = false, // shift the last label to the left to avoid clipping if not using rightgutterwidth above
                                ),
                                leftAxisConfig = AxisConfig(
                                    minValue = 0.0, // make sure we can see a bar for the smallest value by using an even smaller value for the axis (we could calculate this)
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
                    LineChart(
                        data = Sample.bitcoinWeekly2024,
                        config = LineChartConfig(
                            chartConfig = ChartConfig(
                                bottomAxisConfig = AxisConfig(
                                    valueFormatter = {
                                        formatDateFromEpoch(it.toLong())
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

@OptIn(ExperimentalTime::class)
fun formatDateFromEpoch(epochSeconds: Long): String {
    val instant = Instant.fromEpochSeconds(epochSeconds)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = date.month.name.take(3).toInitialCaps()
    val day = date.dayOfMonth.toString().padStart(2, '0')
    return "$day $month"
}

fun String.toInitialCaps(): String {
    return this.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

