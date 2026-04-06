package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swen549.touchanalytics.Constants

@Composable
fun HomeScreen(
    userId: Int,
    navigateBack: () -> Unit,
    sharedViewModel: TouchAnalyticsViewModel,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    ),
) {
    val appMode by sharedViewModel.mode.collectAsStateWithLifecycle()
    val showMoreMenu by viewModel.showMoreMenu.collectAsStateWithLifecycle()
    val enrollmentCount by sharedViewModel.enrollmentCount.collectAsStateWithLifecycle()
    val matchCount by sharedViewModel.matchCount.collectAsStateWithLifecycle()
    val nonmatchCount by sharedViewModel.nonmatchCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar(
                userId = userId,
                navigateBack = navigateBack,
                showMoreMenu = showMoreMenu,
                setShowMoreMenu = viewModel::setShowMoreMenu,
                logout = sharedViewModel::logout
            )
        },
        bottomBar = {
            BottomBar(
                mode = appMode,
                enrollmentCount = enrollmentCount,
                matchCount = matchCount,
                nonmatchCount = nonmatchCount
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(sampleChatPartners) { partner ->
                ChatListItem(partner)
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigateBack: () -> Unit,
    showMoreMenu: Boolean,
    setShowMoreMenu: (Boolean) -> Unit,
    userId: Int,
    logout: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(text = "Messages", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "User ID: $userId", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        actions = {
            IconButton(onClick = { /* Handle search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            Box {
                IconButton(onClick = { setShowMoreMenu(true) }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }

                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = { setShowMoreMenu(false) },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout"
                            )
                        },
                        onClick = {
                            logout()
                            navigateBack()
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp)
                            )
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBar(
    mode: AppMode,
    enrollmentCount: Int,
    matchCount: Int,
    nonmatchCount: Int,
) {
    BottomAppBar {
        if (mode == AppMode.ENROLLMENT) {
            Text(
                text = "$mode MODE: $enrollmentCount / ${Constants.MIN_STROKE_COUNT}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarColumn(
                    text = "MATCH",
                    count = matchCount,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider()

                BottomBarColumn(
                    text = "NONMATCH",
                    count = nonmatchCount,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun BottomBarColumn(
    text: String,
    count: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = count.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun ChatListItem(partner: ChatPartner) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(partner.avatarColor, CircleShape)
        ) {
            Text(
                text = partner.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").uppercase(),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )

            // Status Dot
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.LightGray, shape = CircleShape)
                    .border(2.dp, Color.White, shape = CircleShape)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Name and Last Message
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = partner.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = partner.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )
        }
        
        // Time
        Text(
            text = partner.timestamp,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
