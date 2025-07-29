package com.tryingtorun.kmpcharts.library

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

object ChartHelper {


    private fun calculateBottomAxisLabelSize(
        data: List<ChartDataPoint>,
        config: ChartConfig,
        textMeasurer: TextMeasurer
    ): IntSize {

        val anyXLabel = config.bottomAxisConfig.valueFormatter(data.first().xValue)

        val layout = textMeasurer.measure(
            anyXLabel,
            style = config.bottomAxisConfig.labelStyle
        )

        return layout.size
    }


    /**
     * Finds the closest data point to the given touch position
     */
    internal fun findClosestDataPointIndex(
        touchPosition: Offset,
        coordinates: List<DataPointPlotCoordinates>,
        data: List<ChartDataPoint>,
    ): Int? {

        var closestIndex: Int? = null
        var minDistance = Float.MAX_VALUE
        data.forEachIndexed { index, _ ->
            val barCenterX = coordinates[index].x
            val distance = abs(touchPosition.x - barCenterX)

            if (distance < minDistance) {
                minDistance = distance
                closestIndex = index
            }
        }


        return closestIndex
    }


    private fun calculateLeftAxisLabelSize(
        density: Density,
        data: List<ChartDataPoint>,
        config: ChartConfig,
        textMeasurer: TextMeasurer
    ): IntSize {

        val largestValue = data.maxOfOrNull { it.yValue } ?: 0.0
        val largestValueFormatted = config.leftAxisConfig.valueFormatter(largestValue)

        val size = measureLeftAxisLabelSize(
            label = largestValueFormatted,
            density = density,
            textMeasurer = textMeasurer,
            config = config
        )

        return size
    }

    /**
     * Gets the offset along the y-axis for a specific data point value
     */
    internal fun calculateValueYOffSet(
        config: ChartConfig,
        data: List<ChartDataPoint>,
        chartDimensions: ChartDimensions,
        value: Float
    ): Float {

        val minY = config.leftAxisConfig.minValue ?: data.minOf { it.yValue }
        val maxY = config.leftAxisConfig.maxValue ?: data.maxOf { it.yValue }
        val yRange = maxY - minY

        val plotAreaHeight = chartDimensions.plotAreaHeightPixels
        val y = plotAreaHeight * (1 - ((value - minY) / yRange))
        return y.toFloat()
    }

    /**
     * Returns a list of offsets for each data point relative to the chart plot area
     */
    internal fun getDataPointCoordinates(
        density: Density,
        config: ChartConfig,
        data: List<ChartDataPoint>,
        chartDimensions: ChartDimensions,
        barWidth: Dp = 0.dp // for bar charts we need to reduce the area by a bar width
    ): List<DataPointPlotCoordinates> {

        val minX = data.minOf { it.xValue }
        val minY = config.leftAxisConfig.minValue ?: data.minOf { it.yValue }
        val maxX = data.maxOf { it.xValue }
        val maxY = config.leftAxisConfig.maxValue ?: data.maxOf { it.yValue }

        val xRange = maxX - minX
        val yRange = maxY - minY

        val plotAreaWidth = chartDimensions.plotAreaWidthPixels - config.leftGutterWidth.toPx(density) - config.rightGutterWidth.toPx(density) - barWidth.toPx(density)
        val plotAreaHeight = chartDimensions.plotAreaHeightPixels
        val plotAreaXOffset = config.leftGutterWidth.toPx(density) + chartDimensions.leftAreaWidthPixels + barWidth.toPx(density)/2

        val list = data.map { point ->
            val x = if( xRange == 0.0) plotAreaXOffset else plotAreaXOffset + ((point.xValue - minX) / xRange) * plotAreaWidth
            val y = plotAreaHeight * (1 - ((point.yValue - minY) / yRange))
            DataPointPlotCoordinates(x = x.toFloat(), y = y.toFloat(), dataPoint = point)
        }

        return list
    }


