package com.multi.producthunt.android.screen.authorization

data class AuthenticationState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val name: String,
    val username: String,
    val headline: String? = null,
    val coverImage: String? = null,
    val profileImage: String? = null,
    val password: String,
    val passwordAgain: String,
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

    fun isRegistrationFormValid(): Boolean {
        return password.isNotEmpty() && username.isNotEmpty() && name.isNotEmpty() && password == passwordAgain && (authenticationMode == AuthenticationMode.SIGN_UP
                || passwordRequirements.containsAll(
            PasswordRequirements.values().toList()
        ))
    }
}