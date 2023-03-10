package com.chatter.android.model.user

import android.content.Intent
import java.util.*

data class UserRegisterView(
    var fullName: String,
    var displayName: String,
    var phone: String,
    var country: String,
    var birthdayDate: Date,
    var email: String,
    var password: String,
    var image: Intent,
    var imagePath: Boolean = false,
    var imageData: ByteArray
)
