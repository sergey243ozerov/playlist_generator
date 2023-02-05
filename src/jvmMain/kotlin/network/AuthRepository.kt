package network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import model.AccessTokenNetworkModel
import java.util.*

class AuthRepository(private val client: HttpClient, private val tokenHolder: AccessTokenHolder) {

    private val clientId = "444926c86f594f46b7e05f643f89d7e7"
    private val clientSecret = "7f9d69b034994fa28111fe2b74292d36"
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
                "Basic" + Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
            )
        }.body<AccessTokenNetworkModel>().accessToken
    }
}