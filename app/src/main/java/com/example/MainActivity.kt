package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TikTokBottomNav
import com.example.ui.screens.CreateScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InboxScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokPink
import com.example.viewmodel.MainTab
import com.example.viewmodel.TikTokViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TikTokApp()
            }
        }
    }
}

@Composable
fun TikTokApp(
    viewModel: TikTokViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokDarkBg)
    ) {
        // Main Screen Body
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (selectedTab == MainTab.CREATE) 0.dp else 52.dp)
        ) {
            when (selectedTab) {
                MainTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToSearch = { viewModel.selectTab(MainTab.DISCOVER) }
                )
                MainTab.DISCOVER -> DiscoverScreen(
                    viewModel = viewModel
                )
                MainTab.CREATE -> CreateScreen(
                    viewModel = viewModel
                )
                MainTab.INBOX -> InboxScreen(
                    viewModel = viewModel
                )
                MainTab.PROFILE -> ProfileScreen(
                    viewModel = viewModel
                )
            }
        }

        // Bottom Navigation Bar (Hidden on camera create screen)
        if (selectedTab != MainTab.CREATE) {
            TikTokBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Toast Feedback Notification Banner
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            toastMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF22222A).copy(alpha = 0.95f))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
