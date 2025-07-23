package com.niallermoran.kmpcharts.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity


/**
 * Composable BarChart that supports interactive features
 */
@Composable
fun BarChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    config: BarChartConfig = BarChartConfig()
) {

    val density = LocalDensity.current

    Chart(
        density=density,
        data = data,
        modifier = modifier,
        config = config.chartConfig
    ) { coordinates, chartDimensions ->

        drawBars(
            barDimensions = ChartHelper.getBarDimensions(
                density = density,
                chartDimensions = chartDimensions,
                config = config,
                data = data
            ),
            config = config,
            coordinates = coordinates
        )
    }
}


/**
 * Draws X-axis value labels
 */
private fun DrawScope.drawBars(
    coordinates: List<DataPointPlotCoordinates>,
    barDimensions: BarDimensions,
    config: BarChartConfig,
) {

    coordinates.forEachIndexed { index, coordinates ->

        drawRoundRect(
            brush = config.barFillBrush,
            topLeft = Offset(coordinates.x - barDimensions.barWidthInPixels / 2, coordinates.y),
            size = Size(barDimensions.barWidthInPixels, size.height - coordinates.y),
            cornerRadius = CornerRadius(config.barCornerRadius, config.barCornerRadius)
        )

    }

}


