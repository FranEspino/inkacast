package com.fraporitmos.gdrivevideoplayer.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "webpage")
data class Webpage(
    val title : String,
    val url : String,
    val bitmap : String

){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
//@Entity(tableName = "Folder")
//data class Folder(
//    @PrimaryKey(autoGenerate = true,) var id: Long = 0,
//    val folderKey: String,
//    val name: String,
//    val position: Int,
//    val date: Long,
//    val urlFolder: String
//)

