package com.inkamedia.inkacast.presentation.screen_browser.videos_extract.adapter

data class Video(
    val id: Long,
    val title: String,
    val uri: String,
    val mimeType:String,
    val referer :String,
    val type: String
)