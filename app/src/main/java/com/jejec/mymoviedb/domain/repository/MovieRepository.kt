package com.jejec.mymoviedb.domain.repository

import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.response.DetailMovieResponse
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Local

    fun getMoviesLocal(): Flow<List<FavoriteMovies>>

    suspend fun insertMovie(movie: FavoriteMovies)

    suspend fun deleteMovie(movie: FavoriteMovies)

    // Remote

    suspend fun getMoviesRemote(): Flow<Resource<MovieResponse>>

    suspend fun getMoviesYear(year: String): Flow<Resource<MovieResponse>>

    suspend fun getDetailMovie(movieId: String): Flow<Resource<DetailMovieResponse>>


}