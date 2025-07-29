package com.tryingtorun.kmpcharts.library

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


/**
 * Composable BarChart that supports interactive features
 */
@Composable
fun LineChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    lineChartConfig: LineChartConfig = LineChartConfig()
) {
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val config = lineChartConfig.chartConfig
    var selectedIndex by remember { mutableStateOf<Int?>(null) } // the closest data point to where the drag gesture is
    var previousSelectedIndex by remember { mutableStateOf<Int?>(null) } // the closest data point to where the drag gesture is
    var dragPosition by remember { mutableStateOf(Offset.Zero) } // the current offset from origin
    var showPopup by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {

        if(data.size > 1) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                val height = maxHeight
                val width = maxWidth
                val heightPixels = with(density) { height.toPx() }

                val chartDimensions = ChartHelper.calculateChartDimensions(
                    density = density,
                    data = data,
                    config = config,
                    textMeasurer = textMeasurer,
                    width = width,
                    height = height
                )

                val coordinates = ChartHelper.getDataPointCoordinates(
                    chartDimensions = chartDimensions,
                    config = config,
                    data = data,
                    density = density
                )


                Box(modifier = Modifier.fillMaxSize()) {

                    /**
                     * Left area axis, ticks and labels
                     */
                    Box(
                        modifier = Modifier
                            .width(chartDimensions.leftAreaWidth)
                    ) {
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            drawLeftAxisLabelsAndTicks(
                                density = density,
                                textMeasurer = textMeasurer,
                                config = config,
                                chartDimensions = chartDimensions,
                                data = data
                            )
                        }
                    }

                    /**
                     * Bottom area axis, ticks and labels
                     */
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartDimensions.bottomAreaHeight)
                            .offset {
                                IntOffset(
                                    0,
                                    (heightPixels - chartDimensions.bottomAreaHeightPixels).toInt()
                                )
                            }
                    ) {

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawBottomAxisLabelsAndTicks(
                                density = density,
                                textMeasurer = textMeasurer,
                                config = config,
                                chartDimensions = chartDimensions,
                                coordinates = coordinates
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartDimensions.plotAreaHeight)
                    ) {

                        val draggableState = rememberDraggableState { change ->

                            previousSelectedIndex = selectedIndex
                            val newPosition = dragPosition + Offset(x = change, y = 0f)
                            val foundIndex = ChartHelper.findClosestDataPointIndex(
                                touchPosition = newPosition,
                                data = data,
                                coordinates = coordinates
                            )
                            selectedIndex = foundIndex
                            dragPosition = newPosition
                            showPopup = foundIndex != null
                            if (selectedIndex != null && selectedIndex != previousSelectedIndex)
                                hapticFeedback.performHapticFeedback(
                                    HapticFeedbackType.LongPress
                                )

                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .draggable(
                                    state = draggableState,
                                    orientation = Orientation.Horizontal,
                                    onDragStarted = { offset ->
                                        dragPosition = offset
                                    },
                                    onDragStopped = {
                                        showPopup = false
                                        selectedIndex = null
                                    }
                                )
                        ) {
                            drawChartLine(
                                coordinates = coordinates,
                                config = lineChartConfig,
                                chartDimensions = chartDimensions
                            )

                            if (config.rangeRectangleConfig.display) {

                                val yTop = ChartHelper.calculateValueYOffSet(
                                    config = config,
                                    data = data,
                                    chartDimensions = chartDimensions,
                                    value = config.rangeRectangleConfig.maxY
                                )

                                val yBottom = ChartHelper.calculateValueYOffSet(
                                    config = config,
                                    data = data,
                                    chartDimensions = chartDimensions,
                                    value = config.rangeRectangleConfig.minY
                                )

                                drawRect(
                                    brush = SolidColor(config.rangeRectangleConfig.color),
                                    topLeft = Offset(
                                        x = chartDimensions.leftAreaWidthPixels,
                                        y = yBottom
                                    ),
                                    size = Size(
                                        width = chartDimensions.plotAreaWidth.toPx(density),
                                        height = yTop - yBottom
                                    )
                                )

                                val textX = chartDimensions.leftAreaWidthPixels +
                                        (chartDimensions.plotAreaWidthPixels / 2) -
                                        textMeasurer.measure(config.rangeRectangleConfig.label).size.width / 2

                                val textHeight =
                                    textMeasurer.measure(config.rangeRectangleConfig.label).size.height
                                val textY =
                                    when (config.rangeRectangleConfig.labelPosition) {
                                        RangeRectangleConfig.LabelPosition.CENTER -> (yBottom + (yTop - yBottom) / 2) - (textHeight / 2)
                                        RangeRectangleConfig.LabelPosition.TOP -> yTop - textHeight
                                        RangeRectangleConfig.LabelPosition.BOTTOM -> yBottom + textHeight
                                    }

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = config.rangeRectangleConfig.label,
                                    style = config.rangeRectangleConfig.labelStyle,
                                    topLeft = Offset(
                                        x = textX,
                                        y = textY
                                    )
                                )
                            }
                        }


                        // Popup overlay
                        if (selectedIndex != null) {

                            val selectedData = data[selectedIndex!!]
                            val selectedCoordinate = coordinates[selectedIndex!!]

                            if (showPopup) {
                                PopupBox(
                                    data = selectedData,
                                    config = config,
                                    chartDimensions = chartDimensions,
                                    density = density,
                                    coordinate = selectedCoordinate
                                )
                            }

                            if (config.crossHairConfig.lineStyle.display) {
                                CrossHairs(
                                    config = config,
                                    coordinate = selectedCoordinate
                                )
                            }
                        }
                    }

                }

            }
        }
        else{
            Box(modifier= Modifier.fillMaxSize().border(width=1.dp, color= Color.LightGray), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text="Not enough data", textAlign = TextAlign.Center)
            }
        }
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