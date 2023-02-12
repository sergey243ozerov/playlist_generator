package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    @SerialName("duration_ms")
    val durationMS: Long,
    val explicit: Boolean,
    val name: String,
    val artists: List<Artist>,
)