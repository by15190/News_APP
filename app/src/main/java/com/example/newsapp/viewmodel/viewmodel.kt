package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.collection.emptyLongSet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.news_response
import com.example.newsapp.repo.repo
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import java.util.Locale

class viewmodel(val app: Application, val newsRepository: repo) : AndroidViewModel(app) {

    val headlines: MutableLiveData<Resource<news_response>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse: news_response? = null

    val searchnews: MutableLiveData<Resource<news_response>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: news_response? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    init {
        getheadlines("in")
    }

 // get the handlines from the api
fun getheadlines(countryCode: String)=viewModelScope.launch {
    headlinesInternet(countryCode)
}

    // get the search news
    fun searchnews(searchQuery: String) = viewModelScope.launch {

        searchNewsInternet(searchQuery)
    }

    // Function to handle the response for fetching news headlines
    private fun handelHeadlinesResponse(response: Response<news_response>): Resource<news_response> {

        // Check if the response indicates a successful HTTP status (2xx)
        if (response.isSuccessful) {

            // Safely access the response body
            response.body()?.let { resultResponse ->

                // Increment the page number for headlines
                headlinesPage++

                // Check if this is the first response (headlinesResponse is null)
                if (headlinesResponse == null) {
                    // Assign the resultResponse to headlinesResponse
                    headlinesResponse = resultResponse
                } else {
                    // If headlinesResponse is not null, retrieve the existing articles
                    val oldArticles = headlinesResponse?.articles
                    // Get the new articles from the current response
                    val newArticle = resultResponse.articles
                    // Add the new articles to the existing list of articles
                    oldArticles?.addAll(newArticle)
                }

                // Return a successful Resource containing the updated headlinesResponse
                return Resource.success(headlinesResponse ?: resultResponse)
            }
        }

        // If the response is not successful, return an error Resource with the response message
        return Resource.error(response.message())
    }

    // Function to handle the response for searching news articles
    private fun handelSearchNewsResponse(response: Response<news_response>): Resource<news_response> {

        // Check if the response indicates a successful HTTP status (2xx)
        if (response.isSuccessful) {

            // Safely access the response body
            response.body()?.let { resultResponse ->

                // Check if this is the first search response or if the search query has changed
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    // Reset the search page number to 1 for a new search
                    searchNewsPage = 1
                    // Update the old search query to the new one
                    oldSearchQuery = newSearchQuery
                    // Assign the resultResponse to searchNewsResponse
                    searchNewsResponse = resultResponse
                } else {
                    // If the search response is not null and the query has not changed
                    // Increment the search page number
                    searchNewsPage++
                    // Retrieve the existing articles from the search response
                    val oldArticles = searchNewsResponse?.articles
                    // Get the new articles from the current response
                    val newArticle = resultResponse.articles
                    // Add the new articles to the existing list of articles
                    oldArticles?.addAll(newArticle)
                }

                // Return a successful Resource containing the updated searchNewsResponse
                return Resource.success(searchNewsResponse ?: resultResponse)
            }
        }

        // If the response is not successful, return an error Resource with the response message
        return Resource.error(response.message())
    }

    // Function to add the fav article to the database
    fun addtoFAV(article: Article) =
        viewModelScope.launch { // Launch a coroutine within the viewmodel by viewModelScope
            newsRepository.insertarticle(article) // Insert the article into the database using the repo
        }

    // Function to get the fav article
    fun getallFav() = newsRepository.getFAVarticle()

    /// to delete the article from fav article
    fun deletearticle(article: Article) = viewModelScope.launch {
        newsRepository.deletearticle(article)
    }

    // Function to check for internet connection
    fun internetConnection(context: Context): Boolean {
        // Get the ConnectivityManager system service
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {// apply this to the connectivity manager
            // Check the capabilities of the active network
            return getNetworkCapabilities(activeNetwork)?.run { // run is like let keyword but it doesn't support it
                // Check if the active network has any of the specified transport types
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true // Connected via Wi-Fi
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true // Connected via cellular data
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true // Connected via Ethernet
                    else -> false // Not connected via any recognized transport
                }
            } ?: return false // If there is no active network, return false
        }
    }


    // Function to fetch news headlines based on the provided country code
    private suspend fun headlinesInternet(countryCode: String) {
        // Post a loading state to the LiveData to indicate that data is being fetched
        headlines.postValue(Resource.loading())
        try {
            // Check if there is an internet connection
            if (internetConnection(this.getApplication())) {
                // Fetch the headlines from the repository using the country code and current page
                val response = newsRepository.getHeadlines(countryCode, headlinesPage)
                // Handle the response and post the result to the LiveData
                headlines.postValue(handelHeadlinesResponse(response))
            } else {
                // If there is no internet connection, post an error message
                headlines.postValue(Resource.error("No internet connection"))
            }

        } catch (t: Throwable) {
            // Handle any exceptions that occur during the data fetching process
            when (t) {
                is IOException -> {
                    headlines.postValue(Resource.error("Unable to connect"))
                }

                else -> {
                    headlines.postValue(Resource.error("No signal"))
                }
            }
        }
    }


    // This function performs a search for news articles based on the provided search query.
    // It is marked as 'suspend' to allow it to be called from a coroutine without blocking the main thread.
    private suspend fun searchNewsInternet(searchQuery: String) {
        // Store the new search query for potential future use.
        newSearchQuery = searchQuery

        // Post a loading state to the LiveData to indicate that a search is in progress.
        searchnews.postValue(Resource.loading())

        try {
            // Check if there is an internet connection before attempting to fetch news.
            if (internetConnection(this.getApplication())) {
                // If connected, call the repository to search for news articles with the given query and page number.
                val response = newsRepository.searchnews(searchQuery, searchNewsPage)

                // Process the response and post the result to the LiveData.
                searchnews.postValue(handelSearchNewsResponse(response))
            } else {
                // If there is no internet connection, post an error message to the LiveData.
                searchnews.postValue(Resource.error("No internet connection"))
            }
        } catch (t: Throwable) {
            // Handle exceptions that may occur during the network request.
            when (t) {
                // If the exception is an IOException, it indicates a network issue.
                is IOException -> {
                    // Post an error message indicating that the connection could not be established.
                    searchnews.postValue(Resource.error("Unable to connect to the internet"))
                }
                // For any other type of exception, post a generic error message.
                else -> {
                    searchnews.postValue(Resource.error("An unexpected error occurred"))
                }
            }
        }
    }
}