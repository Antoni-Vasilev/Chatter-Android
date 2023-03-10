package com.chatter.android.retrofit

import com.chatter.android.model.*
import com.chatter.android.model.user.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserController {

    @POST("/user/register")
    fun register(@Body userRegisterInDto: UserRegisterInDto): Call<UserRegisterOutDto>

    @POST("/user/uploadProfileImage")
    fun uploadProfileImage(@Body data: Image): Call<Boolean>

    @POST("/user/login")
    fun login(@Body userLoginInDto: UserLoginInDto): Call<UserLoginOutDto>

    @GET("/user/allUser")
    fun allUser(
        @Query("search") search: String,
        @Query("email") email: String
    ): Call<List<UserInfoDto>>

    @POST("/user/lastOpen")
    fun lastOpen(
        @Query("email") email: String
    ): Call<Boolean>
}