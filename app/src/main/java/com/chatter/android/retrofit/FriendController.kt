package com.chatter.android.retrofit

import com.chatter.android.model.FriendUserInfo
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendController {

    @POST("/friend/getAllByEmail")
    fun getAllByEmail(@Query("email") email: String): Call<List<FriendUserInfo>>
}