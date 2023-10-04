package com.inkacast.core.data

data class Folder (
    var name: String,
    val url: String,
    var isFavorite: Boolean,
    var creationTime: Long,
    var updateTime: Long,
    var id:Long = 0,
)