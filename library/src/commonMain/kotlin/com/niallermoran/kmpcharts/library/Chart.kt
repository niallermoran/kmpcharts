package com.niallermoran.kmpcharts.library

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset


/**
 * Composable that can be used to create a custom chart, e.g. LineChart and/or barchart
 * @param data - list of data points to be plotted
 * @param modifier - modifier for the chart
 * @param config - configuration for the chart
 * @param drawChart - lambda to draw the chart
 */
@Composable
fun Chart(
    density: Density,
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    config: ChartConfig = ChartConfig(),
    drawChart: DrawScope.(
        coordinates: List<DataPointPlotCoordinates>,
        chartDimensions: ChartDimensions
    ) -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current

    if (data.isEmpty()) return

    /**
     * Variables used to control cross-hairs and popup
     */
    var selectedIndex by remember { mutableStateOf<Int?>(null) } // the closest data point to where the drag gesture is
    var previousSelectedIndex by remember { mutableStateOf<Int?>(null) } // the closest data point to where the drag gesture is

    var dragPosition by remember { mutableStateOf(Offset.Zero) } // the current offset from origin
    var showPopup by remember { mutableStateOf(false) }

    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {

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
                        val newPosition = dragPosition + Offset( x= change, y = 0f)
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
                        drawChart(
                            coordinates,
                            chartDimensions,
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
                                    x = 0f,
                                    y = yBottom
                                ),
                                size = Size(
                                    width = chartDimensions.plotAreaWidth.toPx(density),
                                    height = yTop - yBottom
                                )
                            )

                            val textX =
                                (chartDimensions.plotAreaWidthPixels / 2) - textMeasurer.measure(
                                    config.rangeRectangleConfig.label
                                ).size.width / 2

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
}