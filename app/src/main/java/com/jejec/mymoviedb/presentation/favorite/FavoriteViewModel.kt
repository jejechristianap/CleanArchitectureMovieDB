package com.jejec.mymoviedb.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.use_case.MovieUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val useCases: MovieUseCases
) : ViewModel() {

    private val _stateMovies = MutableSharedFlow<FavoriteState>()
    val stateMovies = _stateMovies.asSharedFlow()

    init {
        getMovies()
    }

    fun getMovies() = viewModelScope.launch {
        var repeated = 0
        useCases.getMovies.invoke().onEach { movies ->
            repeated++
            movies.map {

            }
            Timber.v("repeated: $repeated")
        }.collect { movies ->
            if (movies.isEmpty())
                _stateMovies.emit(FavoriteState.Empty("You don't have any favorite movie"))
            else
                _stateMovies.emit(FavoriteState.Movies(movies))
        }
    }

    fun addMovie(movie: FavoriteMovies) = viewModelScope.launch {
        useCases.insertMovie.invoke(movie)
    }

    fun delete(movie: FavoriteMovies) = viewModelScope.launch {
        useCases.deleteMovie.invoke(movie)
    }
}

sealed class FavoriteState {
    data class Movies(val movies: List<FavoriteMovies>) : FavoriteState()
    data class Empty(val message: String) : FavoriteState()
}