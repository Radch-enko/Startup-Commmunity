package com.multi.producthunt.utils

interface TokenManager {
    fun saveToken(value: String)
    fun getToken(): String?
}