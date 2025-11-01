package com.tryingtorun.kmpcharts.library

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChartConfig(

    /**
     * The default color to use for all line and text configs. Can be overridden by defined axis styles
     */
    val defaultAxisTextAndLineColor: Color,

    /**
     * The configuration for the bottom axis. If null not bottom axis, ticks or labels will be shown
     */
    val bottomAxisConfig: AxisConfig? = null,

    /**
     * The left gutter provides a gap between the start of the chart and the left axis
     */
    val leftGutterWidth: Dp = 0.dp,

    /**
     * The right gutter provides a gap between the end of the chart and the right axis
     */
    val rightGutterWidth: Dp = 0.dp,

    /**
     * The configuration for the cross hair
     */
    val crossHairConfig: CrossHairConfig? = null,


    /**
     * The method to use to draw ticks and labels on the bottom axis
     */
    val bottomAxisMethod: BottomAxisTicksAndLabelsDrawMethod = BottomAxisTicksAndLabelsDrawMethod.MATCH_POINT,

    /**
     * The horizontal guide lines to draw on the chart
     */
    val horizontalGuideLines: List<HorizontalGuideLineConfig> = emptyList(),

    /**
     * The configuration for the left axis
     */
    val leftAxisConfig: AxisConfig? = null,


    /**
     * The configuration for the popup rectangle
     */
    val popupConfig: PopupConfig?= null,



    /**
     * The configuration for the range rectangle
     */
    val rangeRectangleConfig: RangeRectangleConfig? = null
)

/**
 * Method to use to display labels and ticks on the bottom axis. If MATCH_POINT the ticks and labels will be drawn for each data point.
 * If DIVIDE_EQUALLY is used, the ticks and labels will be drawn dividing the axis equally based on the number of labels to show
 */
enum class BottomAxisTicksAndLabelsDrawMethod {
    MATCH_POINT, DIVIDE_EQUALLY
}

/**
 * Configure a rectangle to be drawn on the plot area to demonstrate a range
 */
data class RangeRectangleConfig(
    val minY: Float = 0f,
    val maxY: Float = 0f,
    val color: Color = Color.Green.copy(alpha = 0.6f),
    val label: String = "",
    val labelStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    val labelPosition: LabelPosition = LabelPosition.CENTER
) {
    enum class LabelPosition {
        CENTER, TOP, BOTTOM
    }
}

data class HorizontalGuideLineConfig(
    val yValue: Float = 0f,
    val color: Color = Color.Green,
    val label: String = "Horizontal Guide Line",

    /**
     * Padding between label and line
     */
    val padding: Dp = 2.dp,

    val stroke: Stroke = Stroke(
        width = 10f,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    ),
    val labelStyle: TextStyle,
    val labelPosition: LabelPosition = LabelPosition.CENTER_ABOVE
) {
    enum class LabelPosition {
        CENTER_ABOVE, CENTER_UNDER, LEFT_ABOVE, LEFT_UNDER, RIGHT_ABOVE, RIGHT_UNDER
    }
}


data class PopupConfig(

    val backgroundBrush: Brush = SolidColor(Color.Yellow.copy(0.8f)),
    val valueTextColor: Color = Color.Black,
    val summaryTextColor: Color = Color.Black,

    /**
     * The formatter to use to display values on the popup
     */
    val valueFormatter: (Double) -> String = { it.toPrecisionString(1) },

    /**
     * The color of the left axis
     */
    val valueFontSize: TextUnit = 22.sp,

    /**
     * The font size of the summary text
     */
    val summaryTextFontSize: TextUnit = 10.sp,

    /**
     * The maximum height the popup will take up, wrapping the summary text
     */
    val width: Dp = 100.dp,

    /**
     * The corner radius of the popup
     */
    val cornerRadius: Dp = 10.dp,
)


/**
 * Configuration for the bar chart appearance
 */
data class LineChartConfig(

    val chartConfig: ChartConfig,

    /**
     * The brush to use to fill under the line
     */
    val fillBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF45B7D1), Color(0xFF96CEB4))
    ),

    /**
     * Fills the line with the fillBrush
     */
    val fillLine: Boolean = true,

    /**
     * The style to use for the chart line
     */
    val lineStyle: LineStyle = LineStyle(
        display = true,
        color = Color(0xFF45B7D1),
        stroke = Stroke(
            width = 2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )


)

/**
 * Configuration for the bar chart appearance
 */
data class BarChartConfig(

    val chartConfig: ChartConfig,
    /**
     * The width of each bar compared to the space available
     */
    val barWidthFactor: Float = 0.8f,

    /**
     * The corner radius of each bar
     */
    val barCornerRadius: Float = 10f,

    /**
     * The default color of each bar
     */
    val barFillBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF45B7D1), Color(0xFF96CEB4))
    ),

    /**
     * The list of brushes to use to fill each bar individually. If this is provided, it will override [barFillBrush] for each bar based on its index
     */
    val barFillBrushes: List<Brush>? = null,


    /**
     * The formatter to use to display values on top of the bars, leave nul for no labels
     */
    val labelFormatter: ((index: Int, data: ChartDataPoint) -> String)? = null,

    /**
     * The style of the labels
     */
    val labelTextStyle: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Center
    )

)


data class CrossHairConfig(
    val lineStyle: LineStyle,
    val showHorizontalLine: Boolean = true,
    val showVerticalLine: Boolean = true,
    val showCircle: Boolean = true,
    val circleColor: Color = lineStyle.color,
    val circleRadius: Float = 10f

)