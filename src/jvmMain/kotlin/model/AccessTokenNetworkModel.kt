package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenNetworkModel(
    @SerialName("access_token")
    val accessToken: String
)