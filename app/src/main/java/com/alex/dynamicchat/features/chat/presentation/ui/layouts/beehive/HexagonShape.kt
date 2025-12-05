package com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object HexagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val s = w * 0.25f

        val path = Path().apply {
            moveTo(s, 0f)
            lineTo(w - s, 0f)
            lineTo(w, h / 2f)
            lineTo(w - s, h)
            lineTo(s, h)
            lineTo(0f, h / 2f)
            close()
        }

        return Outline.Generic(path)
    }
}
