package com.chatter.android.model

import java.util.*

class FriendRequestAll(
    val id: Long,
    val from: UserInfoDto,
    val to: UserInfoDto,
    val sendDate: Date
)