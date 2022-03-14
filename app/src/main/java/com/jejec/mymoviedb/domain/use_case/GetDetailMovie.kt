package com.jejec.mymoviedb.domain.use_case

import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.response.DetailMovieResponse
import com.jejec.mymoviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetDetailMovie(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: String): Flow<Resource<DetailMovieResponse>> {
        return repository.getDetailMovie(movieId = movieId)
    }
}