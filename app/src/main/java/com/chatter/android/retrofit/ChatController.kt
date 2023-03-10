package com.chatter.android.retrofit

import com.chatter.android.model.chat.ChatMyChatsDto
import com.chatter.android.model.chat.ChatRegisterInDto
import com.chatter.android.model.chat.ChatRegisterOutDto
import com.chatter.android.model.message.MessageInfo
import com.chatter.android.model.message.SendMessageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatController {

    @GET("/chat/myChats")
    fun myChats(
        @Query("email") email: String,
        @Query("search") search: String
    ): Call<List<ChatMyChatsDto>>

    @POST("/chat/registerChat")
    fun registerChat(
        @Body chatRegisterInDto: ChatRegisterInDto,
        @Query("email") email: String
    ): Call<ChatRegisterOutDto>

    @GET("/chat/checkChat")
    fun checkChat(
        @Query("firstUserEmail") firstUserEmail: String,
        @Query("secondUserEmail") secondUserEmail: String
    ): Call<Boolean>

    @POST("/chat/sendMessage")
    fun sendMessage(
        @Body sendMessageDto: SendMessageDto,
        @Query("chatId") chatId: Long
    ): Call<Boolean>

    @GET("/chat/chatById")
    fun getChatById(
        @Query("chatId") id: Long,
        @Query("email") email: String
    ): Call<ChatMyChatsDto>

    @GET("/chat/message/new")
    fun getAllNewMessages(
        @Query("chatId") id: Long,
        @Query("lastMessage") lastMessageId: Long
    ): Call<List<MessageInfo>>

    @GET("/chat/messages")
    fun getAllMessagesFromChat(
        @Query("chatId") id: Long
    ): Call<List<MessageInfo>>
}