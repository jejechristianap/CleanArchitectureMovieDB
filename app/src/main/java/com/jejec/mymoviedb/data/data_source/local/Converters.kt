package com.jejec.mymoviedb.data.data_source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jejec.mymoviedb.domain.model.Genre


class Converters {

    @TypeConverter
    fun listGenreToJson(value: List<Genre>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonGenreToList(value: String) = Gson().fromJson(value, Array<Genre>::class.java).toList()
}