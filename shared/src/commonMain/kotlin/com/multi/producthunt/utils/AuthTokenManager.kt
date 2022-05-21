package com.multi.producthunt.utils

expect class AuthTokenManager constructor(): TokenManager {
    override fun getToken(): String?
    override fun saveToken(value: String)
}