package com.pw.passora986.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences =
            context.getSharedPreferences(
                "vault_keep",
                Context.MODE_PRIVATE
            )
    }

    fun saveString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    fun saveInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    fun saveBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

}