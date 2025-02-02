package com.example.newsapp.database

import androidx.room.TypeConverter
import com.example.newsapp.models.Source

class TypeConvertor {

    @TypeConverter
    fun fromSource(source: Source): String { // convert the source to string to store in db
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source { // convert the string to source to get it back from db
        return Source(name,name)

    }
}