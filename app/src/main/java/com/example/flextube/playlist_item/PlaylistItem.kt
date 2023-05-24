package com.example.flextube.playlist_item

data class PlaylistItem(
    val id: String,
    val title: String,
    val channelTitle: String,
    val thumbnailsUrl: String,
    val videoOwnerChannelId: String,
    val videoId: String
)
