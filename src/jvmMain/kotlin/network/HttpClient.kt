package network

import di.ApplicationScope
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


private val client by lazy {
    HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
}

private val authClient by lazy {
    HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val token = ApplicationScope.tokenHolder.getAccessToken()
                    BearerTokens(token, token)
                }
                sendWithoutRequest { request ->
                    request.url.host == "accounts.spotify.com"
                }
            }
        }
        defaultRequest {
            url("https://api.spotify.com/v1/")
        }
    }
}

fun provideAuthClient(): HttpClient {
    return authClient
}

fun provideNoAuthClient(): HttpClient{
    return client
}

