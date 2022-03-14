package com.jejec.mymoviedb.data.data_source.local

import androidx.room.*
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM favoritemovies")
    fun getMovies(): Flow<List<FavoriteMovies>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: FavoriteMovies)

    @Delete
    suspend fun deleteMovie(movie: FavoriteMovies)
}