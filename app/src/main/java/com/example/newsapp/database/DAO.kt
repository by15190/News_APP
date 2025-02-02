package com.example.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article

@Dao
interface DAO {


    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertFAVarticle(article: Article): Long // suspend indicates that this function can be called from a coroutine or another suspend function

    @Delete(Article::class )
    suspend fun  deleteFAVarticle(article: Article)


    @Query("SELECT * FROM FAV_ARTICLES") // Select items from the fav_article table from the start
     fun getallFAVarticles(): LiveData<List<Article>> /// use the main thread to show the fav article
}