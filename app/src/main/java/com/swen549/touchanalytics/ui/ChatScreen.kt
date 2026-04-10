package com.swen549.touchanalytics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swen549.touchanalytics.data.ChatPartner
import com.swen549.touchanalytics.ui.components.BottomBar
import com.swen549.touchanalytics.ui.components.MessageItem

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
    val partner by chatViewModel.partner.collectAsStateWithLifecycle()
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val input by chatViewModel.input.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column {
                ChatTopBar(
                    partner = partner,
                    navigateBack = navigateBack,
                    showMoreMenu = showMoreMenu,
                    setShowMoreMenu = chatViewModel::setShowMoreMenu,
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
        }
    ) { innerPadding ->
        if (partner == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    reverseLayout = false // standard chat layout or reversed? usually standard for this app
                ) {
                    item {
                        Text(
                            text = "Most recent messages",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(messages) { message ->
                        MessageItem(
                            partner = partner!!,
                            avatarColor = partner!!.avatarColor,
                            message = message
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(500.dp))
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MessageInput(
                        input = input,
                        onValueChange = chatViewModel::setInput
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(
    input: String = "",
    onValueChange: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = onValueChange,
            placeholder = { Text("Send a message...") },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = androidx.compose.ui.text.input.ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    // Handle send message
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = { /* Handle send*/ }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    partner: ChatPartner?,
    navigateBack: () -> Unit,
    showMoreMenu: Boolean,
    setShowMoreMenu: (Boolean) -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = partner?.name ?: "Messages", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {

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
