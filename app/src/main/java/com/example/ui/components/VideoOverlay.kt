package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.VideoItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokPink
import com.example.ui.theme.TikTokYellow
import com.example.viewmodel.BurstHeart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun VideoOverlay(
    video: VideoItem,
    progress: Float,
    burstHearts: List<BurstHeart>,
    onBurstHeartDone: (Long) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    onFollowClick: () -> Unit,
    onHashtagClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Bottom gradient for readable text and captions
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
        )

        // Flying hearts layer for double-tap
        burstHearts.forEach { heart ->
            SingleBurstHeart(
                heart = heart,
                onFinished = { onBurstHeartDone(heart.id) }
            )
        }

        // Right side vertical action icons
        RightActionsBar(
            video = video,
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick,
            onBookmarkClick = onBookmarkClick,
            onShareClick = onShareClick,
            onFollowClick = onFollowClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 28.dp)
        )

        // Bottom Left information (Creator, Caption, Sound marquee)
        BottomVideoInfo(
            video = video,
            onHashtagClick = onHashtagClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 90.dp, bottom = 28.dp)
        )

        // Slim scrubber progress bar at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .height(2.dp)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun RightActionsBar(
    video: VideoItem,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Creator Avatar with Follow Button
        CreatorAvatarWithFollow(
            avatarUrl = video.creatorAvatarUrl,
            isFollowing = video.isFollowing,
            onFollowClick = onFollowClick
        )

        // Like Button
        val likeScale = remember { Animatable(1f) }
        val scope = rememberCoroutineScope()
        ActionItem(
            icon = if (video.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            iconColor = if (video.isLiked) TikTokPink else Color.White,
            countText = formatCount(video.likeCount),
            testTag = "like_button",
            scale = likeScale.value,
            onClick = {
                scope.launch {
                    likeScale.animateTo(1.35f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    likeScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioHighBouncy))
                }
                onLikeClick()
            }
        )

        // Comments Button
        ActionItem(
            icon = Icons.Filled.Comment,
            iconColor = Color.White,
            countText = formatCount(video.commentCount),
            testTag = "comment_button",
            onClick = onCommentClick
        )

        // Bookmark / Favorite Button
        val bookmarkScale = remember { Animatable(1f) }
        ActionItem(
            icon = if (video.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
            iconColor = if (video.isBookmarked) TikTokYellow else Color.White,
            countText = formatCount(video.bookmarkCount),
            testTag = "bookmark_button",
            scale = bookmarkScale.value,
            onClick = {
                scope.launch {
                    bookmarkScale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    bookmarkScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioHighBouncy))
                }
                onBookmarkClick()
            }
        )

        // Share Button
        ActionItem(
            icon = Icons.Filled.Share,
            iconColor = Color.White,
            countText = formatCount(video.shareCount),
            testTag = "share_button",
            onClick = onShareClick
        )

        // Spinning Vinyl Music Disc
        SpinningVinylDisc(
            songCoverUrl = video.songCoverUrl
        )
    }
}

@Composable
fun CreatorAvatarWithFollow(
    avatarUrl: String,
    isFollowing: Boolean,
    onFollowClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        // Avatar circle with white border
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Creator Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .border(1.5.dp, Color.White, CircleShape)
        )

        // Follow Plus Badge
        AnimatedVisibility(
            visible = !isFollowing,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(TikTokPink)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onFollowClick
                    )
                    .testTag("follow_badge"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-1).dp)
                )
            }
        }
    }
}

@Composable
fun ActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    countText: String,
    testTag: String,
    scale: Float = 1f,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = countText,
                tint = iconColor,
                modifier = Modifier.size(34.dp)
            )
        }
        Text(
            text = countText,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun SpinningVinylDisc(
    songCoverUrl: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_rotation"
    )

    // Floating note animation
    val noteFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "music_note"
    )

    Box(
        modifier = modifier.size(54.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating note escaping
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = Color.White.copy(alpha = (1f - noteFloat).coerceIn(0f, 0.8f)),
            modifier = Modifier
                .size(14.dp)
                .offset {
                    IntOffset(
                        x = (-16 - (noteFloat * 18)).roundToInt(),
                        y = (-14 - (noteFloat * 28)).roundToInt()
                    )
                }
                .rotate(noteFloat * 45f)
        )

        // Outer Vinyl Disc
        Box(
            modifier = Modifier
                .size(46.dp)
                .rotate(rotation)
                .clip(CircleShape)
                .background(Color(0xFF151515))
                .border(1.dp, Color(0xFF2A2A2A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Grooves
            Canvas(modifier = Modifier.size(46.dp)) {
                drawCircle(
                    color = Color(0xFF282828),
                    radius = size.width * 0.42f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                )
                drawCircle(
                    color = Color(0xFF333333),
                    radius = size.width * 0.32f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                )
            }

            // Center Cover Art
            AsyncImage(
                model = songCoverUrl,
                contentDescription = "Song Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun BottomVideoInfo(
    video: VideoItem,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Creator Handle and Verified Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = video.creatorHandle,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            if (video.isVerified) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(TikTokCyan),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Verified",
                        tint = Color.Black,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }

        // Caption Text with tags
        Text(
            text = video.caption,
            color = Color.White.copy(alpha = 0.95f),
            fontSize = 14.sp,
            lineHeight = 19.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        // Hashtags row
        if (video.hashtags.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                video.hashtags.take(4).forEach { tag ->
                    Text(
                        text = tag,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onHashtagClick(tag) }
                    )
                }
            }
        }

        // Audio Marquee Ticker
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )

            val infiniteTransition = rememberInfiniteTransition(label = "ticker")
            val scrollOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -180f,
                animationSpec = infiniteRepeatable(
                    animation = tween(7000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "sound_ticker"
            )

            Box(
                modifier = Modifier
                    .width(180.dp)
                    .graphicsLayer { clip = true }
            ) {
                Text(
                    text = "${video.songTitle} - ${video.songArtist}  •  ${video.songTitle} - ${video.songArtist}",
                    color = Color.White,
                    fontSize = 13.sp,
                    maxLines = 1,
                    modifier = Modifier.offset { IntOffset(scrollOffset.roundToInt(), 0) }
                )
            }
        }
    }
}

@Composable
fun SingleBurstHeart(
    heart: BurstHeart,
    onFinished: () -> Unit
) {
    val scale = remember { Animatable(0.2f) }
    val alpha = remember { Animatable(1f) }
    val yOffset = remember { Animatable(0f) }

    LaunchedEffect(heart.id) {
        launch {
            scale.animateTo(
                targetValue = 1.3f,
                animationSpec = tween(150, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(100)
            )
        }
        launch {
            yOffset.animateTo(
                targetValue = -120f,
                animationSpec = tween(650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(400)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(250)
            )
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = (heart.offset.x - 45).roundToInt(),
                    y = (heart.offset.y - 45 + yOffset.value).roundToInt()
                )
            }
            .scale(scale.value)
            .rotate(heart.rotation)
            .graphicsLayer { this.alpha = alpha.value }
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = TikTokPink,
            modifier = Modifier.size(80.dp)
        )
    }
}

fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}
