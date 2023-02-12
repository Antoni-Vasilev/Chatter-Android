package com.chatter.android.model

import android.content.Intent
import android.widget.ImageView
import java.util.*

class UserRegisterView {

    var fullName: String = ""
    var displayName: String = ""
    var phone: String = ""
    var country: String = ""
    var birthdayDate: Date? = null
    var email: String = ""
    var password: String = ""
    var image: Intent? = null
    var imagePath: Boolean = false
    var imageData: ByteArray = ByteArray(1)
}
