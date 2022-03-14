package com.jejec.mymoviedb.data.data_source.remote

import com.jejec.mymoviedb.data.data_source.remote.response.DetailMovieResponse
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.util.Constant.API_KEY
import com.jejec.mymoviedb.util.Constant.DISCOVER_MOVIE
import com.jejec.mymoviedb.util.Constant.MOVIE_MOVIE_ID
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET(DISCOVER_MOVIE)
    suspend fun getMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("year") year: String? = null
    ): Response<MovieResponse>

    @GET(MOVIE_MOVIE_ID)
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("year") year: String? = null
    ): Response<DetailMovieResponse>

}