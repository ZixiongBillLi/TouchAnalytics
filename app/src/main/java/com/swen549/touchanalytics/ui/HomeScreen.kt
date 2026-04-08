package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swen549.touchanalytics.data.ChatPartner
import com.swen549.touchanalytics.ui.components.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: Long,
    onPartnerClick: (Long) -> Unit,
    navigateBack: () -> Unit,
    sharedViewModel: TouchAnalyticsViewModel,
    homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    ),
) {
    LaunchedEffect(userId) {
        homeViewModel.startListening(userId)
    }

    val appMode by sharedViewModel.mode.collectAsStateWithLifecycle()
    val enrollmentCount by sharedViewModel.enrollmentCount.collectAsStateWithLifecycle()
    val matchCount by sharedViewModel.matchCount.collectAsStateWithLifecycle()
    val nonmatchCount by sharedViewModel.nonmatchCount.collectAsStateWithLifecycle()

    val showMoreMenu by homeViewModel.showMoreMenu.collectAsStateWithLifecycle()
    val searchActive by homeViewModel.searchActive.collectAsStateWithLifecycle()
    val chatPartners by homeViewModel.chatPartners.collectAsStateWithLifecycle()
    val query by homeViewModel.query.collectAsStateWithLifecycle()

    if (searchActive) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = homeViewModel::setQuery,
                    onSearch = { homeViewModel.setSearchActive(false) },
                    expanded = true,
                    onExpandedChange = homeViewModel::setSearchActive,
                    placeholder = { Text("Search...") },
                    leadingIcon = {
                        IconButton(onClick = {
                            homeViewModel.setSearchActive(false)
                            homeViewModel.setQuery("")
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = SearchBarDefaults.inputFieldColors()
                )
            },
            expanded = true,
            onExpandedChange = homeViewModel::setSearchActive,
            modifier = Modifier.fillMaxWidth(),
            windowInsets = SearchBarDefaults.windowInsets,
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                items(chatPartners) { partner ->
                    if (partner.name.contains(query, ignoreCase = true)) {
                        ChatListItem(
                            partner = partner,
                            onClick = {
                                onPartnerClick(partner.id)
                                homeViewModel.setSearchActive(false)
                                homeViewModel.setQuery("")
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 88.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                HomeTopBar(
                    userId = userId,
                    navigateBack = navigateBack,
                    showMoreMenu = showMoreMenu,
                    setShowMoreMenu = homeViewModel::setShowMoreMenu,
                    setSearchActive = homeViewModel::setSearchActive,
                    logout = sharedViewModel::logout
                )

                HorizontalDivider(
                    thickness = 0.8.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = 0.8.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                BottomBar(
                    mode = appMode,
                    enrollmentCount = enrollmentCount,
                    matchCount = matchCount,
                    nonmatchCount = nonmatchCount
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(chatPartners) { partner ->
                ChatListItem(
                    partner = partner,
                    onClick = {
                        onPartnerClick(partner.id)
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    navigateBack: () -> Unit,
    showMoreMenu: Boolean,
    setShowMoreMenu: (Boolean) -> Unit,
    setSearchActive: (Boolean) -> Unit,
    userId: Long,
    logout: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Messages", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "User ID: $userId", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        actions = {
            IconButton(onClick = { setSearchActive(true) }) {
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
                        .background(MaterialTheme.colorScheme.surfaceContainer)
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
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun ChatListItem(
    partner: ChatPartner,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
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
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Status Dot
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.LightGray, shape = CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.surface, shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Name and Last Message
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = partner.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = partner.lastMessageTimestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = partner.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