    /**
     * Gets the dimensions for bars in a bar chart, e.g. bar width
     */
    internal fun getBarDimensions(
        density: Density,
        chartDimensions: ChartDimensions,
        config: BarChartConfig,
        data: List<ChartDataPoint>
    ): BarDimensions {

        val availableWidth = chartDimensions.plotAreaWidthPixels - config.chartConfig.leftGutterWidth.toPx(density) - config.chartConfig.rightGutterWidth.toPx(density)

        /**
         * The usable width contains n bars and (n-1) spaces between bars which are a equal to the width of the bar by a factor
         * usableWidth = n * barWidth + (n-1) * spacing
         */
        val barWidth = if( data.size == 1) availableWidth else  availableWidth/ (data.size + config.barWidthFactor * (data.size - 1))
        val spacingBetweenBars = if( data.size == 1) 0f else barWidth * config.barWidthFactor

        return BarDimensions(
            barWidth = with(density) { barWidth.toDp() },
            spacingWidth = with(density) { spacingBetweenBars.toDp() },
            barWidthInPixels = barWidth,
            spacingWidthInPixels = spacingBetweenBars,
        )
    }


    internal fun calculateChartDimensions(
        density: Density,
        data: List<ChartDataPoint>,
        config: ChartConfig,
        textMeasurer: TextMeasurer,
        width: Dp,
        height: Dp
    ): ChartDimensions {


        /**
         * Calculations for left and bottom axis labels
         */
        val leftLabelSize = calculateLeftAxisLabelSize(
            density = density,
            data = data,
            config = config,
            textMeasurer = textMeasurer
        )

        val leftLabelMaxWidth =
            if (config.leftAxisConfig.lineStyle.display) with(density) { leftLabelSize.width.toDp() } else 0.dp
        val bottomLabelSize = calculateBottomAxisLabelSize(data, config, textMeasurer)
        val bottomLabelHeight = with(density) { bottomLabelSize.height.toDp() }
        val bottomAreaHeight =
            bottomLabelHeight + config.leftAxisConfig.labelPadding.calculateTopPadding() + config.leftAxisConfig.labelPadding.calculateBottomPadding() + config.bottomAxisConfig.tickLength
        val leftAreaWidth = leftLabelMaxWidth.plus(
            (if (config.leftAxisConfig.showLabels) config.leftAxisConfig.labelPadding.leftPadding + config.leftAxisConfig.labelPadding.endPadding else 0.dp)
        ).plus(
            (if (config.leftAxisConfig.showTicks) config.leftAxisConfig.tickLength else 0.dp)
        )


        val plotAreaHeight = height - bottomAreaHeight
        val plotAreaWidth = width - leftAreaWidth

        println("plotAreaWidth: $plotAreaWidth")
        println("width: $width")
        println("leftAreaWidth: $leftAreaWidth")

        return ChartDimensions(

            leftAreaWidth = leftAreaWidth,
            plotAreaHeight = plotAreaHeight,
            plotAreaWidth = plotAreaWidth,
            bottomAreaHeight = bottomAreaHeight,
            leftAxisLabelHeight = with(density) { leftLabelSize.height.toDp() },

            leftAreaWidthPixels = with(density) { leftAreaWidth.toPx() },
            plotAreaHeightPixels = with(density) { plotAreaHeight.toPx() },
            plotAreaWidthPixels = with(density) { plotAreaWidth.toPx() },
            bottomAreaHeightPixels = with(density) { bottomAreaHeight.toPx() },
            leftAxisLabelHeightPixels = with(density) { leftLabelSize.height.toDp().toPx() }
        )
    }


    fun measureLeftAxisLabelSize(
        label: String,
        density: Density,
        textMeasurer: TextMeasurer,
        config: ChartConfig
    ): IntSize {
        return textMeasurer.measure(
            text = label,
            style = config.leftAxisConfig.labelStyle,
            density = density
        ).size
    }

