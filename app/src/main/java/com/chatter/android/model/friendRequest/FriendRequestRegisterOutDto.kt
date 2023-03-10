package com.chatter.android.model.friendRequest

import com.chatter.android.model.user.UserInfoDto
import java.util.Date

data class FriendRequestRegisterOutDto(
    val id: Long,
    val from: UserInfoDto,
    val to: UserInfoDto,
    val sendDate: Date
)