package model

import kotlinx.serialization.Serializable

@Serializable
data class Track(val name: String, val duration: Long, val artists: String, val count: Int)