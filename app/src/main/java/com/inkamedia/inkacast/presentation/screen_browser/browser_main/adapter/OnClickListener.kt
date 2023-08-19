package com.inkamedia.inkacast.presentation.screen_browser.browser_main.adapter

import com.fraporitmos.gdrivevideoplayer.repository.Webpage

interface OnClickListener {
    fun onClickebpage(web: Webpage){
    }
    fun onDeletepage(web: Webpage) {}

}