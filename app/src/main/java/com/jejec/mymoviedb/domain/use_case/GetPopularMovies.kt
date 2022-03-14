package com.jejec.mymoviedb.domain.use_case

import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class GetPopularMovies(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Flow<Resource<MovieResponse>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return repository.getMoviesYear(year.toString())
    }
}