package com.chatter.android.database

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Database(context: Context) {

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    companion object {
        const val isFirstLogin: String = "isFirstLogin"
    }

    init {
        sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun saveString(data: String, key: String) {
        editor.putString(key, data)
        editor.apply()
    }

    fun readString(key: String): String {
        return sharedPreferences.getString(key, null).toString()
    }

    inline fun <reified T> readObject(key: String): T {
        val gson = Gson()
        return gson.fromJson(readString(key), T::class.java)
    }

    fun saveObject(objects: ModelSaver, key: String) {
        val gson = Gson()
        val data: String = gson.toJson(objects)

        saveString(data, key)
    }

    fun saveBoolean(data: Boolean, key: String) {
        editor.putBoolean(key, data)
        editor.apply()
    }

    fun readBoolean(defData: Boolean, key: String): Boolean {
        return sharedPreferences.getBoolean(key, defData)
    }
}