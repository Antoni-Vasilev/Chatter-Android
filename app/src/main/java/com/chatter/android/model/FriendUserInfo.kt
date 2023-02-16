package com.chatter.android.model

import java.util.*

data class FriendUserInfo(
    val id: Long,
    val firstUser: UserInfoDto,
    val secondUser: UserInfoDto,
    val startDate: Date
)