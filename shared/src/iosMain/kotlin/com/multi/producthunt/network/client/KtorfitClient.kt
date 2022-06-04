package com.multi.producthunt.network.client

import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.utils.KMMPreference

actual class KtorfitClient actual constructor(kmmPreference: KMMPreference) {
    actual fun getService(): ProjectsApiService {
        TODO("Not yet implemented")
    }
}