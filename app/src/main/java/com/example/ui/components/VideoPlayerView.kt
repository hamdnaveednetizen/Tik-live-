package com.example.ui.components

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun VideoPlayerView(
    video: VideoItem,
    isActivePage: Boolean,
    onSingleTap: () -> Unit,
    onDoubleTap: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    onProgressUpdate: (Float) -> Unit = {}
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var isBuffering by remember { mutableStateOf(true) }
    var isPlayerPrepared by remember { mutableStateOf(false) }
    var showPlayIcon by remember { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val scope = rememberCoroutineScope()

    // Sync playback state with active page and prepared status
    LaunchedEffect(isActivePage, isPlayerPrepared, isPlaying) {
        if (isPlayerPrepared) {
            mediaPlayer?.let { player ->
                try {
                    if (isActivePage && isPlaying) {
                        if (!player.isPlaying) {
                            player.start()
                        }
                    } else {
                        if (player.isPlaying) {
                            player.pause()
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }

    // Polling progress safely only when prepared
    LaunchedEffect(isActivePage, isPlaying, isPlayerPrepared) {
        while (isActive && isActivePage && isPlaying && isPlayerPrepared) {
            mediaPlayer?.let { player ->
                try {
                    if (player.isPlaying && player.duration > 0) {
                        val progress = player.currentPosition.toFloat() / player.duration.toFloat()
                        onProgressUpdate(progress.coerceIn(0f, 1f))
                    }
                } catch (_: Exception) {
                }
            }
            delay(100)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(video.id) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        onDoubleTap(offset)
                    },
                    onTap = {
                        isPlaying = !isPlaying
                        if (isPlayerPrepared) {
                            mediaPlayer?.let { player ->
                                try {
                                    if (isPlaying) {
                                        player.start()
                                        showPlayIcon = false
                                    } else {
                                        player.pause()
                                        showPlayIcon = true
                                    }
                                } catch (_: Exception) {
                                }
                            }
                        } else {
                            showPlayIcon = !isPlaying
                        }
                        onSingleTap()
                    }
                )
            }
    ) {
        // Video background ambient shader & fallback canvas
        DynamicVideoCanvasFallback(
            primaryColor = Color(video.primaryColor),
            secondaryColor = Color(video.secondaryColor),
            isPlaying = isPlaying && isActivePage
        )

        // Native MediaPlayer View
        if (isActivePage) {
            AndroidView(
                factory = { ctx ->
                    SurfaceView(ctx).apply {
                        holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                try {
                                    isPlayerPrepared = false
                                    try {
                                        mediaPlayer?.release()
                                    } catch (_: Exception) {}
                                    mediaPlayer = null

                                    val player = MediaPlayer().apply {
                                        setAudioAttributes(
                                            AudioAttributes.Builder()
                                                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                                .build()
                                        )
                                        setDisplay(holder)
                                        setDataSource(ctx, Uri.parse(video.videoUrl))
                                        isLooping = true
                                        setOnPreparedListener { mp ->
                                            isPlayerPrepared = true
                                            isBuffering = false
                                            try {
                                                if (isPlaying && isActivePage) {
                                                    mp.start()
                                                }
                                            } catch (_: Exception) {}
                                        }
                                        setOnInfoListener { _, what, _ ->
                                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                                                isBuffering = true
                                            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                                                isBuffering = false
                                            }
                                            true
                                        }
                                        setOnErrorListener { _, _, _ ->
                                            isPlayerPrepared = false
                                            isBuffering = false
                                            true // fallback canvas will seamlessly display
                                        }
                                        prepareAsync()
                                    }
                                    mediaPlayer = player
                                } catch (e: Exception) {
                                    isPlayerPrepared = false
                                    isBuffering = false
                                }
                            }

                            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

                            override fun surfaceDestroyed(holder: SurfaceHolder) {
                                isPlayerPrepared = false
                                try {
                                    mediaPlayer?.stop()
                                } catch (_: Exception) {}
                                try {
                                    mediaPlayer?.release()
                                } catch (_: Exception) {}
                                mediaPlayer = null
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        DisposableEffect(video.id) {
            onDispose {
                isPlayerPrepared = false
                try {
                    mediaPlayer?.release()
                } catch (_: Exception) {}
                mediaPlayer = null
            }
        }

        // Buffering indicator
        if (isBuffering && isActivePage) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.Center),
                color = TikTokPink,
                strokeWidth = 3.dp
            )
        }

        // Centered Big Pause / Play indicator
        AnimatedVisibility(
            visible = showPlayIcon && !isPlaying,
            enter = fadeIn(tween(150)) + scaleIn(tween(150)),
            exit = fadeOut(tween(200)) + scaleOut(tween(200)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Black.copy(alpha = 0.45f), shape = androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Video",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(54.dp)
                )
            }
        }
    }
}

@Composable
fun DynamicVideoCanvasFallback(
    primaryColor: Color,
    secondaryColor: Color,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_bg")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Dark atmospheric base
        drawRect(color = Color(0xFF07070A))

        // Vibrant radial gradient backlights
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primaryColor.copy(alpha = 0.35f * pulse), Color.Transparent),
                center = Offset(width * 0.3f, height * 0.4f),
                radius = width * 0.85f
            )
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(secondaryColor.copy(alpha = 0.3f * (1.1f - pulse)), Color.Transparent),
                center = Offset(width * 0.7f, height * 0.65f),
                radius = width * 0.75f
            )
        )

        // Subtle dynamic wave bars in background
        val barCount = 18
        val spacing = width / barCount
        for (i in 0 until barCount) {
            val barHeight = (height * 0.08f) * kotlin.math.sin((i * 0.4f) + (pulse * 3.14f)).toFloat().coerceAtLeast(0.1f)
            val barX = spacing * i + spacing * 0.5f
            val barY = height * 0.52f
            drawLine(
                color = Color.White.copy(alpha = 0.06f),
                start = Offset(barX, barY - barHeight),
                end = Offset(barX, barY + barHeight),
                strokeWidth = 3.dp.toPx()
            )
        }
    }
}
