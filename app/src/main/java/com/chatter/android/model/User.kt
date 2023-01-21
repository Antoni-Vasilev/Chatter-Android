package com.chatter.android.model

import java.util.*

data class User(
    private val id: Long,
    private val fullName: String,
    private val displayName: String,
    private val displayNameCode: String,
    private val phone: String,
    private val country: String,
    private val birthdayDate: Date,
    private val email: String,
    private val password: String
)
