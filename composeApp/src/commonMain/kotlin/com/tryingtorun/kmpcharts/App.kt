package com.tryingtorun.kmpcharts


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tryingtorun.kmpcharts.library.BarChart
import com.tryingtorun.kmpcharts.library.LineChart
import com.tryingtorun.kmpcharts.library.Sample
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {

    var showMinimumConfig by rememberSaveable { mutableStateOf(false) }
    var showBarChartFullScreen by rememberSaveable { mutableStateOf(false) }
    var showLineChartFullScreen by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        showBarChartFullScreen = false
        showLineChartFullScreen = false
    }

    AppTheme {

        Scaffold { padding ->

            val inFullScreen = showLineChartFullScreen || showBarChartFullScreen
            var columnModifier = Modifier.padding(padding).padding(12.dp).fillMaxSize()

            if (!inFullScreen)
                columnModifier = columnModifier.verticalScroll(rememberScrollState())

            Column(
                modifier = columnModifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                if (!inFullScreen) {
                    Text(text = "Platform: ${getPlatform().name}")

                    Text(text = "Tap and Drag Charts to see Popup")

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = {
                            showMinimumConfig = !showMinimumConfig
                        })
                    ) {
                        Checkbox(
                            checked = showMinimumConfig,
                            onCheckedChange = {
                                showMinimumConfig = !showMinimumConfig
                            },
                            enabled = true,
                        )
                        Text(text = "Show Minimum Configuration")
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "Monthly Average Irish Temperatures (Celsius)",
                        textAlign = TextAlign.Center,
                        fontWeight = Bold,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                if (!showLineChartFullScreen || showBarChartFullScreen) {

                    val modifier = if (showBarChartFullScreen)
                        Modifier.fillMaxSize()
                    else
                        Modifier.height(300.dp).fillMaxWidth()

                    Box(modifier = modifier.padding(12.dp).clickable(onClick = {
                        showBarChartFullScreen = !showBarChartFullScreen
                    })) {
                        val data = Sample.irelandMonthlyTemperatureData
                        BarChart(
                            data = data,
                            config = if (showMinimumConfig) Config.getBarchartMinimalConfig(data) else Config.getBarchartSampleConfig(
                                data
                            )
                        )
                    }
                }

                if (!inFullScreen) {
                    Spacer(modifier = Modifier.height(22.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(22.dp))


                    Text(
                        text = "2024 Bitcoin Price Trend",
                        fontWeight = Bold,
                        textAlign = TextAlign.Center
                    )
                }

                if (!showBarChartFullScreen || showLineChartFullScreen) {

                    val lineChartModifier = if (showLineChartFullScreen)
                        Modifier.fillMaxSize()
                    else
                        Modifier.height(300.dp).fillMaxWidth()

                    Box(modifier = lineChartModifier.padding(12.dp).clickable(onClick = {
                        showLineChartFullScreen = !showLineChartFullScreen
                    })) {
                        val data = Sample.bitcoinWeekly2024
                        LineChart(
                            data = data,
                            lineChartConfig = if (showMinimumConfig) Config.getLineChartMinimalConfig(
                                data
                            ) else Config.getLineChartSampleConfig(data)
                        )
                    }

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
    return "${dt.date.day} ${(dt.date.month.ordinal + 1).toMonthShortName()}"
}


