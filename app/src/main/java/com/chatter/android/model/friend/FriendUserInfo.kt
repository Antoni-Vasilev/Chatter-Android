package com.chatter.android.model.friend

import com.chatter.android.model.user.UserInfoDto
import java.util.*

data class FriendUserInfo(
    val id: Long,
    val firstUser: UserInfoDto,
    val secondUser: UserInfoDto,
    val startDate: Date
)