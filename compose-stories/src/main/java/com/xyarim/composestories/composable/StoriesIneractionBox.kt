package com.xyarim.composestories.composable

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.flow.mapNotNull

@Composable
internal fun StoriesInteractionBox(
    modifier: Modifier = Modifier,
    onTap: (Tap) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    BoxWithConstraints(
        modifier = modifier
            .then(Modifier.fillMaxSize())
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            interactionSource.emit(InteractionEvent.OnTapStarted)
                            awaitRelease()
                            interactionSource.emit(
                                InteractionEvent.OnTapStopped(
                                    constraints.maxWidth,
                                    offset.x
                                )
                            )
                        },
                    )
                },
        )
    }

    LaunchedEffect(interactionSource) {
        var lastTapStarterTime = 0L
        interactionSource.interactions.collect { interaction ->
            if (interaction is InteractionEvent) {
                when (interaction) {
                    InteractionEvent.OnTapStarted -> {
                        lastTapStarterTime = System.currentTimeMillis()
                    }

                    is InteractionEvent.OnTapStopped -> {
                        val currentTime = System.currentTimeMillis()
                        val timeDiff = currentTime - lastTapStarterTime
                        if (timeDiff < 200) {
                            val tap = interaction.detectTap()
                            onTap(tap)
                        }
                    }
                }
            }
        }
    }
}

internal sealed class InteractionEvent : Interaction {
    data object OnTapStarted : InteractionEvent()
    data class OnTapStopped(val width: Int, val x: Float) : InteractionEvent()
}

@Composable
internal fun InteractionSource.collectIsPausedAsState(): State<Boolean> {
    val isPaused = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        interactions.mapNotNull { it as? InteractionEvent }.collect {
            when (it) {
                InteractionEvent.OnTapStarted -> isPaused.value =
                    true

                is InteractionEvent.OnTapStopped -> isPaused.value = false
            }
        }
    }
    return isPaused
}

private fun InteractionEvent.OnTapStopped.detectTap(): Tap {
    return if (x < width / 2) {
        Tap.LEFT
    } else {
        Tap.RIGHT
    }
}

