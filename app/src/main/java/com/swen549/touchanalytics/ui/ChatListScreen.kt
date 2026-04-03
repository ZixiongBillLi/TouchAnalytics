package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(userId: Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column (
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "User ID: $userId", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
//                ,
//                actions = {
//                    IconButton(onClick = {}) {
//                        Icon(Icons.Default.MoreVert, contentDescription = "More")
//                    }
//                }
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

@Composable
fun ChatListItem(partner: ChatPartner) {
    Box(
        Modifier.clickable(
            enabled = true,
            onClick = {/* switch to chat screen */}
        )
    ){
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
