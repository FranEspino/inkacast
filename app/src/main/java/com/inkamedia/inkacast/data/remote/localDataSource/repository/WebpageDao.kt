package com.inkacast.gdrivevideoplayer.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WebpageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createWebPage(webpage: Webpage)

    @Query("SELECT COUNT(*) FROM webpage WHERE url = :url OR title = :title ")
    fun countByUrl(url: String, title:String): Int

    @Delete
    fun deleteWebPage(webpage: Webpage)

    @Query("SELECT * FROM webpage")
    fun readWebPage(): Flow<List<Webpage>>

    //query of search
    @Query("SELECT * FROM webpage WHERE title = :seacrhQuery LIKE :seacrhQuery OR url = :seacrhQuery LIKE :seacrhQuery")
    fun searchWebPage(seacrhQuery: String): Flow<List<Webpage>>

}