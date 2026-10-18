package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokPink
import com.example.ui.theme.TikTokTextMuted
import com.example.viewmodel.MainTab
import com.example.viewmodel.TikTokViewModel
import kotlinx.coroutines.delay

@Composable
fun CreateScreen(
    viewModel: TikTokViewModel,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var recordTimeSeconds by remember { mutableIntStateOf(0) }
    var selectedSound by remember { mutableStateOf("Origami - Metro Beats (Trending)") }
    var selectedSpeed by remember { mutableStateOf("1x") }
    var flashEnabled by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(0) }
    var isFrontCamera by remember { mutableStateOf(false) }
    var showPostDialog by remember { mutableStateOf(false) }

    // Caption & tags for publishing
    var postCaption by remember { mutableStateOf("") }
    var postHashtags by remember { mutableStateOf("#fyp, #creativetok, #viral") }

    // Recording timer simulation
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordTimeSeconds = 0
            while (isRecording && recordTimeSeconds < 15) {
                delay(1000)
                recordTimeSeconds += 1
            }
            if (recordTimeSeconds >= 15) {
                isRecording = false
                showPostDialog = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Viewfinder Canvas Simulation
        CameraViewfinderBackground(
            isFront = isFrontCamera,
            isRecording = isRecording
        )

        // Top bar with close, sound selector, and flash
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Close Button
            IconButton(
                onClick = { viewModel.selectTab(MainTab.HOME) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .testTag("close_camera_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Camera",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Sound Selector Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        viewModel.showToast("Sound selected: $selectedSound")
                    }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = TikTokCyan,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = selectedSound.take(20) + "...",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Flash Toggle
            IconButton(
                onClick = {
                    flashEnabled = !flashEnabled
                    viewModel.showToast(if (flashEnabled) "Flash ON" else "Flash OFF")
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (flashEnabled) TikTokPink else Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Flash",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Right side camera controls tool strip
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CameraToolButton(
                icon = Icons.Default.FlipCameraAndroid,
                label = "Flip",
                onClick = {
                    isFrontCamera = !isFrontCamera
                    viewModel.showToast(if (isFrontCamera) "Front Camera" else "Back Camera")
                }
            )
            CameraToolButton(
                icon = Icons.Default.Speed,
                label = selectedSpeed,
                onClick = {
                    selectedSpeed = when (selectedSpeed) {
                        "1x" -> "2x"
                        "2x" -> "3x"
                        "3x" -> "0.5x"
                        else -> "1x"
                    }
                    viewModel.showToast("Speed: $selectedSpeed")
                }
            )
            CameraToolButton(
                icon = Icons.Default.AutoAwesome,
                label = "Beauty",
                onClick = { viewModel.showToast("Beauty filter applied ✨") }
            )
            CameraToolButton(
                icon = Icons.Default.Timer,
                label = if (timerSeconds > 0) "${timerSeconds}s" else "Timer",
                onClick = {
                    timerSeconds = if (timerSeconds == 0) 3 else if (timerSeconds == 3) 10 else 0
                    viewModel.showToast(if (timerSeconds > 0) "Timer: ${timerSeconds}s" else "Timer OFF")
                }
            )
        }

        // Bottom Controls: Recording duration, Record Button, Checkmark Next
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Recording Timer Text
            if (isRecording) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TikTokPink)
                    )
                    Text(
                        text = String.format("00:%02d / 00:15", recordTimeSeconds),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // Duration selection tabs (15s, 60s, Photo)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "15s",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "60s",
                        color = TikTokTextMuted,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Templates",
                        color = TikTokTextMuted,
                        fontSize = 14.sp
                    )
                }
            }

            // Big Red Record Button + Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Effects Icon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { viewModel.showToast("Effects Library opened") }
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TikTokCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Effects",
                            tint = TikTokCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Effects", color = Color.White, fontSize = 11.sp)
                }

                // Main Shutter / Record Button
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(TikTokPink)
                        .clickable {
                            if (!isRecording) {
                                isRecording = true
                            } else {
                                isRecording = false
                                showPostDialog = true
                            }
                        }
                        .testTag("record_shutter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRecording) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White)
                        )
                    }
                }

                // Next Checkmark (Active once recorded, or tap to finish)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            showPostDialog = true
                        }
                        .testTag("finish_recording_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TikTokCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Finish", color = Color.White, fontSize = 11.sp)
                }
            }
        }

        // Post Dialog
        if (showPostDialog) {
            AlertDialog(
                onDismissRequest = { showPostDialog = false },
                containerColor = TikTokDarkBg,
                title = {
                    Text("Post New TikTok", color = Color.White, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Add a caption and tags before publishing to the feed.",
                            color = TikTokTextMuted,
                            fontSize = 13.sp
                        )
                        OutlinedTextField(
                            value = postCaption,
                            onValueChange = { postCaption = it },
                            placeholder = { Text("Write a caption...", color = TikTokTextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = TikTokDarkSurface,
                                unfocusedContainerColor = TikTokDarkSurface,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        OutlinedTextField(
                            value = postHashtags,
                            onValueChange = { postHashtags = it },
                            placeholder = { Text("#hashtags", color = TikTokTextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = TikTokDarkSurface,
                                unfocusedContainerColor = TikTokDarkSurface,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val tagsList = postHashtags.split(",")
                                .map { it.trim() }
                                .filter { it.isNotBlank() }
                            viewModel.publishNewVideo(
                                caption = postCaption,
                                soundTitle = selectedSound,
                                tags = tagsList
                            )
                            showPostDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokPink)
                    ) {
                        Text("Post to Feed 🚀", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPostDialog = false }) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.7f))
                    }
                }
            )
        }
    }
}

@Composable
fun CameraToolButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CameraViewfinderBackground(
    isFront: Boolean,
    isRecording: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cam_ambient")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cam_pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Dark viewfinder preview gradient
        val baseColors = if (isFront) {
            listOf(Color(0xFF191024), Color(0xFF0F0B18), Color(0xFF07050A))
        } else {
            listOf(Color(0xFF101B24), Color(0xFF0A131A), Color(0xFF05080C))
        }
        drawRect(brush = Brush.verticalGradient(baseColors))

        // Center lens target brackets
        val centerX = width / 2f
        val centerY = height / 2f
        val bracketSize = 40.dp.toPx()
        val color = if (isRecording) TikTokPink.copy(alpha = 0.7f) else Color.White.copy(alpha = pulse)

        // 4 corners of focus square
        drawCircle(
            color = color,
            radius = 4.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        // Subtle rule-of-thirds grid
        val stroke = Stroke(width = 0.5.dp.toPx())
        val gridColor = Color.White.copy(alpha = 0.08f)
        drawLine(gridColor, Offset(width / 3f, 0f), Offset(width / 3f, height), strokeWidth = stroke.width)
        drawLine(gridColor, Offset(width * 2f / 3f, 0f), Offset(width * 2f / 3f, height), strokeWidth = stroke.width)
        drawLine(gridColor, Offset(0f, height / 3f), Offset(width, height / 3f), strokeWidth = stroke.width)
        drawLine(gridColor, Offset(0f, height * 2f / 3f), Offset(width, height * 2f / 3f), strokeWidth = stroke.width)
    }
}
