package com.example.newsfeed.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsfeed.data.remote.dto.ArticleDto
import com.example.newsfeed.ui.model.HomeViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.collectAsState
import com.example.newsfeed.ui.model.HomeUiState


// danh sách tab bottom nav
private val bottomNavItems = listOf(
    BottomNavItem("Trang chủ", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Đã lưu",    Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    BottomNavItem("Báo chí",   Icons.Filled.Newspaper, Icons.Outlined.Newspaper)
)
@Composable
fun MainScreen(
    viewModel: HomeViewModel = viewModel(),
    onArticleClick: (ArticleDto) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> {
                    HomeScreen(
                        onArticleClick = onArticleClick,
                        onLogout = onLogout
                    )
                }

                1 -> {
                    // Tab Đã lưu (tạm)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📌 Đã lưu\nĐang phát triển...", textAlign = TextAlign.Center)
                    }
                }

                2 -> {
                    BaoChiScreen(onArticleClick = onArticleClick)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                }
            }
        }

        // ===== FOOTER (BOTTOM NAVIGATION) =====
        NavigationBar {
            bottomNavItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label) }
                )
            }
        }
    }
}



