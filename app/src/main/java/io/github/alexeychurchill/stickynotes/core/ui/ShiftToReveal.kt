@file:OptIn(ExperimentalMaterialApi::class)

package io.github.alexeychurchill.stickynotes.core.ui

import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection.Rtl
import io.github.alexeychurchill.stickynotes.core.ui.ShiftValue.None
import kotlin.math.roundToInt

enum class ShiftValue {
    None,
    EndToStart,
    StartToEnd,
}

@Composable
fun ShiftToReveal(
    modifier: Modifier = Modifier,
    thresholds: (ShiftValue, ShiftValue) -> ThresholdConfig = { _, _ ->
        FractionalThreshold(0.2f)
    },
    shifts: Map<Dp, ShiftValue> = emptyMap(),
    content: @Composable () -> Unit,
    background: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val swipeableState = rememberSwipeableState(None)
        val anchors = mutableMapOf(
            0.0f to None,
        )
        for (shift in shifts) {
            val shiftPx = with(LocalDensity.current) {
                shift.key.toPx()
            }
            val valuePx = when (shift.value) {
                None -> continue
                ShiftValue.EndToStart -> -shiftPx
                ShiftValue.StartToEnd -> shiftPx
            }
            anchors += valuePx to shift.value
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .swipeable(
                    reverseDirection = LocalLayoutDirection.current == Rtl,
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = thresholds,
                    orientation = Horizontal,
                ),
        ) {
            Box(modifier = Modifier.matchParentSize()) {
                background()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .offset { swipeableState.asOffset() },
            ) {
                content()
            }
        }
    }
}

private fun SwipeableState<ShiftValue>.asOffset(): IntOffset {
    return IntOffset(
        x = offset.value.roundToInt(),
        y = 0
    )
}
