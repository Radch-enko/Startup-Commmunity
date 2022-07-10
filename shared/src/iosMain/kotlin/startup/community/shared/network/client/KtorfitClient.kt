package startup.community.shared.network.client

import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.utils.KMMPreference

actual class KtorfitClient actual constructor(kmmPreference: KMMPreference) {
    actual fun getService(): ProjectsApiService {
        TODO("Not yet implemented")
    }
}