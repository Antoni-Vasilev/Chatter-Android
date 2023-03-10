package com.chatter.android.model.chat

import com.chatter.android.model.message.MessageInfo
import com.chatter.android.model.user.UserInfoDto

data class ChatRegisterOutDto(
    val id: Long,
    val userInfo: UserInfoDto,
    val lastMessage: MessageInfo
)