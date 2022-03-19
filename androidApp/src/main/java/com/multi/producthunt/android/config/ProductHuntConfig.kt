package com.multi.producthunt.android.config

import com.multi.producthunt.android.BuildConfig
import com.multi.producthunt.utils.AppConfig

class ProductHuntConfig : AppConfig {
    override val baseUrl: String
        get() = BuildConfig.BASE_URL
    override val accessToken: String
        get() = BuildConfig.APP_ACCESS_TOKEN
}