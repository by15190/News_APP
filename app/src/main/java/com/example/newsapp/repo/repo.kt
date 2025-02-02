package com.example.newsapp.repo

import androidx.lifecycle.LiveData
import com.example.newsapp.api.retrofitInstance
import com.example.newsapp.database.DATABASE
import com.example.newsapp.models.Article


class repo(val db: DATABASE) {

    /// repo will get the data from the database and the api using the retrofit
    /// data from api
    suspend fun getHeadlines(countrycode: String, pagenumber: Int) =
        retrofitInstance.api.getHeadlines(countrycode, pagenumber)
    // get the response from the api using the retrofit
    // in retofitinstance the api variable is used to get the response from the api


    suspend fun searchnews(searchquery: String, pagenumber: Int) =
        retrofitInstance.api.searchForNews(searchquery, pagenumber)


//////// article in database

    /// now to insert the article in database of fav article
    suspend fun insertarticle(article: Article) = db.getDao().insertFAVarticle(article)

    /// to delete the article from database
    suspend fun deletearticle(article: Article) = db.getDao().deleteFAVarticle(article)

    // to show the all fav article from db
    fun getFAVarticle() = db.getDao().getallFAVarticles() as LiveData<List<Article>>

}