    internal fun measureBottomAxisLabelSize(
        label: String,
        density: Density,
        textMeasurer: TextMeasurer,
        config: ChartConfig
    ): IntSize {
        return textMeasurer.measure(
            text = label,
            style = config.bottomAxisConfig.labelStyle,
            density = density
        ).size
    }
}


@Composable
internal fun CrossHairs(
    config: ChartConfig,
    coordinate: DataPointPlotCoordinates
) {
    Canvas(
        modifier = Modifier.fillMaxSize().background(Color.Transparent)
    ) {
        // vertical cross hair
        drawLine(
            start = Offset(coordinate.x, 0f),
            end = Offset(coordinate.x, size.height),
            pathEffect = config.crossHairConfig.lineStyle.stroke.pathEffect,
            cap = config.crossHairConfig.lineStyle.stroke.cap,
            color = config.crossHairConfig.lineStyle.color,
            strokeWidth = config.crossHairConfig.lineStyle.stroke.width,
        )

        // horizontal cross hair
        drawLine(
            start = Offset(0f, coordinate.y),
            end = Offset(size.width, coordinate.y),
            pathEffect = config.crossHairConfig.lineStyle.stroke.pathEffect,
            cap = config.crossHairConfig.lineStyle.stroke.cap,
            color = config.crossHairConfig.lineStyle.color,
            strokeWidth = config.crossHairConfig.lineStyle.stroke.width,
        )

        if (config.crossHairConfig.showCircle) {
            drawCircle(
                color = config.crossHairConfig.circleColor,
                radius = config.crossHairConfig.circleRadius,
                center = Offset(coordinate.x, coordinate.y)
            )
        }
    }
}

/**
 * Popup box component that displays information about the selected data point
 */
@Composable
internal fun PopupBox(
    density: Density,
    data: ChartDataPoint,
    config: ChartConfig,
    chartDimensions: ChartDimensions,
    coordinate: DataPointPlotCoordinates,
) {
    // get the x position of the box as longs as x + width of box doesn't overflow the chart
    var x = coordinate.x
    if (x + config.popupConfig.width.toPx(density) > chartDimensions.fullWidth.toPx(density))
        x = chartDimensions.fullWidth.toPx(density) - config.popupConfig.width.toPx(density)

    Row(modifier = Modifier.fillMaxSize()) {

        Spacer(
            modifier = Modifier.width(x.toDp(density))
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(config.popupConfig.cornerRadius))
                .background(config.popupConfig.color)
                .width(config.popupConfig.width)

        )
        {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = config.popupConfig.valueFormatter(data.yValue),
                    style = TextStyle(
                        color = config.popupConfig.valueTextColor,
                        fontSize = config.popupConfig.valueFontSize
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
                Text(
                    text = data.summary,
                    style = TextStyle(
                        color = config.popupConfig.summaryTextColor,
                        fontSize = config.popupConfig.summaryTextFontSize
                    ),
                    overflow = TextOverflow.Visible
                )
            }
        }

    }
}


/**
 * Draws Y-axis value labels
 */
