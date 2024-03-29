package com.chatter.android.retrofit

import com.chatter.android.model.friendRequest.FriendRequestAll
import com.chatter.android.model.friendRequest.FriendRequestRegisterInDto
import com.chatter.android.model.friendRequest.FriendRequestRegisterOutDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendRequestController {

    @POST("/friendRequest/registerRequest")
    fun registerRequest(@Body friendRequestRegisterInDto: FriendRequestRegisterInDto): Call<FriendRequestRegisterOutDto>

    @POST("/friendRequest/checkRequest")
    fun checkRequest(@Body friendRequestRegisterInDto: FriendRequestRegisterInDto): Call<Boolean>

    @POST("/friendRequest/acceptRequest")
    fun acceptRequest(@Body friendRequestRegisterInDto: FriendRequestRegisterInDto): Call<Boolean>

    @POST("/friendRequest/rejectRequest")
    fun rejectRequest(
        @Query("requestId") requestId: Long
    ): Call<Boolean>

    @GET("/friendRequest/all")
    fun all(
        @Query("search") search: String,
        @Query("email") email: String
    ): Call<List<FriendRequestAll>>
}