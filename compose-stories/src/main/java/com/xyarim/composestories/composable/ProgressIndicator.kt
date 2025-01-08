package com.xyarim.composestories.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ProgressIndicatorTheme(
    val modifier: Modifier = Modifier,
    val padding: Dp = 16.dp,
    val trackColor: Color = Color.Gray,
    val progressColor: Color = Color.White,
    val height: Dp = 2.5.dp,
    val spaceBetween: Dp = 2.5.dp
)

@Composable
internal fun ProgressIndicator(
    modifier: Modifier = Modifier,
    theme: ProgressIndicatorTheme = ProgressIndicatorTheme(),
    stepCount: Int,
    currentStepState: Int,
    progress: Animatable<Float, AnimationVector1D>,
) {
    Row(
        modifier = theme.modifier.then(modifier),
        horizontalArrangement = Arrangement.spacedBy(theme.spaceBetween)
    ) {
        for (i in 0 until stepCount) {
            val stepProgress = when {
                i == currentStepState -> progress.value
                i > currentStepState -> 0f
                else -> 1f
            }
            LinearProgressIndicator(
                strokeCap = StrokeCap.Round,
                color = theme.progressColor,
                trackColor = theme.trackColor,
                progress = { stepProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(theme.height)
            )
        }
    }
}
