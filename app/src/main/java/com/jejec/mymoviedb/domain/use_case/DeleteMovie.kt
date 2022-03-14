package com.jejec.mymoviedb.domain.use_case

import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.domain.repository.MovieRepository

class DeleteMovie(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movie: FavoriteMovies) {
        repository.deleteMovie(movie)
    }
}