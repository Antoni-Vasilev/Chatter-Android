package com.chatter.android.model.user

import java.util.*

data class UserInfoDto(
    val id: Long,
    val fullName: String,
    val displayName: String,
    val displayNameCode: String,
    val email: String,
    val lastOpen: Date
)