package com.jejec.mymoviedb.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.hasInternetConnection
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.domain.use_case.MovieUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: MovieUseCases,
    private val app: Application
) : AndroidViewModel(app) {

    private val _stateMovies = MutableStateFlow<HomeState>(HomeState.Init)
    val stateMovies: StateFlow<HomeState> get() = _stateMovies

    private val _stateComingSoon = MutableStateFlow<HomeState>(HomeState.Init)
    val stateComingSoon: StateFlow<HomeState> get() = _stateComingSoon


    private fun loadingMovies(isLoading: Boolean) {
        _stateMovies.value = HomeState.Loading(isLoading)
    }

    private fun loadingComingSoon(isLoading: Boolean) {
        _stateComingSoon.value = HomeState.Loading(isLoading)
    }

    init {
        getMovies()
        getComingMovies()
    }

    fun getMovies() = viewModelScope.launch {

        if (app.hasInternetConnection()) {
            useCase.getMoviesRemote.invoke().onStart {
                loadingMovies(true)
            }.collect { result ->
                loadingMovies(false)
                when (result) {
                    is Resource.Success -> _stateMovies.value = HomeState.Success(result.data)
                    is Resource.Error -> _stateMovies.value = HomeState.Error(result.message, Error.ApiError)
                }
            }
        } else {
            _stateMovies.value = HomeState.Error("Please check your connection", Error.Connection)
        }
    }

    fun getComingMovies() = viewModelScope.launch {
        if (app.hasInternetConnection()) {
            useCase.getComingSoonMovies.invoke().onStart {
                loadingComingSoon(true)
            }.collect { result ->
                loadingComingSoon(false)
                when (result) {
                    is Resource.Success -> _stateComingSoon.value = HomeState.Success(result.data)
                    is Resource.Error -> _stateComingSoon.value = HomeState.Error(result.message, Error.ApiError)
                }
            }
        } else {
            _stateComingSoon.value = HomeState.Error("Please check your connection", Error.Connection)
        }
    }
}

sealed class HomeState {
    object Init : HomeState()
    data class Loading(val isLoading: Boolean) : HomeState()
    data class Success(val movieResponse: MovieResponse?) : HomeState()
    data class Error(val message: String?, val error: com.jejec.mymoviedb.presentation.home.Error) : HomeState()
}

enum class Error {
    ApiError, Connection
}