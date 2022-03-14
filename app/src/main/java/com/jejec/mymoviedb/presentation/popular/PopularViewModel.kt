package com.jejec.mymoviedb.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejec.mymoviedb.data.data_source.remote.Resource
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.domain.use_case.MovieUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val useCase: MovieUseCases
) : ViewModel() {

    private val _stateMovies = MutableStateFlow<PopularState>(PopularState.Init)
    val stateMovies: StateFlow<PopularState> get() = _stateMovies

    init {
        getMovies()
    }

    fun getMovies() = viewModelScope.launch {
        useCase.getMoviesRemote.invoke().onStart {
            _stateMovies.value = PopularState.Loading(true)
        }.collect { result ->
            _stateMovies.value = PopularState.Loading(false)
            when (result) {
                is Resource.Success -> _stateMovies.value = PopularState.Success(result.data)
                is Resource.Error -> _stateMovies.value =
                    PopularState.Error(result.message)
            }
        }
    }
}

sealed class PopularState {
    object Init : PopularState()
    data class Loading(val isLoading: Boolean) : PopularState()
    data class Success(val movieResponse: MovieResponse?) : PopularState()
    data class Error(val message: String?) : PopularState()
}