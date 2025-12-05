package com.alex.dynamicchat.features.chat.presentation.ui.layouts.beehive

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import com.alex.dynamicchat.features.chat.presentation.ui.models.MessageUi

@Composable
fun BeehiveLayout(
    messages: List<MessageUi>,
    modifier: Modifier = Modifier,
    content: @Composable (MessageUi) -> Unit
) {
    Layout(
        modifier = modifier,
        content = { messages.forEach { content(it) } }
    ) { measurables, constraints ->

        val tileWidth = constraints.maxWidth / 2
        val tileHeight = (tileWidth * 0.85f).toInt()

        val verticalStep = (tileHeight * 0.75f).toInt()

        val positions = mutableListOf<Placeable>()
        val coords = mutableListOf<Pair<Int, Int>>()

        measurables.forEachIndexed { index, measurable ->
            val message = messages[index]
            val placeable = measurable.measure(
                Constraints.fixed(tileWidth, tileHeight)
            )
            positions += placeable

            val isMe = message.isMe

            val offsetX = if (isMe) {
                constraints.maxWidth - tileWidth
            } else {
                0
            }

            val staggerShift = if (index % 2 == 0) 0 else tileWidth / 4

            val finalX = offsetX + if (isMe) -staggerShift else staggerShift
            val finalY = index * verticalStep

            coords += finalX to finalY
        }

        val finalHeight = coords.last().second + tileHeight

        layout(constraints.maxWidth, finalHeight) {
            positions.forEachIndexed { i, placeable ->
                placeable.place(coords[i].first, coords[i].second)
            }
        }
    }
}
