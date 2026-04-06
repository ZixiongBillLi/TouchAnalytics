package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: TouchViewModel = viewModel(),
    onPartnerClick: (Int) -> Unit
) {
    // To create an infinite-like looping list
    val infiniteCount = Int.MAX_VALUE
    val startIndex = (infiniteCount / 2) - (infiniteCount / 2 % sampleChatPartners.size)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "User ID: ${viewModel.userID ?: "Unknown"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                count = infiniteCount,
                itemContent = { index ->
                    val partner = sampleChatPartners[index % sampleChatPartners.size]
                    ChatListItem(partner, onClick = { onPartnerClick(partner.id) })
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }
            )
        }
    }
}

@Composable
fun ChatListItem(partner: ChatPartner, onClick: () -> Unit) {
    Box(
        Modifier.clickable(onClick = onClick)
    ) {
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
            )

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
}
