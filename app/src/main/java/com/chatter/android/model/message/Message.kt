package com.chatter.android.model.message

import com.chatter.android.model.user.UserInfoDto
import java.util.*

data class Message(
    val id: Long,
    val message: String,
    val sender: UserInfoDto,
    val fileName: String,
    val type: MessageType,
    val sendDate: Date,
    val likes: List<UserInfoDto>
)