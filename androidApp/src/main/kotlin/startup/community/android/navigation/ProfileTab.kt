package startup.community.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import startup.community.shared.MR
import startup.community.android.screen.authorization.AuthenticationScreen
import startup.community.android.screen.profile.ProfileScreen
import startup.community.android.screen.user_projects.UserProjectsListScreen
import org.kodein.di.compose.rememberInstance
import startup.community.shared.domain.usecase.AuthorizationUseCase

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
        val authorizationUseCase: AuthorizationUseCase by rememberInstance()
        val isAuthorized = authorizationUseCase.isAuthorized()
        val tabNavigator = LocalTabNavigator.current

        if (isAuthorized) {
            ProfileScreen(
                onLogout = { tabNavigator.current = HomeTab },
                onShowProjects = { id, navigator ->
                    navigator?.parent?.push(UserProjectsListScreen(id))
                }).Content()
        } else {
            AuthenticationScreen(onSuccessAuthenticate = {
                tabNavigator.current = HomeTab
            }).Content()
        }
    }
}