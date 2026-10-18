package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokPink
import com.example.ui.theme.TikTokTextMuted
import com.example.viewmodel.MainTab

@Composable
fun TikTokBottomNav(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(TikTokDarkBg)
            .navigationBarsPadding()
    ) {
        // Thin top divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.White.copy(alpha = 0.12f))
                .align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Home Tab
            BottomNavItem(
                title = "Home",
                icon = if (selectedTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                isSelected = selectedTab == MainTab.HOME,
                onClick = { onTabSelected(MainTab.HOME) },
                testTag = "nav_home"
            )

            // Discover Tab
            BottomNavItem(
                title = "Discover",
                icon = Icons.Default.Search,
                isSelected = selectedTab == MainTab.DISCOVER,
                onClick = { onTabSelected(MainTab.DISCOVER) },
                testTag = "nav_discover"
            )

            // TikTok Iconic Create (+) Button
            TikTokAddButton(
                onClick = { onTabSelected(MainTab.CREATE) },
                testTag = "nav_create"
            )

            // Inbox Tab
            BottomNavItem(
                title = "Inbox",
                icon = if (selectedTab == MainTab.INBOX) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                isSelected = selectedTab == MainTab.INBOX,
                hasBadge = true,
                onClick = { onTabSelected(MainTab.INBOX) },
                testTag = "nav_inbox"
            )

            // Profile Tab
            BottomNavItem(
                title = "Profile",
                icon = if (selectedTab == MainTab.PROFILE) Icons.Filled.Person else Icons.Outlined.PersonOutline,
                isSelected = selectedTab == MainTab.PROFILE,
                onClick = { onTabSelected(MainTab.PROFILE) },
                testTag = "nav_profile"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    hasBadge: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else TikTokTextMuted,
                modifier = Modifier.size(24.dp)
            )
            if (hasBadge) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 2.dp, y = (-2).dp)
                        .clip(CircleShape)
                        .background(TikTokPink)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else TikTokTextMuted,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun TikTokAddButton(
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Cyan left underlay
        Box(
            modifier = Modifier
                .offset(x = (-3).dp)
                .size(width = 38.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TikTokCyan)
        )

        // Pink right underlay
        Box(
            modifier = Modifier
                .offset(x = 3.dp)
                .size(width = 38.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TikTokPink)
        )

        // White center pill
        Box(
            modifier = Modifier
                .size(width = 38.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Video",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
