package com.multi.producthunt.android.screen.di

import com.multi.producthunt.android.screen.addproject.AddProjectViewModel
import com.multi.producthunt.android.screen.authorization.AuthorizationViewModel
import com.multi.producthunt.android.screen.detail.DetailProjectViewModel
import com.multi.producthunt.android.screen.home.HomeScreenViewModel
import com.multi.producthunt.android.screen.profile.ProfileScreenViewModel
import com.multi.producthunt.android.screen.settings.SettingsScreenViewModel
import com.multi.producthunt.android.screen.timeline.TimelineScreenViewModel
import com.multi.producthunt.android.screen.user_projects.UserProjectsListViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider

val viewModelModule = DI.Module("viewModelModule") {
    bind<HomeScreenViewModel>() with provider { HomeScreenViewModel(instance(), instance()) }
    bind<TimelineScreenViewModel>() with provider {
        TimelineScreenViewModel(
            instance(),
            instance()
        )
    }
    bind<SettingsScreenViewModel>() with provider { SettingsScreenViewModel(instance()) }
    bind<AuthorizationViewModel>() with provider { AuthorizationViewModel(instance(), instance()) }
    bind<ProfileScreenViewModel>() with factory { params: Int ->
        ProfileScreenViewModel(
            params,
            instance(),
            instance()
        )
    }

    bind<AddProjectViewModel>() with factory { params: Int ->
        AddProjectViewModel(
            params,
            instance(),
            instance(),
            instance()
        )
    }
    bind<DetailProjectViewModel>() with factory { params: Int ->
        DetailProjectViewModel(
            params,
            instance(),
            instance(),
            instance(),
        )
    }
    bind<UserProjectsListViewModel>() with factory { params: Int ->
        UserProjectsListViewModel(
            params,
            instance(),
            instance(),
            instance()
        )
    }
}