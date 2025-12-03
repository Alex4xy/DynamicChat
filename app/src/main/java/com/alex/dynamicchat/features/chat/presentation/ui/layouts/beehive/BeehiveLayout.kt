package com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlin.math.max

@Composable
fun BeehiveLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val tileConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        val placeables = measurables.map { it.measure(tileConstraints) }

        if (placeables.isEmpty()) {
            return@Layout layout(
                width = constraints.minWidth,
                height = constraints.minHeight
            ) {}
        }

        val tileWidth = placeables.first().width
        val tileHeight = placeables.first().height

        val horizontalSpacing = (tileWidth * 0.2f).toInt()
        val verticalSpacing = (tileHeight * 0.1f).toInt()

        val effectiveTileWidth = tileWidth + horizontalSpacing
        val effectiveTileHeight = tileHeight + verticalSpacing

        val maxWidth = constraints.maxWidth
        val itemsPerRow = max(1, maxWidth / effectiveTileWidth)

        var x: Int
        var y = 0
        var row = 0

        val positions = mutableListOf<Pair<Int, Int>>()

        placeables.forEachIndexed { index, _ ->
            val column = index % itemsPerRow
            row = index / itemsPerRow
            val offsetX = if (row % 2 == 0) 0 else effectiveTileWidth / 2

            x = column * effectiveTileWidth + offsetX
            y = row * (effectiveTileHeight * 0.8f).toInt()

            positions += x to y
        }

        val layoutHeight = (y + tileHeight + verticalSpacing)
            .coerceAtLeast(constraints.minHeight)

        layout(
            width = maxWidth,
            height = layoutHeight
        ) {
            placeables.forEachIndexed { index, placeable ->
                val (px, py) = positions[index]
                placeable.place(px, py)
            }
        }
    }
}

