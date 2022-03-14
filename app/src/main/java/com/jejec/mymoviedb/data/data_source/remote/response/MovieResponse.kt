package com.jejec.mymoviedb.data.data_source.remote.response

import com.google.gson.annotations.SerializedName
import com.jejec.mymoviedb.domain.model.Movie

data class MovieResponse(
    val page: Int? = null,
    val results: List<Movie>? = null,
    @SerializedName("total_page")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)
