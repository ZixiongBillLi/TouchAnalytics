package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    partnerId: Int,
    onBack: () -> Unit,
    viewModel: TouchViewModel = viewModel()
) {
    val partner = sampleChatPartners.find { it.id == partnerId } ?: return
    val modeText = if (viewModel.mode == AppMode.ENROLLMENT) "Enrollment Mode" else "Verification Mode"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = partner.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = modeText,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleMode() }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Toggle Mode")
                    }
                }
            )
        },
        bottomBar = {
            ChatBottomBar(viewModel)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE9F0F4))
        ) {
            Text(
                text = "Most recent messages",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 12.sp,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(partner.messages) { message ->
                    MessageItem(message, partner.avatarColor)
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, avatarColor: Color) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message.timestamp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(avatarColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .shadow(1.dp, RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ChatBottomBar(viewModel: TouchViewModel) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (viewModel.mode == AppMode.ENROLLMENT) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "User ID: ${viewModel.userID}", fontWeight = FontWeight.SemiBold)
                }
                VerticalDivider(modifier = Modifier.height(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Registered Swipes: ${viewModel.enrollmentCount} / ${viewModel.maxEnrollment}",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Match", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "${viewModel.matchCount}",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                VerticalDivider(modifier = Modifier.height(40.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Non Match", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "${viewModel.mismatchCount}",
                        color = Color(0xFFF44336),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
