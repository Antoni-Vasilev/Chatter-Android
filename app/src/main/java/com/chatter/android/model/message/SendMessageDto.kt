package com.chatter.android.model.message

data class SendMessageDto(
    val message: String,
    val senderEmail: String,
    val fileName: ByteArray,
    val type: MessageType
)
