package com.example.newsapp.api

import com.example.newsapp.util.constant.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class retrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging =
                HttpLoggingInterceptor() // Create an interceptor for logging HTTP requests/responses
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) // Set the logging level to BODY

            val client =
                OkHttpClient.Builder() //This is the HTTP client that Retrofit uses to make network requests.
                    .addInterceptor(logging) // Add the logging interceptor to the OkHttp client
                    .build() // Build the OkHttp client

            Retrofit.Builder()
                .baseUrl(BASE_URL) // Set the base URL for the Retrofit instance
                .addConverterFactory(GsonConverterFactory.create()) // Add a converter factory for JSON parsing
                .client(client) // Set the OkHttp client //This sets the OkHttp client that Retrofit will use for making requests.
                .build() // Build the Retrofit instance
        }

        val api by lazy {
            retrofit.create(newsAPI::class.java) // Create the API service from the Retrofit instance
        } //This line uses the Retrofit instance (presumably already created and configured elsewhere in your code) to create an implementation of the newsAPI interface.
    // The newsAPI interface would define the endpoints and methods for making network requests related to news.
    }

}

// the client will make the request and put the log of the request/response in the  logging