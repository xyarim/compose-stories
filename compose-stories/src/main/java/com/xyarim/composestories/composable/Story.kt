package com.xyarim.composestories.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xyarim.composestories.StoryItem

@Composable
internal fun Story(
    storyItem: StoryItem,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize().then(modifier)) {
        storyItem.content()
    }
}