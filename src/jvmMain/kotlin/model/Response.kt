package model

@kotlinx.serialization.Serializable
data class Response(
    val result: List<Track>
)