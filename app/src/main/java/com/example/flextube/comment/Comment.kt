package com.example.flextube.comment

class Comment
    (
    val id: String,
    val text: String,
    val author: String,
    val authorLogo: String,
    val likeCount: String,
    var publishedAt: String
            ) {

    init {
        convertPublishedAt()
    }
    private fun convertPublishedAt(){
        publishedAt = publishedAt.subSequence(0,publishedAt.indexOf('T')).toString()
    }
}