package com.example.newsapp.util

import androidx.sqlite.db.SupportSQLiteCompat

class constant {
    companion object {
        const val API_KEY ="your api key" // api key for news api
        const val BASE_URL = "https://newsapi.org/" /// base url of site , not the api url
        const val SEARCH_NEWS_TIME_DELAY = 500L // time delay of 500ms b/w consecutive search request
        const val QUERY_PAGE_SIZE = 20 // no of pages to load at once from api response for pagination purpose
    }
}
