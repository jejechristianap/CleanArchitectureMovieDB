package com.jejec.mymoviedb.data.repository

import com.jejec.mymoviedb.data.data_source.local.MovieDao
import com.jejec.mymoviedb.data.data_source.remote.MovieApi
import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.response.DetailMovieResponse
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val dao: MovieDao,
    private val apiService: MovieApi
) : MovieRepository {

    override fun getMoviesLocal(): Flow<List<FavoriteMovies>> {
        return dao.getMovies()
    }

    override suspend fun insertMovie(movie: FavoriteMovies) {
        return dao.insertMovie(movie)
    }

    override suspend fun deleteMovie(movie: FavoriteMovies) {
        return dao.deleteMovie(movie)
    }

    override suspend fun getMoviesRemote() = flow<Resource<MovieResponse>> {
        try {
            val response = apiService.getMovies()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty Body"))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getMoviesYear(year: String) = flow<Resource<MovieResponse>> {
        try {
            val response = apiService.getMovies(year = year)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty Body"))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getDetailMovie(movieId: String) = flow<Resource<DetailMovieResponse>> {
        try {
            val response = apiService.getDetailMovie(movieId = movieId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty Body"))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}