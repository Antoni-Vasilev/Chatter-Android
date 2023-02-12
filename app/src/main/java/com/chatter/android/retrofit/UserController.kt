package com.chatter.android.retrofit

import com.chatter.android.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserController {

    @POST("/user/register")
    fun register(@Body userRegisterInDto: UserRegisterInDto): Call<UserRegisterOutDto>

    @POST("/user/uploadProfileImage")
    fun uploadProfileImage(@Body data: Image): Call<Boolean>

    @POST("/user/login")
    fun login(@Body userLoginInDto: UserLoginInDto): Call<UserLoginOutDto>
}