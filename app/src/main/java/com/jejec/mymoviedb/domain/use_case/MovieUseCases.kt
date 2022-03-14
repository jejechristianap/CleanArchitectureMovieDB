package com.jejec.mymoviedb.domain.use_case

data class MovieUseCases(
    val deleteMovie: DeleteMovie,
    val getComingSoonMovies: GetComingSoonMovies,
    val getDetailMovie: GetDetailMovie,
    val getMovies: GetMoviesLocal,
    val getMoviesRemote: GetMoviesRemote,
    val getPopularMovies: GetPopularMovies,
    val insertMovie: InsertMovie
)
