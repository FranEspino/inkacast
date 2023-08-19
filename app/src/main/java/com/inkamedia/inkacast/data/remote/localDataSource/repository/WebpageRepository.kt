package com.inkamedia.inkacast.data.remote.localDataSource.repository

import com.fraporitmos.gdrivevideoplayer.repository.Webpage
import com.fraporitmos.gdrivevideoplayer.repository.WebpageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebpageRepository @Inject constructor(
    private val webpageDao: WebpageDao
){

    fun  readWebPage(): Flow<List<Webpage>> {
        return webpageDao.readWebPage()
    }
    suspend fun createWebPage(webpage: Webpage) {
        webpageDao.createWebPage(webpage)
    }

    suspend fun deleteWebPage(webpage: Webpage) {
        webpageDao.deleteWebPage(webpage)
    }
    suspend fun  countByUrl(webpage: Webpage):Int{
       return  webpageDao.countByUrl(webpage.url, webpage.title)
    }

    fun searchWebPage(seacrhQuery: String): Flow<List<Webpage>> {
        return webpageDao.searchWebPage(seacrhQuery)
    }


}