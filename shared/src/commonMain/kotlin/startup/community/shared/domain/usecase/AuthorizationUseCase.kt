package startup.community.shared.domain.usecase

import startup.community.shared.utils.KMMPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AuthorizationUseCase(private val kmmPreference: KMMPreference) {

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val CURRENT_USER_ID = "CURRENT_USER_ID"
    }

    val scope = CoroutineScope(Dispatchers.Main)

    fun isAuthorized(): Boolean {
        return !kmmPreference.getString(ACCESS_TOKEN).isNullOrEmpty()
    }

    fun logout() {
        kmmPreference.put(ACCESS_TOKEN, "")
    }

    fun saveCurrentUserId(id: Int) {
        kmmPreference.put(CURRENT_USER_ID, id)
    }

    fun deleteCurrentUserId() {
        kmmPreference.put(CURRENT_USER_ID, 0)
    }

    fun getCurrentUserId(): Int? {
        kmmPreference.getInt(CURRENT_USER_ID, 0).let {
            if (it == 0) return null
            else return it
        }
    }
}