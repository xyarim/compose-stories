package com.xyarim.stories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.xyarim.composestories.StoryItem
import kotlin.random.Random

private val randomSeed = Random(1337).nextInt()
val dummyStories = (0..4).mapIndexed { i, v ->
    StoryItem(
        content = {
            val contentUrl = "https://picsum.photos/seed/${randomSeed+i}/2000/3000?random=1"
            val profileUrl = "https://thispersondoesnotexist.com/?${i+30}"
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    model = contentUrl,
                    contentDescription = null,
                )
                StoryImage(imageUrl = profileUrl)
            }
        })
}

@Composable
fun StoryImage(imageUrl: String) {
    val shape = CircleShape
    Box(
        modifier = Modifier.padding(start = 18.dp, top = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = Color.LightGray, shape = shape)
                .clip(shape)
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl + "?${imageUrl}"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
