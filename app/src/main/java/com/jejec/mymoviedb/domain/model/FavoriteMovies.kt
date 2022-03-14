package com.jejec.mymoviedb.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FavoriteMovies(
    @PrimaryKey
    val id: Int?,
    val genres: List<Genre>? = null,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val title: String?,
)

fun FavoriteMovies.toMovie() = Movie(
    id = id,
    posterPath = posterPath,
    overview = overview,
    releaseDate = releaseDate,
    title = title
)
