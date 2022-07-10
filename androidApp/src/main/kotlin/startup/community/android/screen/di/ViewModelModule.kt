package startup.community.android.screen.di

import org.kodein.di.*
import startup.community.android.screen.addproject.AddProjectViewModel
import startup.community.android.screen.authorization.AuthorizationViewModel
import startup.community.android.screen.create_discussion.AddDiscussionViewModel
import startup.community.android.screen.detail.DetailProjectViewModel
import startup.community.android.screen.detail_discussion.DetailDiscussionViewModel
import startup.community.android.screen.discussions.DiscussionListViewModel
import startup.community.android.screen.home.HomeScreenViewModel
import startup.community.android.screen.profile.ProfileScreenViewModel
import startup.community.android.screen.settings.SettingsScreenViewModel
import startup.community.android.screen.timeline.TimelineScreenViewModel
import startup.community.android.screen.topic_projects.TopicProjectsListViewModel
import startup.community.android.screen.topics.TopicsScreenViewModel
import startup.community.android.screen.user_projects.UserProjectsListViewModel

val viewModelModule = DI.Module("viewModelModule") {
    bind<HomeScreenViewModel>() with provider {
        HomeScreenViewModel(
            instance(),
            instance(),
            instance()
        )
    }
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

    bind<TopicsScreenViewModel>() with provider { TopicsScreenViewModel(instance()) }

    bind<TopicProjectsListViewModel>() with factory { params: List<Any> ->
        TopicProjectsListViewModel(
            params[0] as Int,
            params[1] as String,
            instance(),
            instance()
        )
    }

    bind<DiscussionListViewModel>() with provider {
        DiscussionListViewModel(
            instance(),
            instance()
        )
    }

    bind<AddDiscussionViewModel>() with provider {
        AddDiscussionViewModel(
            instance(),
            instance(),
            instance()
        )
    }
    bind<DetailDiscussionViewModel>() with factory { params: Int ->
        DetailDiscussionViewModel(
            params,
            instance(),
            instance(),
            instance(),
        )
    }
}