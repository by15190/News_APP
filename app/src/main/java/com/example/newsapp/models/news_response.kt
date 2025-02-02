package com.example.newsapp.models

data class news_response(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int,
)