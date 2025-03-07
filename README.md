# News App

A modern Android news application that fetches real-time news using NewsAPI, implements MVVM architecture, and allows users to save their favorite articles locally using Room database.

## Features

- Real-time news updates from NewsAPI
- Browse news by categories (Business, Technology, Sports, etc.)
- Search news articles
- Save favorite articles for offline reading
- Share articles with friends
- Dark/Light theme support
- Pull-to-refresh functionality
- Infinite scroll pagination
- Responsive layout for different screen sizes

## Architecture

This application implements the MVVM (Model-View-ViewModel) architecture pattern and follows clean architecture principles:

- **Model**: Handles data operations through Repository pattern
  - Remote data source (NewsAPI)
  - Local data source (Room Database)
- **View**: UI layer with Activities and Fragments
- **ViewModel**: Manages UI-related data and business logic
- **Repository**: Single source of truth, manages data from both API and local database

### Tech Stack

- Kotlin
- Android Architecture Components
  - Room Database (for favorite articles)
  - ViewModel
  - LiveData
  - Coroutines for async operations
- Retrofit for API calls
- Dagger Hilt for dependency injection
- Glide for image loading
- Material Design Components
- ViewBinding
- Navigation Component
- Unit Testing (JUnit, Mockito)
- UI Testing (Espresso)

## Project Structure

```
app/
├── data/
│   ├── api/
│   │   ├── NewsApiService.kt
│   │   └── models/
│   ├── local/
│   │   ├── dao/
│   │   ├── entities/
│   │   └── NewsDatabase.kt
│   └── repository/
├── di/
│   └── modules/
├── domain/
│   ├── model/
│   └── usecase/
├── ui/
│   ├── news/
│   │   ├── list/
│   │   └── detail/
│   ├── favorites/
│   ├── search/
│   └── common/
└── utils/
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 21 or higher
- Kotlin 1.5 or higher
- NewsAPI key (get it from [NewsAPI](https://newsapi.org))

### Installation

1. Clone the repository:
```bash
git clone https://github.com/by15190/News_APP.git
```

2. Add your NewsAPI key in `local.properties`:
```properties
NEWS_API_KEY=your_api_key_here
```

3. Open the project in Android Studio
4. Sync project with Gradle files
5. Run the app on an emulator or physical device

## API Integration

The app uses Retrofit to communicate with NewsAPI:

```kotlin
interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String?,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse
    
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}
```

## Database Schema

### Article Entity
```kotlin
@Entity(tableName = "favorite_articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String?,
    val content: String?,
    val author: String?,
    val publishedAt: String,
    val urlToImage: String?,
    val source: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

## Key Features Implementation

### Favorite Articles
- Users can save articles for offline reading
- Room database stores article details
- Favorites sync with UI immediately using LiveData

### Pagination
- Implemented using Paging 3 library
- Smooth infinite scrolling
- Efficient memory usage

### Search
- Real-time search suggestions
- Search history using Room
- Debounced API calls

## Error Handling

- Network connectivity checks
- API error handling
- Graceful degradation
- User-friendly error messages
- Retry mechanisms

## Testing

### Unit Tests
- ViewModel testing
- Repository testing
- Use case testing
- API response testing

### UI Tests
- Navigation testing
- Screen state testing
- User interaction testing

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



## Acknowledgments

- [NewsAPI](https://newsapi.org) for providing the news data
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Material Design](https://material.io/design)
- [Retrofit](https://square.github.io/retrofit/)
- [Room](https://developer.android.com/training/data-storage/room)
