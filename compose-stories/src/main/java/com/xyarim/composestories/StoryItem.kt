package com.xyarim.composestories

import androidx.compose.runtime.Composable

data class StoryItem(
    val content: @Composable () -> Unit,
)