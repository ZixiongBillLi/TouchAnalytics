package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swen549.touchanalytics.data.ChatPartner
import com.swen549.touchanalytics.data.Message
import com.swen549.touchanalytics.data.User
import com.swen549.touchanalytics.ui.components.BottomBar

@Composable
fun ChatScreen(
    userId: Long,
    partnerId: Long,
    navigateBack: () -> Unit,
    sharedViewModel: TouchAnalyticsViewModel,
    chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModel.Factory
    )
) {
    LaunchedEffect(partnerId) {
        chatViewModel.startListening(userId, partnerId)
    }

    val appMode by sharedViewModel.mode.collectAsStateWithLifecycle()
    val enrollmentCount by sharedViewModel.enrollmentCount.collectAsStateWithLifecycle()
    val matchCount by sharedViewModel.matchCount.collectAsStateWithLifecycle()
    val nonmatchCount by sharedViewModel.nonmatchCount.collectAsStateWithLifecycle()

    val showMoreMenu by chatViewModel.showMoreMenu.collectAsStateWithLifecycle()
    val partner = chatViewModel.partner.collectAsStateWithLifecycle()
    val messages = chatViewModel.messages.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ChatTopBar(
                userId = userId,
                navigateBack = navigateBack,
                showMoreMenu = showMoreMenu,
                setShowMoreMenu = chatViewModel::setShowMoreMenu,
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
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
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
                items(messages.value) { message ->
                    MessageItem(
                        partner = partner.value!!,
                        avatarColor = partner.value!!.avatarColor,
                        message = message
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    partner: ChatPartner,
    avatarColor: Color,
    message: Message
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message.timestampString,
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
            ) {
                Text(
                    text = partner.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString("")
                        .uppercase(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    navigateBack: () -> Unit,
    showMoreMenu: Boolean,
    setShowMoreMenu: (Boolean) -> Unit,
    userId: Long,
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Messages", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "User ID: $userId", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
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

                }
            }
        }
    )
}