package com.jejec.mymoviedb.domain.use_case

import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.domain.repository.MovieRepository
import com.jejec.mymoviedb.util.InvalidMovieException

class InsertMovie(
    private val repository: MovieRepository
) {

    @Throws(InvalidMovieException::class)
    suspend operator fun invoke(movie: FavoriteMovies) {
        if (movie.title.isNullOrEmpty())
            throw InvalidMovieException("The title of the movie not found")
        repository.insertMovie(movie)
    }
}