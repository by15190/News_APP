package com.example.newsapp.api

import android.icu.text.StringSearch

import com.example.newsapp.models.news_response
import com.example.newsapp.util.constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface newsAPI {

    /// get the response from the api using the retrofit
    @GET("v2/top-headlines")     // endpoint for the api // here headlines /// b/w base url and ?
    suspend fun getHeadlines(
        @Query("country") // query for the country
        countryCode: String = "in",

        @Query("page") // query for the page number of response from api
        pageNumber: Int = 1,

        @Query("apiKey") // query for the api key
        apiKey: String = API_KEY,
    ): Response<news_response>


    @GET("v2/everything")     // endpoint for the api // here headlines /// b/w base url and ?
    suspend fun searchForNews(
        @Query("q") // query for the search
        searchQuery: String,

        @Query("page") // query for the page number of response from api
        pageNumber: Int = 1,

        @Query("apiKey") // query for the api key
        apiKey: String = API_KEY,
    ): Response<news_response>
}
