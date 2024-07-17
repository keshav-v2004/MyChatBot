package com.example.mychatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier,
) {

    val responseState by viewModel.responseState.collectAsState()

    Scaffold(
        topBar = { AppHeader() },

        ) { paddingValue ->

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                if (responseState.isNotEmpty()) {
                    ChatList(
                        messageList = responseState,
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ai_stars_icon_artificial_intelligence_600nw_2351532151),
                            contentDescription = null,
                            modifier = modifier
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            }


            InputArea(
                SendQuery = {
                    viewModel.SendMessage(query = it)
                }
            )
        }
    }
}

@Composable
fun ChatList(
    messageList: List<MessageModel>,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            reverseLayout = true
        ) {
            items(messageList.reversed()) {
                EachMessage(
                    messageModel = it
                )
            }
        }
    }
}

@Composable
fun EachMessage(
    messageModel: MessageModel,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =

        if (messageModel.role == "user") {
            Arrangement.End
        } else Arrangement.Start,

        modifier = Modifier
            .padding(5.dp)
//            .fillMaxWidth()
    ) {
        if (messageModel.role == "user") {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 15.dp,
                            topStart = 15.dp,
                            bottomEnd = 0.dp,
                            topEnd = 15.dp
                        )
                    )
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.query,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W300,
                        modifier = modifier
                            .background(Color.Green)
                    )
                }
            }

        } else {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 0.dp,
                            topStart = 15.dp,
                            bottomEnd = 15.dp,
                            topEnd = 15.dp
                        )
                    )
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.query,
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W300,
                        modifier = Modifier
                            .background(Color.Gray)
                    )
                }

            }
        }
    }
}

@Composable
fun InputArea(
    SendQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var message by remember { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = {
                    message = it
                },
                label = {
                    Text(text = "Ask me anything..")
                },
                modifier = modifier
                    .weight(1f)
            )

            IconButton(
                onClick = {
                    if (message != "") {
                        SendQuery(message)
                        message = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .statusBarsPadding()
            .padding(start = 1.dp, end = 1.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.DarkGray)
    ) {
        Text(
            text = "ChatBot",
            fontSize = 45.sp,
            fontWeight = FontWeight.W500,
            color = Color.White,
        )
        Text(
            text = " (Powered by Gemini) ",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )
    }
}