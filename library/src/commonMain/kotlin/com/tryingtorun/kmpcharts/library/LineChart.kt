package com.tryingtorun.kmpcharts.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity


/**
 * Composable BarChart that supports interactive features
 */
@Composable
fun LineChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    config: LineChartConfig = LineChartConfig()
) {
    val density = LocalDensity.current
    Chart(
        data = data,
        modifier = modifier,
        config = config.chartConfig,
        density = density
    ) { coordinates, chartDimensions ->
        drawChartLine(
            coordinates = coordinates,
            config = config,
            chartDimensions = chartDimensions
        )
    }
}

/**
 * Draws X-axis value labels
 */
private fun DrawScope.drawChartLine(
    coordinates: List<DataPointPlotCoordinates>,
    config: LineChartConfig,
    chartDimensions: ChartDimensions
) {

    val smoothPath = createSmoothPath(coordinates)
    val filledPath = Path().apply {
        addPath(smoothPath)
        lineTo(coordinates.last().x, with(density) { chartDimensions.plotAreaHeight.toPx() })
        lineTo(coordinates.first().x, with(density) { chartDimensions.plotAreaHeight.toPx() })
        close()
    }

    if( config.fillLine ) {
        drawPath(
            path = filledPath,
            brush = config.fillBrush
        )
    }

    // Draw smooth line
    if (config.lineStyle.display) {
        drawPath(
            path = smoothPath,
            color = config.lineStyle.color,
            style = config.lineStyle.stroke
        )
    }

}


/**
 * Creates a smooth path using cubic Bezier curves
 */
private fun createSmoothPath(points: List<DataPointPlotCoordinates>): Path {

    if (points.size < 2) return Path()

    val path = Path()
    path.moveTo(points[0].x, points[0].y)

    if (points.size == 2) {
        path.lineTo(points[1].x, points[1].y)
        return path
    }

    // Calculate control points for smooth curves
    val controlPoints = calculateControlPoints(points)

    for (i in 1 until points.size) {
        val currentPoint = points[i]
        val controlPoint1 = controlPoints[i - 1].first
        val controlPoint2 = controlPoints[i - 1].second

        //path.lineTo(currentPoint.x, currentPoint.y)

        path.cubicTo(
            controlPoint1.x, controlPoint1.y,
            controlPoint2.x, controlPoint2.y,
            currentPoint.x, currentPoint.y
        )


    }

    return path
}


/**
 * Calculates control points for cubic Bezier curves
 */
private fun calculateControlPoints(
    points: List<DataPointPlotCoordinates>
): List<Pair<Offset, Offset>> {
    val controlPoints = mutableListOf<Pair<Offset, Offset>>()

    for (i in 0 until points.size - 1) {
        val currentPoint = points[i]
        val nextPoint = points[i + 1]

        val controlPoint1 = Offset(
            (currentPoint.x + nextPoint.x)/2,
            currentPoint.y
        )

        val controlPoint2 = Offset(
            (currentPoint.x + nextPoint.x)/2,
            nextPoint.y
        )

        controlPoints.add(Pair(controlPoint1, controlPoint2))
    }

    return controlPoints
}