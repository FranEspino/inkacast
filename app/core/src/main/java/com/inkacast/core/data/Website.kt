package com.inkacast.core.data

data class Website (
    var url: String,
    var name: String,
    var isFavorite: Boolean,
    var updateTime: Long,
    var id:Long = 0
)