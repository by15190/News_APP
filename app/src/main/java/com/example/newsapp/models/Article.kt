package com.example.newsapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity("fav_articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source, /// now here the type source need to be converted to string in order to store in database ,
                                // use the type convertor to convert
    val title: String,
    val url: String,
    val urlToImage: String,
) : Serializable