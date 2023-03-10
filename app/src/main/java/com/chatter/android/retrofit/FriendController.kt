package com.chatter.android.retrofit

import com.chatter.android.model.friend.FriendUserInfo
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendController {

    @POST("/friend/getAllByEmail")
    fun getAllByEmail(
        @Query("email") email: String,
        @Query("search") search: String
    ): Call<List<FriendUserInfo>>

    @DELETE("/friend/delete")
    fun delete(
        @Query("firstUserEmail") firstUserEmail: String,
        @Query("secondUserEmail") secondUserEmail: String
    ): Call<Boolean>
}