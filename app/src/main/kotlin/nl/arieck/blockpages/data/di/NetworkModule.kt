package nl.arieck.blockpages.data.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import nl.arieck.blockpages.BuildConfig
import org.koin.dsl.module

val networkModule = module {

    val baseUrl = BuildConfig.API_URL_PREFIX + BuildConfig.BASE_URL

    factory {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            defaultRequest {
                url(baseUrl)
            }
            install(Resources)
            install(ContentNegotiation){
                json(Json { ignoreUnknownKeys = true })
            }
            expectSuccess = true
        }
    }

}
