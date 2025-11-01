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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.modifier.ModifierLocalModifierNode
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
fun BarChart(
    data: List<ChartDataPoint>,
    modifier: Modifier = Modifier,
    config: BarChartConfig? = null
) {

    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    var selectedIndex by remember { mutableStateOf<Int?>(null) } // the closest data point to where the drag gesture is
    var previousSelectedIndex by remember { mutableStateOf<Int?>(null) } // the closest data point to where the drag gesture is
    var dragPosition by remember { mutableStateOf(Offset.Zero) } // the current offset from origin
    var showPopup by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {

        if (data.isNotEmpty()) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                val height = maxHeight
                val width = maxWidth
                val heightPixels = with(density) { height.toPx() }

                val chartDimensions = ChartHelper.calculateChartDimensions(
                    density = density,
                    data = data,
                    config = config?.chartConfig,
                    textMeasurer = textMeasurer,
                    width = width,
                    height = height
                )

                val barDimensions = ChartHelper.getBarDimensions(
                    density = density,
                    chartDimensions = chartDimensions,
                    config = config,
                    data = data
                )

                val coordinates = ChartHelper.getDataPointCoordinates(
                    chartDimensions = chartDimensions,
                    config = config?.chartConfig,
                    data = data,
                    density = density,
                    barWidth = barDimensions.barWidth
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

                            if( config != null ) {
                                drawLeftAxisLabelsAndTicks(
                                    density = density,
                                    textMeasurer = textMeasurer,
                                    config = config.chartConfig,
                                    chartDimensions = chartDimensions,
                                    data = data
                                )
                            }
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

                        if( config != null ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {

                                drawBottomAxisLabelsAndTicks(
                                    density = density,
                                    textMeasurer = textMeasurer,
                                    config = config.chartConfig,
                                    chartDimensions = chartDimensions,
                                    coordinates = coordinates
                                )
                            }
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

                        var modifier = Modifier.fillMaxSize()
                        if( config?.chartConfig?.popupConfig != null  )
                        {
                            modifier = modifier.draggable(
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
                        }

                        Canvas(
                            modifier = modifier
                        ) {
                            coordinates.forEachIndexed { index, coordinate ->

                                val brush =
                                    if (config?.barFillBrushes.isNullOrEmpty() || index >= config.barFillBrushes.size) {
                                        config?.barFillBrush ?: SolidColor(Color.Blue)
                                    } else {
                                        config.barFillBrushes[index]
                                    }

                                drawRoundRect(
                                    brush = brush,
                                    topLeft = Offset(
                                        coordinate.x - barDimensions.barWidthInPixels / 2,
                                        coordinate.y
                                    ),
                                    size = Size(
                                        barDimensions.barWidthInPixels,
                                        size.height - coordinate.y
                                    ),
                                    cornerRadius = CornerRadius(
                                        config?.barCornerRadius ?: 10f,
                                        config?.barCornerRadius ?: 10f
                                    )
                                )

                                // place text label if formatter is provided
                                if (config?.labelFormatter != null) {

                                    val label = config.labelFormatter.invoke(
                                        index,
                                        data[index]
                                    )

                                    drawText(
                                        style = config.labelTextStyle,
                                        textMeasurer = textMeasurer,
                                        text = label,
                                        topLeft = Offset(
                                            x = coordinate.x - textMeasurer.measure(label).size.width / 2,
                                            y = coordinate.y - textMeasurer.measure(label).size.height - 5.dp.toPx(
                                                density
                                            )
                                        )
                                    )
                                }

                            }

                            if (!config?.chartConfig?.horizontalGuideLines.isNullOrEmpty()) {

                                config.chartConfig.horizontalGuideLines.forEach { guideLineConfig ->

                                    val y = ChartHelper.calculateValueYOffSet(
                                        config = config.chartConfig,
                                        data = data,
                                        chartDimensions = chartDimensions,
                                        value = guideLineConfig.yValue
                                    )

                                    drawLine(
                                        color = guideLineConfig.color,
                                        start = Offset(
                                            x = chartDimensions.leftAreaWidthPixels,
                                            y = y
                                        ),
                                        end = Offset(
                                            x = chartDimensions.leftAreaWidthPixels + chartDimensions.plotAreaWidthPixels,
                                            y = y
                                        ),
                                        strokeWidth = guideLineConfig.stroke.width,
                                        pathEffect = guideLineConfig.stroke.pathEffect
                                    )

                                    // position label
                                    val labelSize = textMeasurer.measure(
                                        text = guideLineConfig.label,
                                        style = guideLineConfig.labelStyle
                                    ).size
                                    val padding = with(density) { guideLineConfig.padding.toPx() }

                                    val xLabel = when (guideLineConfig.labelPosition) {
                                        HorizontalGuideLineConfig.LabelPosition.CENTER_ABOVE, HorizontalGuideLineConfig.LabelPosition.CENTER_UNDER -> {
                                            chartDimensions.leftAreaWidthPixels + (chartDimensions.plotAreaWidthPixels / 2) - (labelSize.width / 2)
                                        }

                                        HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE, HorizontalGuideLineConfig.LabelPosition.LEFT_UNDER -> {
                                            chartDimensions.leftAreaWidthPixels + padding
                                        }

                                        HorizontalGuideLineConfig.LabelPosition.RIGHT_ABOVE, HorizontalGuideLineConfig.LabelPosition.RIGHT_UNDER -> {
                                            chartDimensions.leftAreaWidthPixels + chartDimensions.plotAreaWidthPixels - labelSize.width
                                        }
                                    }

                                    val yLabel = when (guideLineConfig.labelPosition) {
                                        HorizontalGuideLineConfig.LabelPosition.CENTER_ABOVE, HorizontalGuideLineConfig.LabelPosition.LEFT_ABOVE, HorizontalGuideLineConfig.LabelPosition.RIGHT_ABOVE -> {
                                            y - labelSize.height - padding
                                        }

                                        HorizontalGuideLineConfig.LabelPosition.CENTER_UNDER, HorizontalGuideLineConfig.LabelPosition.LEFT_UNDER, HorizontalGuideLineConfig.LabelPosition.RIGHT_UNDER -> {
                                            y + padding
                                        }
                                    }

                                    drawText(
                                        textMeasurer = textMeasurer,
                                        text = guideLineConfig.label,
                                        style = guideLineConfig.labelStyle,
                                        topLeft = Offset(
                                            x = xLabel,
                                            y = yLabel
                                        )
                                    )
                                }
                            }

                            if (config?.chartConfig?.rangeRectangleConfig != null) {

                                val yTop = ChartHelper.calculateValueYOffSet(
                                    config = config.chartConfig,
                                    data = data,
                                    chartDimensions = chartDimensions,
                                    value = config.chartConfig.rangeRectangleConfig.maxY
                                )

                                val yBottom = ChartHelper.calculateValueYOffSet(
                                    config = config.chartConfig,
                                    data = data,
                                    chartDimensions = chartDimensions,
                                    value = config.chartConfig.rangeRectangleConfig.minY
                                )

                                drawRect(
                                    brush = SolidColor(config.chartConfig.rangeRectangleConfig.color),
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
                                        textMeasurer.measure(config.chartConfig.rangeRectangleConfig.label).size.width / 2

                                val textHeight =
                                    textMeasurer.measure(config.chartConfig.rangeRectangleConfig.label).size.height
                                val textY =
                                    when (config.chartConfig.rangeRectangleConfig.labelPosition) {
                                        RangeRectangleConfig.LabelPosition.CENTER -> (yBottom + (yTop - yBottom) / 2) - (textHeight / 2)
                                        RangeRectangleConfig.LabelPosition.TOP -> yTop - textHeight
                                        RangeRectangleConfig.LabelPosition.BOTTOM -> yBottom + textHeight
                                    }

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = config.chartConfig.rangeRectangleConfig.label,
                                    style = config.chartConfig.rangeRectangleConfig.labelStyle,
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

                            if ( config != null && showPopup) {
                                PopupBox(
                                    data = selectedData,
                                    config = config.chartConfig,
                                    chartDimensions = chartDimensions,
                                    density = density,
                                    coordinate = selectedCoordinate
                                )
                            }

                            if (config?.chartConfig?.crossHairConfig != null && config.chartConfig.crossHairConfig.lineStyle.display) {
                                CrossHairs(
                                    config = config.chartConfig,
                                    coordinate = selectedCoordinate
                                )
                            }
                        }
                    }

                }

            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize().border(width = 1.dp, color = Color.LightGray),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = "No Data", textAlign = TextAlign.Center)
            }
        }
    }
}


