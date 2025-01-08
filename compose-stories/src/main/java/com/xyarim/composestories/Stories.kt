import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.xyarim.composestories.StoryItem
import com.xyarim.composestories.composable.ProgressIndicator
import com.xyarim.composestories.composable.ProgressIndicatorTheme
import com.xyarim.composestories.composable.StoriesInteractionBox
import com.xyarim.composestories.composable.Story
import com.xyarim.composestories.composable.Tap
import com.xyarim.composestories.composable.collectIsPausedAsState
import com.xyarim.composestories.composable.onTap
import kotlinx.coroutines.launch

private const val StepDuration = 5000
private const val BeyondViewportPageCount = 3

@Composable
fun Stories(
    modifier: Modifier = Modifier,
    restartOnComplete: Boolean = true,
    stepDuration: Int = StepDuration,
    beyondViewportPageCount: Int = BeyondViewportPageCount,
    progressIndicatorTheme: ProgressIndicatorTheme = remember { ProgressIndicatorTheme() },
    stories: List<StoryItem>,
    onComplete: () -> Unit = {}
) {
    if (stories.isEmpty()) return

    val scope = rememberCoroutineScope()

    val storiesSize = remember(stories) {
        stories.size
    }

    val pagerState = rememberPagerState(pageCount = {
        storiesSize
    })

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPaused by interactionSource.collectIsPausedAsState()

    var currentStepProgress by remember {
        mutableFloatStateOf(0F)
    }

    var currentStep by remember {
        mutableIntStateOf(0)
    }

    val progress = remember(pagerState.currentPage) {
        Animatable(0f)
    }

    var restartAnimationKey by remember { mutableIntStateOf(0) }

    val stepCount = stories.size

    LaunchedEffect(pagerState.currentPage) {
        currentStep = pagerState.currentPage
        pagerState.scrollToPage(pagerState.currentPage)
    }

    LaunchedEffect(storiesSize) {
        currentStep = 0
    }

    Box(modifier = modifier) {
        HorizontalPager(
            beyondViewportPageCount = beyondViewportPageCount,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { page ->
            Story(
                storyItem = stories[page],
            )
        }

        ProgressIndicator(
            theme = progressIndicatorTheme,
            stepCount = stories.size,
            currentStepState = currentStep,
            progress = progress
        )

        StoriesInteractionBox(
            interactionSource = interactionSource,
            onTap = { tap ->
                scope.launch {
                    when (tap) {
                        Tap.RIGHT -> {
                            pagerState.onTap(tap)
                        }

                        Tap.LEFT -> {
                            if (currentStepProgress >= .5f || currentStep == 0) {
                                restartAnimationKey++
                            } else pagerState.onTap(tap)
                        }
                    }

                }
            }
        )
    }


    LaunchedEffect(restartAnimationKey) {
        with(progress) {
            snapTo(0f)
            animateProgress(durationMillis = StepDuration) {
                currentStepProgress = progress.value
            }
        }
    }

    LaunchedEffect(isPaused, currentStep) {
        if (isPaused) {
            progress.stop()
        } else {
            for (i in currentStep until stepCount) {
                progress.animateProgress(
                    durationMillis = ((1f - progress.value) * stepDuration).toInt()
                ) { progressValue ->
                    currentStepProgress = progressValue
                }

                if (currentStep + 1 <= stepCount - 1) {
                    progress.snapTo(0f)
                    currentStep += 1
                    scope.launch {
                        pagerState.scrollToPage(currentStep)
                    }
                } else {
                    onComplete()
                    if (restartOnComplete) {
                        pagerState.scrollToPage(0)
                    }

                }
            }
        }
    }
}

private suspend fun Animatable<Float, AnimationVector1D>.animateProgress(
    durationMillis: Int,
    onProgressUpdated: (progressValue: Float) -> Unit
) {
    this.animateTo(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = LinearEasing
        )
    ) {
        onProgressUpdated(value)
    }
}
