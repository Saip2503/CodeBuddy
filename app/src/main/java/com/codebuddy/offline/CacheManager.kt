package com.codebuddy.offline

import android.content.Context
import android.content.SharedPreferences

class CacheManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("chat_cache", Context.MODE_PRIVATE)

    fun saveResponse(prompt: String, response: String) {
        prefs.edit().putString(prompt, response).apply()
    }

    fun getResponse(prompt: String): String? {
        return prefs.getString(prompt, null)
    }

    fun getCachedReply(message: String): String? {
        return prefs.getString("reply_$message", null)
    }

    fun saveReply(message: String, reply: String) {
        prefs.edit().putString("reply_$message", reply).apply()
    }
}
