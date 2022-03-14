package com.jejec.mymoviedb.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie

@Database(
    entities = [FavoriteMovies::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {
        const val DATABASE_NAME = "movie_db"
    }
}