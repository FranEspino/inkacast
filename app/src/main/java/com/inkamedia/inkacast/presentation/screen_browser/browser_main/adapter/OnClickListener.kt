package com.inkamedia.inkacast.presentation.screen_browser.browser_main.adapter

import com.inkacast.gdrivevideoplayer.repository.Webpage

interface OnClickListener {
    fun onClickebpage(web: Webpage){
    }
    fun onDeletepage(web: Webpage) {}

}