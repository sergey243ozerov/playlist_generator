package model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistPaging(
    val playlists: PagingContent
){
    @Serializable
    data class PagingContent(val items: List<PlaylistId>)
}