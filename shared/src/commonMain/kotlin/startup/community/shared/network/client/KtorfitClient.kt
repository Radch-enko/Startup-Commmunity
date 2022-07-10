package startup.community.shared.network.client

import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.utils.KMMPreference

expect class KtorfitClient(kmmPreference: KMMPreference){
    fun getService(): ProjectsApiService
}