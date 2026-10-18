package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.TopBar
import com.example.ui.components.VideoOverlay
import com.example.ui.components.VideoPlayerView
import com.example.ui.theme.TikTokDarkBg
import com.example.viewmodel.FeedType
import com.example.viewmodel.MainTab
import com.example.viewmodel.TikTokViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TikTokViewModel,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.currentFeedVideos.collectAsState()
    val feedType by viewModel.feedType.collectAsState()
    val burstHearts by viewModel.burstHearts.collectAsState()
    val activeCommentsVideoId by viewModel.activeCommentsVideoId.collectAsState()
    val activeShareVideo by viewModel.activeShareVideo.collectAsState()

    val pagerState = rememberPagerState(pageCount = { videos.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokDarkBg)
    ) {
        if (videos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No videos in this feed yet",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { pageIndex ->
                val video = videos.getOrNull(pageIndex)
                if (video != null) {
                    val isActivePage = pagerState.currentPage == pageIndex
                    var progress by remember { mutableFloatStateOf(0f) }

                    Box(modifier = Modifier.fillMaxSize()) {
                        // Video Player
                        VideoPlayerView(
                            video = video,
                            isActivePage = isActivePage,
                            onSingleTap = { /* handled inside player */ },
                            onDoubleTap = { offset ->
                                viewModel.addBurstHeart(offset, video.id)
                            },
                            onProgressUpdate = { progress = it }
                        )

                        // TikTok HUD Overlay
                        VideoOverlay(
                            video = video,
                            progress = progress,
                            burstHearts = burstHearts,
                            onBurstHeartDone = { id -> viewModel.removeBurstHeart(id) },
                            onLikeClick = { viewModel.toggleLike(video.id) },
                            onCommentClick = { viewModel.openComments(video.id) },
                            onBookmarkClick = { viewModel.toggleBookmark(video.id) },
                            onShareClick = { viewModel.openShare(video) },
                            onFollowClick = { viewModel.toggleFollow(video.id) },
                            onHashtagClick = { tag ->
                                viewModel.onSearchQueryChange(tag)
                                viewModel.selectTab(MainTab.DISCOVER)
                            }
                        )
                    }
                }
            }
        }

        // Top Bar (LIVE, Following | For You, Search)
        TopBar(
            currentFeed = feedType,
            onFeedSelected = { viewModel.setFeedType(it) },
            onSearchClick = onNavigateToSearch,
            onLiveClick = { viewModel.showToast("No active live streams right now") },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Comments Bottom Sheet
        if (activeCommentsVideoId != null) {
            val videoForComments = videos.find { it.id == activeCommentsVideoId }
                ?: viewModel.allVideos.value.find { it.id == activeCommentsVideoId }
            if (videoForComments != null) {
                CommentsBottomSheet(
                    video = videoForComments,
                    onDismiss = { viewModel.closeComments() },
                    onAddComment = { text ->
                        viewModel.addComment(videoForComments.id, text)
                    },
                    onToggleCommentLike = { commentId ->
                        viewModel.toggleCommentLike(videoForComments.id, commentId)
                    }
                )
            }
        }

        // Share Bottom Sheet
        if (activeShareVideo != null) {
            ShareBottomSheet(
                video = activeShareVideo!!,
                onDismiss = { viewModel.closeShare() },
                onCopyLink = {
                    viewModel.showToast("Link copied to clipboard! 🔗")
                    viewModel.closeShare()
                },
                onSaveVideo = {
                    viewModel.showToast("Video saved to your device! 📥")
                    viewModel.closeShare()
                },
                onDirectSend = { friendName ->
                    viewModel.showToast("Sent to $friendName! ✈️")
                    viewModel.closeShare()
                }
            )
        }
    }
}
