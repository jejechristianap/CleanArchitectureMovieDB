package com.jejec.mymoviedb.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Movie(
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @PrimaryKey
    val id: Int?,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    @SerializedName("overview")
    val overview: String?,
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val title: String?,
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null
) : Serializable

fun Movie?.toFavoriteMovie() = FavoriteMovies(
    id = this?.id,
    releaseDate = this?.releaseDate,
    overview = this?.overview,
    posterPath = this?.posterPath,
    title = this?.title
)