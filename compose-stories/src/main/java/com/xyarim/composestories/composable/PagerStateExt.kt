package com.xyarim.composestories.composable

import androidx.compose.foundation.pager.PagerState

internal suspend fun PagerState.onTap(tap: Tap) {
    when (tap) {
        Tap.RIGHT -> {
            if (canScrollForward) {
                scrollToPage(currentPage.inc())

            } else {
                scrollToPage(0)
            }
        }

        Tap.LEFT -> {
            if (canScrollBackward) {
                scrollToPage(currentPage.dec())
            }
        }
    }
}