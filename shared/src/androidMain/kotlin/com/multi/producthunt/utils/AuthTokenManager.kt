package com.multi.producthunt.utils

import android.content.Context

actual class AuthTokenManager actual constructor() : TokenManager {

    private var context: Context? = null

    companion object {
        const val prefs = "AUTHORIZATOIN_PREFS"
        const val token = "TOKEN"
    }

    private val sharedPreferences = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
    private val editor = sharedPreferences?.edit()

    actual override fun saveToken(value: String) {
        editor?.putString(token, value)
        editor?.commit()
    }

    actual override fun getToken(): String? {
        return sharedPreferences?.getString(token, null)
    }
}