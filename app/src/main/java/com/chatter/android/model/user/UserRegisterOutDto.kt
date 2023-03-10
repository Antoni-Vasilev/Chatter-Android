package com.chatter.android.model.user

import java.util.*

data class UserRegisterOutDto(
    val fullName: String,
    val displayName: String,
    val phone: String,
    val country: String,
    val birthdayDate: Date,
    val email: String
)