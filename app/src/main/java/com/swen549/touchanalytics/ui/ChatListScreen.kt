package com.swen549.touchanalytics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swen549.touchanalytics.ui.components.BottomBar
import com.swen549.touchanalytics.ui.components.ChatListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    userId: Long,
    onPartnerClick: (Long) -> Unit,
    navigateBack: () -> Unit,
    viewModel: TouchAnalyticsViewModel,
    homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    ),
) {
    LaunchedEffect(userId) {
        homeViewModel.startListening(userId)
    }

    val appMode by viewModel.mode.collectAsStateWithLifecycle()
    val enrollmentCount by viewModel.enrollmentCount.collectAsStateWithLifecycle()
    val matchCount by viewModel.matchCount.collectAsStateWithLifecycle()
    val nonmatchCount by viewModel.nonmatchCount.collectAsStateWithLifecycle()

    val showMoreMenu by homeViewModel.showMoreMenu.collectAsStateWithLifecycle()
    val chatPartners by homeViewModel.chatPartners.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Messages", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "User ID: $userId", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Search logic if needed */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        Box {
                            IconButton(onClick = { homeViewModel.setShowMoreMenu(true) }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More")
                            }
                            DropdownMenu(
                                expanded = showMoreMenu,
                                onDismissRequest = { homeViewModel.setShowMoreMenu(false) }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                                    onClick = {
                                        viewModel.logout()
                                        navigateBack()
                                    }
                                )
                            }
                        }
                    }
                )
                HorizontalDivider(thickness = 0.8.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
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
                .padding(innerPadding),
            // Ensure it's always scrollable for touch analytics
            userScrollEnabled = true,
            verticalArrangement = Arrangement.Top
        ) {
            // To ensure scrolling even with few items, we can add a spacer or dummy items
            // But LazyColumn with enough content works. To force scrollability for analytics:
            item { Spacer(modifier = Modifier.height(1.dp)) }
            
            items(chatPartners) { partner ->
                ChatListItem(
                    partner = partner,
                    onClick = { onPartnerClick(partner.id) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }

            item { Spacer(modifier = Modifier.height(500.dp)) }
        }
    }
}
