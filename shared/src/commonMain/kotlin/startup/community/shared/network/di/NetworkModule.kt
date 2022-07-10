package startup.community.shared.network.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import startup.community.shared.network.client.KtorfitClient
import startup.community.shared.network.service.ProjectsApiService

val networkModule = DI.Module("Network Module", false, "network", fun DI.Builder.() {
    bind<ProjectsApiService>() with factory { KtorfitClient(instance()).getService() }
})