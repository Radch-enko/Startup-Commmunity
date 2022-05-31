package com.multi.producthunt.domain.usecase

import com.multi.producthunt.utils.KMMPreference
import io.github.aakira.napier.Napier

class AuthorizationUseCase(private val kmmPreference: KMMPreference) {

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }

    fun isAuthorized(): Boolean {
        val token = kmmPreference.getString(ACCESS_TOKEN)
        Napier.e("AuthorizationUseCase_token = $token")
        val result = !token.isNullOrEmpty()
        Napier.e("isAuthorized = $result")
        return result
    }

    fun logout() {
        kmmPreference.put(ACCESS_TOKEN, "")
    }
}