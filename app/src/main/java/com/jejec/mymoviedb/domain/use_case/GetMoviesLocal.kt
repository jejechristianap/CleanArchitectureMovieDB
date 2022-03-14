package com.jejec.mymoviedb.domain.use_case

import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class GetMoviesLocal(
    private val repository: MovieRepository
) {

    operator fun invoke(): Flow<List<FavoriteMovies>> {
        return repository.getMoviesLocal().map { movies ->
            movies.sortedBy {
                it.title?.lowercase()
            }
        }
    }
}