package com.xyarim.stories

import Stories
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.xyarim.composestories.composable.ProgressIndicatorTheme
import com.xyarim.stories.ui.theme.ComposeStoriesTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            ),
        )
        setContent {
            ComposeStoriesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Compose Stories")
                            },
                        )
                    }
                ) { innerPadding ->
                    Stories(
                        modifier = Modifier.padding(innerPadding),
                        progressIndicatorTheme = ProgressIndicatorTheme(
                           modifier = Modifier.padding(all = 16.dp)
                        ),
                        stories = dummyStories,
                        stepDuration = 2000
                    )
                }
            }
        }
    }
}