internal fun DrawScope.drawLeftAxisLabelsAndTicks(
    density: Density,
    textMeasurer: TextMeasurer,
    config: ChartConfig,
    data: List<ChartDataPoint>,
    chartDimensions: ChartDimensions
) {
    val width = size.width
    val height = size.height - chartDimensions.bottomAreaHeightPixels

    val minY = config.leftAxisConfig.minValue ?: data.minOf { it.yValue }
    val maxY = config.leftAxisConfig.maxValue ?: data.maxOf { it.yValue }

    val yRange = maxY - minY

    val tickLength = config.leftAxisConfig.tickLength
    val tickLengthInPixels = with(density) { tickLength.toPx() }
    val valueDelta = yRange / (config.leftAxisConfig.numberOfLabelsToShow - 1)
    val heightDelta = height / (config.leftAxisConfig.numberOfLabelsToShow - 1)

    /**
     * Draw axis line
     */
    if (config.leftAxisConfig.lineStyle.display) {
        drawLine(
            color = config.leftAxisConfig.lineStyle.color,
            start = Offset(size.width, 0f),
            end = Offset(size.width, height),
            strokeWidth = config.leftAxisConfig.lineStyle.stroke.width
        )
    }



    for (i in 1..config.leftAxisConfig.numberOfLabelsToShow) {

        val value = minY + (i - 1) * valueDelta
        val isFirstLabel = i == 1
        val isLastLabel = i == config.leftAxisConfig.numberOfLabelsToShow

        val label = config.leftAxisConfig.valueFormatter(value)
        val labelSize = ChartHelper.measureLeftAxisLabelSize(label, density, textMeasurer, config)
        val labelLengthPx = labelSize.width
        val labelHeightPx = labelSize.height

        val y = height - ((i - 1) * heightDelta)
        val xStart = width
        val xEnd = width - tickLengthInPixels


        // draw the tick line
        if (config.leftAxisConfig.showTicks) {
            drawLine(
                color = config.leftAxisConfig.lineStyle.color,
                start = Offset(xStart, y),
                end = Offset(xEnd, y),
                strokeWidth = config.leftAxisConfig.lineStyle.stroke.width
            )

        }

        // calculate the yPosition for the label
        val yPosition = if (isFirstLabel && config.leftAxisConfig.shiftFirstLabel)
            y - labelHeightPx
        else if (isLastLabel && config.leftAxisConfig.shiftLastLabel)
            y
        else y - labelHeightPx / 2

        if (config.leftAxisConfig.showLabels) {
            drawText(
                textMeasurer = textMeasurer,
                text = config.leftAxisConfig.valueFormatter(value),
                topLeft = Offset(
                    width - labelLengthPx - config.leftAxisConfig.labelPadding.leftPadding.toPx(
                        density
                    ) - if (config.leftAxisConfig.showTicks) config.leftAxisConfig.tickLength.toPx(
                        density
                    ) else 0f,
                    yPosition
                ),
                style = config.leftAxisConfig.labelStyle
            )
        }

    }

}


/**
 * Draws Y-axis value labels
 */
