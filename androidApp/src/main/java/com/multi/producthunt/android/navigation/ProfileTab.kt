package com.multi.producthunt.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.multi.producthunt.MR
import com.multi.producthunt.android.screen.authorization.AuthenticationScreen
import com.multi.producthunt.android.screen.profile.ProfileScreen
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.utils.KMMPreference
import io.github.aakira.napier.Napier
import org.kodein.di.compose.instance
import org.kodein.di.compose.rememberInstance

object ProfileTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(id = MR.strings.profile_tab_title.resourceId)
            val icon = rememberVectorPainter(Icons.Default.Person)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val pref: KMMPreference by rememberInstance()
        val isAuthorized = !pref.getString(AuthorizationUseCase.ACCESS_TOKEN).isNullOrEmpty()
        Napier.e("ProfileTab isAuthorized = $isAuthorized")
        val launchScreen = if (isAuthorized) {
            ProfileScreen()
        } else {
            AuthenticationScreen(onSuccessAuthenticate = {
                it?.replace(ProfileScreen())
            })
        }
        Navigator(screen = launchScreen)
    }
}