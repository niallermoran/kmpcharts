package com.tryingtorun.kmpcharts.library
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
     * The configuration for the popup rectangle
     */
    val popupConfig: PopupConfig = PopupConfig(),

    /**
     * The configuration for the range rectangle
     */
    val rangeRectangleConfig: RangeRectangleConfig = RangeRectangleConfig(),

    /**
     * The configuration for the left axis
     */
    val leftAxisConfig: AxisConfig = AxisConfig(),

    /**
     * The configuration for the bottom axis
     */
    val bottomAxisConfig: AxisConfig = AxisConfig(),

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
    val crossHairConfig: CrossHairConfig = CrossHairConfig()
)

/**
 * Configure a rectangle to be drawn on the plot area to demonstrate a range
 */
data class RangeRectangleConfig(

    val display: Boolean = false,
    val minY: Float = 0f,
    val maxY: Float = 0f,
    val color: Color = Color.Green.copy(alpha = 0.6f),
    val label: String = "",
    val labelStyle: TextStyle = TextStyle(
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    val labelPosition: LabelPosition = LabelPosition.CENTER
)
{
    enum class LabelPosition {
        CENTER, TOP, BOTTOM
    }

}


data class PopupConfig(

    val valueTextColor: Color = Color.Black,

    /**
     * The formatter to use to display values on the popup
     */
    val valueFormatter: (Double) -> String = { it.toPrecisionString(1) },

    /**
     * The color of the left axis
     */
    val valueFontSize: TextUnit = 22.sp,


    val summaryTextColor: Color = Color.Black,

    /**
     * The font size of the summary text
     */
    val summaryTextFontSize: TextUnit = 10.sp,

    /**
     * The maximum height the popup will take up, wrapping the summary text
     */
    val width: Dp = 200.dp,

    /**
     * The corner radius of the popup
     */
    val cornerRadius: Dp = 10.dp,

    val color: Brush = SolidColor(Color.Yellow.copy(0.8f)),
)


/**
 * Configuration for the bar chart appearance
 */
data class LineChartConfig(

    /**
     * The chart configuration
     */
    val chartConfig: ChartConfig = ChartConfig(),

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

    /**
     * The chart configuration
     */
    val chartConfig: ChartConfig = ChartConfig(),

    /**
     * The width of each bar compared to the space available
     */
    val barWidthFactor: Float = 0.8f,

    /**
     * The corner radius of each bar
     */
    val barCornerRadius: Float = 10f,

    /**
     * The color of each bar
     */
    val barFillBrush: Brush = Brush.linearGradient(
        colors = listOf(Color(0xFF45B7D1), Color(0xFF96CEB4))
    )


)


data class CrossHairConfig(
    val lineStyle: LineStyle = LineStyle(
        color = Color.LightGray,
        stroke = Stroke(
            width = 2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    ),
    val showHorizontalLine: Boolean = true,
    val showVerticalLine: Boolean = true,
    val showCircle: Boolean = true,
    val circleColor: Color = lineStyle.color,
    val circleRadius: Float = 10f

)