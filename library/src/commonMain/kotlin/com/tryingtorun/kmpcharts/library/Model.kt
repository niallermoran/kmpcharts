package com.tryingtorun.kmpcharts.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val defaultLineColor = Color.Gray // default color used that works for both themes

/**
 * Data class representing a single bar in the chart
 */
data class ChartDataPoint(
    val xValue: Double,
    val yValue: Double,
    val summary: String
)

/**
 * Data class representing a single point in the chart plot area
 */
data class DataPointPlotCoordinates(
    val x: Float,
    val y: Float,
    val dataPoint: ChartDataPoint
)

data class AxisConfig(

    /**
     * The formatter to use to display values on the axis
     */
    val valueFormatter: (Double) -> String = { it.toPrecisionString(2) },

    /**
     * The style for the axis
     */
    val lineStyle: LineStyle = LineStyle(),

    /**
     * The style for the grid lines
     */
    val gridLineStyle: LineStyle = LineStyle(),

    /**
     * Whether or not to show ticks for each point on the axis
     */
    val showTicks: Boolean = true,

    /**
     * Whether or not to show labels for each point on the axis
     */
    val showLabels: Boolean = true,

    /**
     * Whether or not to show labels for each point on the axis
     */
    val showAxisLine: Boolean = true,

    /**
     * The length of the ticks
     */
    val tickLength: Dp = 10.dp,

    /**
     * The style of the labels
     */
    val labelStyle: TextStyle = TextStyle(
        color = defaultLineColor
    ),

    /**
     * The padding around the labels
     */
    val labelPadding: PaddingValues = PaddingValues(6.dp),

    /**
     * Defines the scale for the axis, if left null will be calculated from the data
     */
    val scale: Scale? = null,

    /**
     * The number of labels to show on the axis.
     * For line charts the bottom axis range will be equally divided. For bar charts the labels will match to the closest bar, but you may have to adjust this value based on your data set.
     */
    val numberOfLabelsToShow: Int = 5,

    /**
     * Whether or not to shift the first label up or to the right to ensure it is visible
     * If you set this to false, then use gutter widths on the chart config to allow space and ensure these labels are not cut off
     */
    val shiftFirstLabel: Boolean = false,

    /**
     * Whether or not to shift the last label up or to the right to ensure it is visible
     * If you set this to false, then use gutter widths on the chart config to allow space and ensure these labels are not cut off
     */
    val shiftLastLabel: Boolean = false

)

data class LineStyle(
    val color: Color = defaultLineColor,
    val stroke: Stroke = Stroke(
        width = 2f,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
    )
)

/**
 * Data class representing the dimensions of the bar chart
 */
data class BarDimensions(

    val barWidth: Dp,
    val spacingWidth: Dp,
    val barWidthInPixels: Float,
    val spacingWidthInPixels: Float,

    )

/**
 * Gets the absolute pixels for a given dp value and screen density
 */
fun Dp.toPx(density: Density): Float {
    return with(density) { this@toPx.toPx() }
}

/**
 * Gets the dp value for a given pixel value and screen density
 */
fun Float.toDp(density: Density): Dp {
    return with(density) { this@toDp.toDp() }
}

data class ChartDimensions(
    val leftAreaWidth: Dp,
    val plotAreaHeight: Dp,
    val plotAreaWidth: Dp,
    val bottomAreaHeight: Dp,
    val leftAxisLabelHeight: Dp,
    val leftAreaWidthPixels: Float,
    val plotAreaHeightPixels: Float,
    val plotAreaWidthPixels: Float,
    val bottomAreaHeightPixels: Float,
    val leftAxisLabelHeightPixels: Float
) {
    val fullWidth
        get() = leftAreaWidth + plotAreaWidth
}

val PaddingValues.leftPadding: Dp
    get() = calculateLeftPadding(LayoutDirection.Ltr)
val PaddingValues.endPadding: Dp
    get() = calculateEndPadding(LayoutDirection.Ltr)

val PaddingValues.topPadding: Dp
    get() = calculateTopPadding()
val PaddingValues.bottomPadding: Dp
    get() = calculateBottomPadding()