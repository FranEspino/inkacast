package com.inkacast.core.data

data class Video(
    var folderId: String,
    var title: String,
    var url: String,
    var currentMilliseconds: Long,
    var isFavorite: Boolean,
    var creationTime: Long,
    var id:Long = 0
)
