package com.multi.producthunt.android.screen.authorization

data class AuthenticationState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val username: String,
    val password: String,
    val passwordRequirements: List<PasswordRequirements> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return password.isNotEmpty() && username.isNotEmpty() && (authenticationMode == AuthenticationMode.SIGN_IN
                || passwordRequirements.containsAll(
            PasswordRequirements.values().toList()
        ))
    }
}