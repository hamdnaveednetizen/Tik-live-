package com.example.data.model

data class CommentItem(
    val id: String,
    val userHandle: String,
    val userName: String,
    val avatarUrl: String,
    val text: String,
    val timeAgo: String,
    val likeCount: Int,
    val isLiked: Boolean = false
)
