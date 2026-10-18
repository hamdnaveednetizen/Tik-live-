package com.example.data.repository

import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class VideoRepository {

    private val initialVideos = listOf(
        VideoItem(
            id = "vid_1",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            creatorName = "Alex Rivera",
            creatorHandle = "@alex_rivera",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            isVerified = true,
            caption = "Wait till the drop! Tried this transition 47 times today ⚡️🔥",
            hashtags = listOf("#fyp", "#transition", "#viral", "#dance", "#editing"),
            songTitle = "Neon Midnight (Bass Boost)",
            songArtist = "CyberBeat & Luna",
            songCoverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=150&auto=format&fit=crop&q=80",
            likeCount = 384200L,
            commentCount = 4210L,
            bookmarkCount = 51200L,
            shareCount = 18900L,
            isLiked = false,
            isBookmarked = false,
            isFollowing = false,
            primaryColor = 0xFFFE2C55,
            secondaryColor = 0xFF25F4EE,
            category = "for_you",
            comments = listOf(
                CommentItem("c1", "@sophia_art", "Sophia", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80", "The frame timing at 0:08 was INSANE 🔥", "2h ago", 1240, false),
                CommentItem("c2", "@marcus_vfx", "Marcus Tech", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80", "Tutorial please! Which shutter speed did you use?", "4h ago", 890, true),
                CommentItem("c3", "@elena_beats", "Elena", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&auto=format&fit=crop&q=80", "This audio is trending so fast right now", "6h ago", 312, false)
            )
        ),
        VideoItem(
            id = "vid_2",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            creatorName = "Chef Kaito",
            creatorHandle = "@kaitocooks",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80",
            isVerified = true,
            caption = "Crispy Honey Chili Glazed Salmon in under 10 minutes 🍣🍯 Sound on for crunch!",
            hashtags = listOf("#foodie", "#easyrecipe", "#asmr", "#cookingtiktok", "#fyp"),
            songTitle = "Lofi Kitchen Vibes (Pt. 2)",
            songArtist = "Kaito Audio & Sounds",
            songCoverUrl = "https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=150&auto=format&fit=crop&q=80",
            likeCount = 892400L,
            commentCount = 9812L,
            bookmarkCount = 142000L,
            shareCount = 64200L,
            isLiked = true,
            isBookmarked = true,
            isFollowing = true,
            primaryColor = 0xFFFF9900,
            secondaryColor = 0xFFFF3B30,
            category = "for_you",
            comments = listOf(
                CommentItem("c4", "@tastytreats", "Tasty Treats", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80", "Making this tonight! What kind of chili flakes?", "1h ago", 2100, false),
                CommentItem("c5", "@gordon_stan", "Max", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80", "The glaze caramelization is perfection chef 🧑‍🍳", "3h ago", 540, false)
            )
        ),
        VideoItem(
            id = "vid_3",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            creatorName = "Maya Explorer",
            creatorHandle = "@maya_wander",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&auto=format&fit=crop&q=80",
            isVerified = false,
            caption = "Secret mountain lagoon in the Dolomites you probably never heard of ⛰️💧 Save this for your next trip!",
            hashtags = listOf("#traveltok", "#italy", "#bucketlist", "#nature", "#wanderlust"),
            songTitle = "Golden Hour (Acoustic Remix)",
            songArtist = "Sol & Strings",
            songCoverUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=150&auto=format&fit=crop&q=80",
            likeCount = 512900L,
            commentCount = 3450L,
            bookmarkCount = 88900L,
            shareCount = 22100L,
            isLiked = false,
            isBookmarked = false,
            isFollowing = false,
            primaryColor = 0xFF00C9FF,
            secondaryColor = 0xFF92FE9D,
            category = "following",
            comments = listOf(
                CommentItem("c6", "@traveler_sam", "Sam", "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=150&auto=format&fit=crop&q=80", "Exact GPS pin? The water looks unreal!", "5h ago", 720, false)
            )
        ),
        VideoItem(
            id = "vid_4",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            creatorName = "TechPulse",
            creatorHandle = "@techpulse_daily",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&auto=format&fit=crop&q=80",
            isVerified = true,
            caption = "Top 3 hidden Android features you aren't using in 2026 📱🚀 Number 2 blew my mind!",
            hashtags = listOf("#techtok", "#androidtips", "#gadgets", "#techhacks", "#phone"),
            songTitle = "Future Wave Synth",
            songArtist = "HyperDrive Studio",
            songCoverUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=150&auto=format&fit=crop&q=80",
            likeCount = 248000L,
            commentCount = 1890L,
            bookmarkCount = 36700L,
            shareCount = 14300L,
            isLiked = false,
            isBookmarked = false,
            isFollowing = true,
            primaryColor = 0xFF6C5CE7,
            secondaryColor = 0xFFA29BFE,
            category = "for_you",
            comments = listOf(
                CommentItem("c7", "@gadgetguy", "Liam", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150&auto=format&fit=crop&q=80", "Just turned on feature #2, so useful thanks!", "2h ago", 410, false)
            )
        ),
        VideoItem(
            id = "vid_5",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            creatorName = "Zara Dance Studio",
            creatorHandle = "@zara_moves",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80",
            isVerified = true,
            caption = "New weekly dance routine! Tag someone who needs to learn this 💃✨ #dancechallenge",
            hashtags = listOf("#dance", "#choreography", "#trending", "#dancechallenge", "#groove"),
            songTitle = "Energy Pulse (Club Mix)",
            songArtist = "DJ Pulse & Groove",
            songCoverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=150&auto=format&fit=crop&q=80",
            likeCount = 671000L,
            commentCount = 5400L,
            bookmarkCount = 49200L,
            shareCount = 27800L,
            isLiked = false,
            isBookmarked = false,
            isFollowing = false,
            primaryColor = 0xFFFF2A6D,
            secondaryColor = 0xFF05D9E8,
            category = "following",
            comments = listOf(
                CommentItem("c8", "@dancer_chloe", "Chloe", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=150&auto=format&fit=crop&q=80", "Learning this right now!! Love the footwork 😍", "30m ago", 330, false)
            )
        )
    )

    private val _videos = MutableStateFlow<List<VideoItem>>(initialVideos)
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    fun toggleLike(videoId: String) {
        _videos.value = _videos.value.map { video ->
            if (video.id == videoId) {
                val newLiked = !video.isLiked
                val newCount = if (newLiked) video.likeCount + 1 else (video.likeCount - 1).coerceAtLeast(0)
                video.copy(isLiked = newLiked, likeCount = newCount)
            } else video
        }
    }

    fun toggleBookmark(videoId: String) {
        _videos.value = _videos.value.map { video ->
            if (video.id == videoId) {
                val newBookmarked = !video.isBookmarked
                val newCount = if (newBookmarked) video.bookmarkCount + 1 else (video.bookmarkCount - 1).coerceAtLeast(0)
                video.copy(isBookmarked = newBookmarked, bookmarkCount = newCount)
            } else video
        }
    }

    fun toggleFollow(videoId: String) {
        val target = _videos.value.find { it.id == videoId } ?: return
        val newFollowState = !target.isFollowing
        _videos.value = _videos.value.map { video ->
            if (video.creatorHandle == target.creatorHandle) {
                video.copy(isFollowing = newFollowState)
            } else video
        }
    }

    fun addComment(videoId: String, text: String, userHandle: String = "@you", userName: String = "You"): CommentItem {
        val newComment = CommentItem(
            id = UUID.randomUUID().toString(),
            userHandle = userHandle,
            userName = userName,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            text = text,
            timeAgo = "Just now",
            likeCount = 0,
            isLiked = false
        )
        _videos.value = _videos.value.map { video ->
            if (video.id == videoId) {
                video.copy(
                    comments = listOf(newComment) + video.comments,
                    commentCount = video.commentCount + 1
                )
            } else video
        }
        return newComment
    }

    fun toggleCommentLike(videoId: String, commentId: String) {
        _videos.value = _videos.value.map { video ->
            if (video.id == videoId) {
                val updatedComments = video.comments.map { comment ->
                    if (comment.id == commentId) {
                        val newLiked = !comment.isLiked
                        val newCount = if (newLiked) comment.likeCount + 1 else (comment.likeCount - 1).coerceAtLeast(0)
                        comment.copy(isLiked = newLiked, likeCount = newCount)
                    } else comment
                }
                video.copy(comments = updatedComments)
            } else video
        }
    }

    fun addUploadedVideo(caption: String, soundTitle: String, hashtags: List<String>) {
        val newVideo = VideoItem(
            id = "user_vid_${System.currentTimeMillis()}",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            creatorName = "You",
            creatorHandle = "@iamcreator",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            isVerified = true,
            caption = caption.ifEmpty { "Created with TikTok Studio 🎥✨" },
            hashtags = hashtags.ifEmpty { listOf("#fyp", "#newvideo", "#creator") },
            songTitle = soundTitle.ifEmpty { "Original Sound - @iamcreator" },
            songArtist = "@iamcreator",
            songCoverUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=150&auto=format&fit=crop&q=80",
            likeCount = 1L,
            commentCount = 0L,
            bookmarkCount = 0L,
            shareCount = 0L,
            isLiked = true,
            isBookmarked = false,
            isFollowing = true,
            primaryColor = 0xFF25F4EE,
            secondaryColor = 0xFFFE2C55,
            category = "for_you",
            comments = emptyList()
        )
        _videos.value = listOf(newVideo) + _videos.value
    }
}
