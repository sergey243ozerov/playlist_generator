package model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistTrackResponse(
    val items: List<TrackInfo>
){
    @Serializable
    data class TrackInfo(val track: Track)
}