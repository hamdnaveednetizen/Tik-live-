package com.example.data.model

data class VideoItem(
    val id: String,
    val videoUrl: String,
    val creatorName: String,
    val creatorHandle: String,
    val creatorAvatarUrl: String,
    val isVerified: Boolean = true,
    val caption: String,
    val hashtags: List<String>,
    val songTitle: String,
    val songArtist: String,
    val songCoverUrl: String,
    val likeCount: Long,
    val commentCount: Long,
    val bookmarkCount: Long,
    val shareCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val isFollowing: Boolean = false,
    val primaryColor: Long = 0xFFFE2C55,
    val secondaryColor: Long = 0xFF25F4EE,
    val category: String = "for_you",
    val comments: List<CommentItem> = emptyList()
)
