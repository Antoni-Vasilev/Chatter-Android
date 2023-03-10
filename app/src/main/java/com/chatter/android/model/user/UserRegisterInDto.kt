package com.chatter.android.model.user

import java.util.*

data class UserRegisterInDto(
    var fullName: String,
    var displayName: String,
    var phone: String,
    var country: String,
    var birthdayDate: Date,
    var email: String,
    var password: String
)