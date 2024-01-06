package com.angelhr28.yapechallenge2024.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

class DottedShape(private val step: Dp) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val stepPx = with(density) { step.toPx() }
        val dotWidth = stepPx / 2
        val dotsCount = (size.width / stepPx).roundToInt()
        val path = Path()

        path.moveTo(0f, 0f)
        for (i in 0 until dotsCount) {
            val x = i * stepPx
            path.moveTo(x, 0f)
            path.lineTo(x + dotWidth, 0f)
            path.lineTo(x + dotWidth, size.height)
            path.lineTo(x, size.height)
            path.close()
        }

        return Outline.Generic(path)
    }
}
