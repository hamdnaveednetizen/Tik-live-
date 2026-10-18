package com.example.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    DISCOVER,
    CREATE,
    INBOX,
    PROFILE
}

enum class FeedType {
    FOLLOWING,
    FOR_YOU
}

data class BurstHeart(
    val id: Long,
    val offset: Offset,
    val rotation: Float
)

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val avatarUrl: String,
    val type: String, // like, comment, follower, mention
    val actionText: String? = null
)

class TikTokViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(MainTab.HOME)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _feedType = MutableStateFlow(FeedType.FOR_YOU)
    val feedType: StateFlow<FeedType> = _feedType.asStateFlow()

    val allVideos: StateFlow<List<VideoItem>> = repository.videos

    val currentFeedVideos: StateFlow<List<VideoItem>> = combine(allVideos, _feedType) { list, feed ->
        when (feed) {
            FeedType.FOR_YOU -> list
            FeedType.FOLLOWING -> {
                val filtered = list.filter { it.isFollowing || it.category == "following" }
                if (filtered.isEmpty()) list else filtered
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active bottom sheets
    private val _activeCommentsVideoId = MutableStateFlow<String?>(null)
    val activeCommentsVideoId: StateFlow<String?> = _activeCommentsVideoId.asStateFlow()

    private val _activeShareVideo = MutableStateFlow<VideoItem?>(null)
    val activeShareVideo: StateFlow<VideoItem?> = _activeShareVideo.asStateFlow()

    // Search query for discover
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Hearts bursting on double tap
    private val _burstHearts = MutableStateFlow<List<BurstHeart>>(emptyList())
    val burstHearts: StateFlow<List<BurstHeart>> = _burstHearts.asStateFlow()

    // Transient message toast / banner
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // User profile video tab
    private val _profileTab = MutableStateFlow(0) // 0: My Videos, 1: Bookmarks, 2: Liked
    val profileTab: StateFlow<Int> = _profileTab.asStateFlow()

    // Inbox notifications
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem("n1", "Alex Rivera liked your comment", "\"The frame timing was INSANE 🔥\"", "10m ago", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80", "like"),
            NotificationItem("n2", "Elena started following you", "Say hi and view their videos", "1h ago", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&auto=format&fit=crop&q=80", "follower", "Follow back"),
            NotificationItem("n3", "Zara Dance Studio mentioned you", "Check out the new choreography clip", "3h ago", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80", "mention"),
            NotificationItem("n4", "Chef Kaito liked your video", "Your clip reached 10,000 views today 🎉", "1d ago", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80", "like")
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun selectTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun setFeedType(type: FeedType) {
        _feedType.value = type
    }

    fun setProfileTab(tabIndex: Int) {
        _profileTab.value = tabIndex
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleLike(videoId: String) {
        repository.toggleLike(videoId)
    }

    fun toggleBookmark(videoId: String) {
        repository.toggleBookmark(videoId)
        val video = allVideos.value.find { it.id == videoId }
        if (video != null && !video.isBookmarked) {
            showToast("Added to Favorites")
        } else {
            showToast("Removed from Favorites")
        }
    }

    fun toggleFollow(videoId: String) {
        repository.toggleFollow(videoId)
    }

    fun openComments(videoId: String) {
        _activeCommentsVideoId.value = videoId
    }

    fun closeComments() {
        _activeCommentsVideoId.value = null
    }

    fun openShare(video: VideoItem) {
        _activeShareVideo.value = video
    }

    fun closeShare() {
        _activeShareVideo.value = null
    }

    fun addComment(videoId: String, text: String) {
        if (text.isNotBlank()) {
            repository.addComment(videoId, text.trim())
        }
    }

    fun toggleCommentLike(videoId: String, commentId: String) {
        repository.toggleCommentLike(videoId, commentId)
    }

    fun addBurstHeart(offset: Offset, videoId: String) {
        val id = System.currentTimeMillis()
        val randomRot = (-25..25).random().toFloat()
        _burstHearts.value = _burstHearts.value + BurstHeart(id, offset, randomRot)

        // If not already liked, like the video
        val video = allVideos.value.find { it.id == videoId }
        if (video != null && !video.isLiked) {
            repository.toggleLike(videoId)
        }
    }

    fun removeBurstHeart(id: Long) {
        _burstHearts.value = _burstHearts.value.filter { it.id != id }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
        viewModelScope.launch {
            kotlinx.coroutines.delay(2500)
            if (_toastMessage.value == message) {
                _toastMessage.value = null
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun publishNewVideo(caption: String, soundTitle: String, tags: List<String>) {
        repository.addUploadedVideo(caption, soundTitle, tags)
        showToast("Video posted successfully! 🎉")
        _selectedTab.value = MainTab.HOME
        _feedType.value = FeedType.FOR_YOU
    }
}
