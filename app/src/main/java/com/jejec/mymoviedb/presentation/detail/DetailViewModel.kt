package com.jejec.mymoviedb.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.response.DetailMovieResponse
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.domain.use_case.MovieUseCases
import com.jejec.mymoviedb.presentation.home.HomeState
import com.jejec.mymoviedb.util.InvalidMovieException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCases: MovieUseCases
) : ViewModel() {


    private val _addToFavoriteFlow = MutableSharedFlow<AddFavoriteState>()
    val addToFavoriteFlow = _addToFavoriteFlow.asSharedFlow()

    private val _stateDetailMovie = MutableStateFlow<DetailState>(DetailState.Init)
    val stateDetailMovie: StateFlow<DetailState> get() = _stateDetailMovie

    fun addMovie(movie: FavoriteMovies) = viewModelScope.launch {
        try {
            useCases.insertMovie.invoke(movie)
            _addToFavoriteFlow.emit(AddFavoriteState.Added)
        } catch (e: InvalidMovieException) {
            _addToFavoriteFlow.emit(
                AddFavoriteState.Error(
                    message = e.message ?: "Couldn't add movie"
                )
            )
        }
    }

    fun getDetailMovie(movieId: String) = viewModelScope.launch {
        useCases.getDetailMovie.invoke(movieId)
            .onStart {
                _stateDetailMovie.value = DetailState.Loading(true)
            }.catch { ex ->
                _stateDetailMovie.value = DetailState.Loading(false)
                _stateDetailMovie.value = DetailState.Error(ex.message)
            }.collectLatest { result ->
                _stateDetailMovie.value = DetailState.Loading(false)
                when (result) {
                    is Resource.Success -> {
                        _stateDetailMovie.value = DetailState.Success(result.data)
                    }
                    is Resource.Error -> {
                        _stateDetailMovie.value = DetailState.Error(result.message)
                    }
                }
            }
    }
}

sealed class AddFavoriteState {
    object Added : AddFavoriteState()
    data class Error(val message: String) : AddFavoriteState()
}

sealed class DetailState {
    object Init : DetailState()
    data class Loading(val isLoading: Boolean) : DetailState()
    data class Success(val response: DetailMovieResponse?) : DetailState()
    data class Error(val message: String?) : DetailState()
}