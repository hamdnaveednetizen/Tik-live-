package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.VideoItem
import com.example.ui.components.formatCount
import com.example.ui.theme.TikTokCard
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokDarkSurface
import com.example.ui.theme.TikTokPink
import com.example.ui.theme.TikTokTextDim
import com.example.ui.theme.TikTokTextMuted
import com.example.ui.theme.TikTokYellow
import com.example.viewmodel.MainTab
import com.example.viewmodel.TikTokViewModel

@Composable
fun ProfileScreen(
    viewModel: TikTokViewModel,
    modifier: Modifier = Modifier
) {
    val allVideos by viewModel.allVideos.collectAsState()
    val activeProfileTab by viewModel.profileTab.collectAsState()

    val myVideos = allVideos.filter { it.creatorHandle == "@iamcreator" || it.id.startsWith("user_") }
    val bookmarkedVideos = allVideos.filter { it.isBookmarked }
    val likedVideos = allVideos.filter { it.isLiked }

    val displayVideos = when (activeProfileTab) {
        0 -> if (myVideos.isNotEmpty()) myVideos else allVideos.take(2)
        1 -> bookmarkedVideos
        2 -> likedVideos
        else -> allVideos
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokDarkBg)
            .statusBarsPadding()
    ) {
        // Top profile bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Alex Creator",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { viewModel.showToast("Profile link copied! 🔗") },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Profile",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { viewModel.showToast("Settings & Privacy") },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Profile Avatar and Username
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                    contentDescription = "My Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, TikTokPink, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(TikTokCyan)
                        .clickable { viewModel.showToast("Change Avatar") },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "@iamcreator",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Stats row (Following, Followers, Likes)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat(count = "342", label = "Following")
                ProfileStat(count = "28.4K", label = "Followers")
                ProfileStat(count = "1.2M", label = "Likes")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons (Edit Profile, Share Profile)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.showToast("Edit Profile clicked") },
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edit profile", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { viewModel.showToast("Profile shared!") },
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokCard),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Share profile", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bio
            Text(
                text = "✨ Creating daily short stories | Tech & Design 🚀\nTap the plus button to record new clips! 👇",
                color = TikTokTextDim,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Media Tabs: ▦ My Videos, 🔖 Bookmarks, ❤️ Likes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = Color.White.copy(alpha = 0.1f)
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ProfileTabIcon(
                icon = Icons.Default.GridOn,
                isSelected = activeProfileTab == 0,
                onClick = { viewModel.setProfileTab(0) },
                testTag = "profile_tab_my_videos"
            )
            ProfileTabIcon(
                icon = Icons.Default.Bookmark,
                isSelected = activeProfileTab == 1,
                onClick = { viewModel.setProfileTab(1) },
                testTag = "profile_tab_bookmarks"
            )
            ProfileTabIcon(
                icon = Icons.Default.Favorite,
                isSelected = activeProfileTab == 2,
                onClick = { viewModel.setProfileTab(2) },
                testTag = "profile_tab_liked"
            )
        }

        // Grid of Videos in selected tab
        if (displayVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = when (activeProfileTab) {
                        1 -> "No favorite videos yet. Tap 🔖 to save videos!"
                        2 -> "No liked videos yet. Tap ❤️ on any video!"
                        else -> "No videos recorded yet. Tap [+] to create!"
                    },
                    color = TikTokTextMuted,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                items(displayVideos, key = { it.id }) { video ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(0.75f)
                            .background(TikTokDarkSurface)
                            .clickable { viewModel.selectTab(MainTab.HOME) }
                    ) {
                        AsyncImage(
                            model = video.songCoverUrl,
                            contentDescription = video.caption,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Bottom gradient & view count
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                    )
                                )
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = formatCount(video.likeCount),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = TikTokTextMuted, fontSize = 12.sp)
    }
}

@Composable
fun ProfileTabIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
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
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else TikTokTextMuted,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(2.dp)
                    .background(Color.White)
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
