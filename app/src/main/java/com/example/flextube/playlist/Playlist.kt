package com.example.flextube.playlist

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val itemCount: Int
)
