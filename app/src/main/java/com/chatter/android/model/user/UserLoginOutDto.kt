package com.chatter.android.model.user

import com.chatter.android.database.ModelSaver
import java.util.*

data class UserLoginOutDto(
    val fullName: String,
    val displayName: String,
    val displayNameCode: String,
    val phone: String,
    val birthdayDate: Date,
    val country: String,
    val email: String
) : ModelSaver