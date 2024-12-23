    package com.example.to_dolist.screens.pomodoro

    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.size
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.unit.dp
    import coil.compose.AsyncImage
    import com.example.to_dolist.R

    @Composable
    fun ConfettiCelebration() {
        val context = LocalContext.current
        val gifPath = R.raw.animation

        AsyncImage(
            model = gifPath,
            contentDescription = "Celebration GIF",
        )
    }
