package com.tryingtorun.kmpcharts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tryingtorun.kmpcharts.library.AxisConfig
import com.tryingtorun.kmpcharts.library.BarChartConfig
import com.tryingtorun.kmpcharts.library.BottomAxisTicksAndLabelsDrawMethod
import com.tryingtorun.kmpcharts.library.ChartConfig
import com.tryingtorun.kmpcharts.library.ChartDataPoint
import com.tryingtorun.kmpcharts.library.HorizontalGuideLineConfig
import com.tryingtorun.kmpcharts.library.LineChartConfig
import com.tryingtorun.kmpcharts.library.LineStyle
import com.tryingtorun.kmpcharts.library.PopupConfig
import com.tryingtorun.kmpcharts.library.RangeRectangleConfig
import com.tryingtorun.kmpcharts.library.Sample
import com.tryingtorun.kmpcharts.library.Scale
import kotlin.random.Random

class Config {

    companion object {

        @Composable
        fun getLineChartSampleConfig(data: List<ChartDataPoint>): LineChartConfig {

            val avg = data.sumOf { it.yValue } / data.size
            val min = data.minOf { it.yValue }
            val max = data.maxOf { it.yValue }

            return LineChartConfig(
                chartConfig = ChartConfig(
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
                            color = Color.Red,
                            yValue = max.toFloat(),
                            labelStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp
                            ),
                        )
                    ),
                    bottomAxisMethod = BottomAxisTicksAndLabelsDrawMethod.DIVIDE_EQUALLY,
                    bottomAxisConfig = AxisConfig(
                        valueFormatter = {
                            it.toInt().toDateString()
                        },
                        numberOfLabelsToShow = 5, // don't want clutter on the bottom axis then change this, depending on your data set
                        shiftLastLabel = true, // shift the last label to the left to avoid clipping if not using rightgutterwidth above
                        shiftFirstLabel = true,
                    ),
                    leftAxisConfig = AxisConfig(
                        valueFormatter = {
                            it.toCurrencyString()
                        },
                        numberOfLabelsToShow = 10,
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
        }


        @Composable
        fun getLineChartMinimalConfig(data: List<ChartDataPoint>): LineChartConfig {
            return LineChartConfig()
        }

        @Composable
        fun getBarchartMinimalConfig(data: List<ChartDataPoint>): BarChartConfig {
            return BarChartConfig(chartConfig = ChartConfig(
                leftAxisConfig = AxisConfig(
                    scale = Scale(0.0),
                    showLabels = false,
                    showTicks = false
                )
            ))
        }


        @Composable
        fun getBarchartSampleConfig(data: List<ChartDataPoint>): BarChartConfig {

            val averageTemp = Sample.irelandMonthlyTemperatureData.sumOf { it.yValue } / data.size
            val max = data.maxOf { it.yValue }
            val min = data.minOf { it.yValue }

            val brushes = data.map { it ->
                val red = Random.nextFloat()
                val green = Random.nextFloat()
                val blue = Random.nextFloat()
                SolidColor(Color(red = red, green = green, blue = blue))
            }

            return BarChartConfig(
                labelFormatter = { i, data ->

                    if (i % 2 == 0)  // show label for every second bar only to avoid clutter
                        ""
                    else
                        "${data.yValue.toInt()}°C"
                },
                barFillBrushes = brushes,
                chartConfig = ChartConfig(
                    popupConfig = PopupConfig(
                        valueFormatter = {
                            "${it.toInt()}°C"
                        },
                        valueTextColor = MaterialTheme.colorScheme.onSurface,
                        summaryTextColor = MaterialTheme.colorScheme.onSurface,
                    ),
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
                            color = Color.Red,
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
                    ),
                    leftAxisConfig = AxisConfig(
                        scale = Scale(min=0.0, max=if (max > 18.0) max else 20.0),
                        valueFormatter = {
                            "${it.toInt()}°C"
                        },
                        numberOfLabelsToShow = 5,
                        showLabels = true,
                        showTicks = true,
                    )
                )
            )
        }
    }
}