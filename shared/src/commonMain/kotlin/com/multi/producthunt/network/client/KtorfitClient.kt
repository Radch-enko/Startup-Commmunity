package com.multi.producthunt.network.client

import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.network.service.ResultConverter
import com.multi.producthunt.utils.KMMPreference
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

expect class KtorfitClient{
    fun getService(): ProjectsApiService
}