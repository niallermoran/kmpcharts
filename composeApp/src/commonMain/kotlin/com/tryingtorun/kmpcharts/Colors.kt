package com.tryingtorun.kmpcharts

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.luminance

object PulseIQColors {

    object NeutralColors {
        val veryLightGray = Color(0xFFF5F5F5)
        val lightGray = Color(0xFFBDBDBD)
        val gray = Color(0xFF9E9E9E)
        val darkGray = Color(0xFF616161)
        val veryDarkGray = Color(0xFF212121)
        val green = Color(0xFF22c55e)
        val red = Color(0xFFC70707)
        val yellow = Color(0xFFeab308)
        val orange = Color(0xFFf97316)
        val blue = Color(0xFF3b82f6)
    }

    object BrandColors {
        val blue = Color(0xFF629bf7)
        val purple = Color(0xFFa904f6)
    }


    object UIColors{
        val backgroundDark = Color(0xFF121212)
        val cardSurfaceDark = Color(0xFF1c1c1c)
        val appBarSurfaceDark = Color(0xFF121212)

        val backgroundLight = Color(0xFFfafafa)
        val cardSurfaceLight = Color(0xFFf5f5f5)
        val appBarSurfaceLight = Color(0xFFebebeb)

        val cardOutlineDark = Color(0xFF262626)
        val cardOutlineLight = Color(0xFFe0e0e0)

        /**
         * Gets the HR zone colors
         */
        val zoneColors =  listOf(
            SolidColor(Color.LightGray),
            SolidColor(
                Color(
                    red = .100f,
                    green = 0.75f,
                    blue = 1f,
                    alpha = 1f,
                    colorSpace = ColorSpaces.Srgb
                )
            ),
            SolidColor(Color(0f, 0.8f, 0f)),
            SolidColor(Color(red = 1f, green = .5f, blue = 0f)),
            SolidColor(Color(red = 0.8f, green = 0f, blue = 0f)),
        )
    }




    val LightColorScheme = lightColorScheme(
        primary = BrandColors.blue,
        secondary = BrandColors.purple,
        background = UIColors.backgroundLight,
        surface = UIColors.cardSurfaceLight,
        onPrimary = BrandColors.blue.getContrastingColor(),
        onSecondary = BrandColors.purple.getContrastingColor(),
        onBackground = NeutralColors.veryDarkGray,
        onSurface = NeutralColors.veryDarkGray,
        primaryContainer = UIColors.appBarSurfaceLight,
        onPrimaryContainer = NeutralColors.veryDarkGray,
    )

    val DarkColorScheme = darkColorScheme(
        primary = BrandColors.purple,
        secondary = BrandColors.blue,
        background = UIColors.backgroundDark,
        surface = UIColors.cardSurfaceDark,
        onPrimary = BrandColors.blue.getContrastingColor(),
        onSecondary = BrandColors.purple.getContrastingColor(),
        onBackground = NeutralColors.veryLightGray,
        onSurface = NeutralColors.veryLightGray,
        onSurfaceVariant = NeutralColors.lightGray,
        primaryContainer = UIColors.appBarSurfaceDark,
        onPrimaryContainer = NeutralColors.veryLightGray,
    )

}

fun contrastRatio(c1: Color, c2: Color): Double {
    val l1 = c1.luminance() + 0.05
    val l2 = c2.luminance() + 0.05
    return if (l1 > l2) l1 / l2 else l2 / l1
}

fun Color.getContrastingColor(): Color {

    val blackContrast = contrastRatio(this, Color.Black)
    val whiteContrast = contrastRatio(this, Color.White)
    val darkGrayContrast = contrastRatio(this, Color.DarkGray)
    val lightGrayContrast = contrastRatio(this, Color.DarkGray)

    // Return the color with the highest contrast ratio
    return when {
        blackContrast >= whiteContrast && blackContrast >= darkGrayContrast && blackContrast >= lightGrayContrast -> Color.Black
        whiteContrast >= blackContrast && whiteContrast >= darkGrayContrast && whiteContrast >= lightGrayContrast -> Color.White
        darkGrayContrast >= blackContrast && darkGrayContrast >= whiteContrast && darkGrayContrast >= lightGrayContrast -> Color.DarkGray
        else -> Color.LightGray
    }

}


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

fun Color.darken(darkenBy: Float = 0.3f): Color {
    return copy(
        red = red * darkenBy,
        green = green * darkenBy,
        blue = blue * darkenBy,
        alpha = alpha
    )
}

fun Color.lighten(lightenBy: Float = 1.3f): Color {
    return copy(
        red = (red * lightenBy).coerceAtMost(1f),
        green = (green * lightenBy).coerceAtMost(1f),
        blue = (blue * lightenBy).coerceAtMost(1f),
        alpha = alpha
    )
}
