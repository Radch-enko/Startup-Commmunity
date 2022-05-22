package com.multi.producthunt.android.screen.profile

import cafe.adriel.voyager.core.model.StateScreenModel
import com.multi.producthunt.android.screen.authorization.AuthorizationViewModel
import com.multi.producthunt.utils.KMMPreference

class ProfileScreenViewModel(private val pref: KMMPreference) :
    StateScreenModel<ProfileScreenViewModel.State>(State.Empty(pref.getString(AuthorizationViewModel.ACCESS_TOKEN))) {
    sealed class State {
        class Empty(val token: String?) : State()
    }
}