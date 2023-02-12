package network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import model.AccessTokenNetworkModel
import java.util.*

class AuthRepository(private val client: HttpClient, private val tokenHolder: AccessTokenHolder) {

    private val clientId = "dcd66f1204424e258866a0333d1f9025"
    private val clientSecret = "7cf252c5522f479285a8dbf16b5da7a8"
    suspend fun authorize() {
        val token = client.getAccessToken()
        tokenHolder.saveAccessToken(token)
    }

    private suspend fun HttpClient.getAccessToken(): String {
        val requestParams = Parameters.build {
            append("grant_type", "client_credentials")
        }

        return submitForm(
            url = "https://accounts.spotify.com/api/token",
            formParameters = requestParams,
        ) {
            header(
                HttpHeaders.Authorization,
                "Basic " + Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
            )
        }.body<AccessTokenNetworkModel>().accessToken
    }
}