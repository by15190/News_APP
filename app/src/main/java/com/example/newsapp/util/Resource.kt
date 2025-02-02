package com.example.newsapp.util

sealed class Resource<T>( // sealed class is used to create a class that can be subclassed but not instantiated
 // no hierarchy of classes can be created using sealed class

    val data : T? = null,
    val message : String? = null
)
{
    class success<T> (date:T) : Resource<T>(date)
    /// success class passing the argument in the resources clas

    class error<T> (message: String, data: T? = null) : Resource<T>(data, message)
    /// error class passing the argument in the resources clas

    class loading<T> : Resource<T>()
    /// loading class not passing the argument in the resources clas


}