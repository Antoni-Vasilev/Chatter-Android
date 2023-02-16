package com.chatter.android.model

import java.util.Date

data class FriendRequestRegisterOutDto(
    val id: Long,
    val from: UserInfoDto,
    val to: UserInfoDto,
    val sendDate: Date
)