internal fun DrawScope.drawBottomAxisLabelsAndTicks(
    density: Density,
    textMeasurer: TextMeasurer,
    config: ChartConfig,
    chartDimensions: ChartDimensions,
    coordinates: List<DataPointPlotCoordinates>,

    /**
     * Used for bar charts only when we need to shift left based on the calculated bar width, leave as zero for line charts, see https://github.com/niallermoran/kmpcharts/issues/3
     */
    barWidth: Dp = 0.dp
) {

    // the starting x-coord that everything else works from as the canvas covers the full width of the chart (to avoid clipping)
    val startX = chartDimensions.leftAreaWidthPixels + barWidth.toPx(density)

    /**
     * Draw the axis line
     */
    if (config.bottomAxisConfig.lineStyle.display) {
        drawLine(
            color = config.bottomAxisConfig.lineStyle.color,
            strokeWidth = config.bottomAxisConfig.lineStyle.stroke.width,
            start = Offset(x = startX, 0f),
            end = Offset(size.width, 0f),
        )
    }

    // depending on how many ticks/labels we want, we may need to skip some
    // calculate the skip value which is the number of coordinates to skip (if any)
    // taking into account that the first and last label will be displayed
    val skip = if (config.bottomAxisConfig.numberOfLabelsToShow >= coordinates.size) 0
    else ((coordinates.size - 2) / (config.bottomAxisConfig.numberOfLabelsToShow - 1))

    var lastIndexDisplayed = 0 // keep track of last index displayed so we can skip

    coordinates.forEachIndexed { index, dataPoint ->

        val isFirst = index == 0
        val isLast = index == coordinates.size - 1

        val show = isFirst || isLast || index > lastIndexDisplayed + skip

        if (show) {

            lastIndexDisplayed = index

            // get the label details
            val label = config.bottomAxisConfig.valueFormatter(dataPoint.dataPoint.xValue)
            val labelSize =
                ChartHelper.measureBottomAxisLabelSize(label, density, textMeasurer, config)
            val labelLengthPx = labelSize.width

            // get the X coordinate for the label
            val xLabelTopLeft = if (config.bottomAxisConfig.shiftFirstLabel && isFirst)
                dataPoint.x
            else if (config.bottomAxisConfig.shiftLastLabel && isLast)
                dataPoint.x - labelLengthPx
            else
                dataPoint.x - (labelLengthPx / 2)

            // get the Y coordinate
            val yTickStart = 0f
            val yTickEnd = config.bottomAxisConfig.tickLength.toPx(density)
            val yLabelTopLeft =
                config.leftAxisConfig.labelPadding.topPadding.toPx(density) + if (config.bottomAxisConfig.showTicks) config.bottomAxisConfig.tickLength.toPx(
                    density
                ) else 0f

            // draw the tick line
            if (config.bottomAxisConfig.showTicks) {
                drawLine(
                    color = config.bottomAxisConfig.lineStyle.color,
                    start = Offset(dataPoint.x, yTickStart),
                    end = Offset(dataPoint.x, yTickEnd),
                    strokeWidth = config.bottomAxisConfig.lineStyle.stroke.width
                )
            }

            if (config.bottomAxisConfig.showLabels) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(
                        xLabelTopLeft,
                        yLabelTopLeft
                    ),
                    style = config.bottomAxisConfig.labelStyle
                )
            }
        }
    }


    /*
        val maxValue = data.maxOfOrNull { it.xValue } ?: 0.0
        val minValue = data.minOfOrNull { it.xValue } ?: 0.0
        val tickLength = config.bottomAxisConfig.tickLength
        val tickLengthInPixels = tickLength.toPx(density)
        val valueRange = maxValue - minValue
        val valueDelta = valueRange / (config.bottomAxisConfig.numberOfLabelsToShow - 1)

        for (i in 1..config.bottomAxisConfig.numberOfLabelsToShow) {

            val isFirst = i == 1
            val isLast = i == config.bottomAxisConfig.numberOfLabelsToShow

            val value = minValue + (i - 1) * valueDelta
            val ratio = (value - minValue).toFloat() / valueRange.toFloat()

            val label = config.bottomAxisConfig.valueFormatter(value)
            val labelSize = ChartHelper.measureBottomAxisLabelSize(label, density, textMeasurer, config)
            val labelLengthPx = labelSize.width

            // get the coordinates for the tick and label
            val xTick = startX + (width * ratio)
            val yTickStart = 0f
            val yTickEnd = tickLengthInPixels
            val yLabelTopLeft =
                config.leftAxisConfig.labelPadding.topPadding.toPx(density) + if (config.bottomAxisConfig.showTicks) tickLengthInPixels else 0f

            val xLabelTopLeft = if (config.bottomAxisConfig.shiftFirstLabel && isFirst)
                xTick
            else if (config.bottomAxisConfig.shiftLastLabel && isLast)
                xTick - labelLengthPx
            else
                xTick - (labelLengthPx / 2)


            // draw the tick line
            if (config.bottomAxisConfig.showTicks) {
                drawLine(
                    color = config.bottomAxisConfig.lineStyle.color,
                    start = Offset(xTick, yTickStart.toFloat()),
                    end = Offset(xTick, yTickEnd.toFloat()),
                    strokeWidth = config.bottomAxisConfig.lineStyle.stroke.width
                )
            }

            if (config.bottomAxisConfig.showLabels) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(
                        xLabelTopLeft,
                        yLabelTopLeft
                    ),
                    style = config.bottomAxisConfig.labelStyle
                )
            }

        }*/

}



internal fun Double.toPrecisionString(precision: Int): String {
    if (precision == 0)
        return this.roundToInt().toString()
    else {
        val factor = 10.0.pow(precision.toDouble())
        return (round(this * factor) / factor).toString()
    }
}


