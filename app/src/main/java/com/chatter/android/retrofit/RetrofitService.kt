package com.chatter.android.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitService {

    fun getUrl(): String {
        return "http://${getDatabaseAddress()}:${getDatabasePort()}"
    }

    fun getDatabaseAddress(): String {
        return "192.168.100.8"
    }

    private fun getDatabasePort(): Int {
        return 12345
    }

    fun getMessagesPort(): Int {
        return 10002
    }

    var retrofit: Retrofit

    init {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(getUrl())
            .build()
    }
}