package com.multi.producthunt.domain.usecase

import com.multi.producthunt.utils.KMMPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AuthorizationUseCase(private val kmmPreference: KMMPreference) {

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }

    val scope = CoroutineScope(Dispatchers.Main)

    fun isAuthorized(): Boolean {
        return !kmmPreference.getString(ACCESS_TOKEN).isNullOrEmpty()
    }

    fun logout() {
        kmmPreference.put(ACCESS_TOKEN, "")
    }
}