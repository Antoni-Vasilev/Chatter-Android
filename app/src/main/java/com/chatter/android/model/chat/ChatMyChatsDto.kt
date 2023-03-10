package com.chatter.android.model.chat

import com.chatter.android.model.message.Message
import com.chatter.android.model.user.UserInfoDto

class ChatMyChatsDto(
    val id: Long,
    val userInfo: UserInfoDto,
    val lastMessage: Message?
)