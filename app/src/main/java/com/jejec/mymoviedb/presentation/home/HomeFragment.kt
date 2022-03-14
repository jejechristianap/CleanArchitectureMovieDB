package com.jejec.mymoviedb.presentation.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import com.jejec.mymoviedb.MainActivity
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.data.data_source.remote.response.MovieResponse
import com.jejec.mymoviedb.databinding.FragmentHomeBinding
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.util.convertDpToPx
import com.jejec.mymoviedb.util.showSnackbar
import com.jejec.mymoviedb.util.snackWithAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var bind: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private var movieResponse: MovieResponse? = null

    private var popularMovies: List<Movie>? = null

    private val bannerAdapter = BannerAdapter()

    private val popularMoviesAdapter = MovieAdapter()

    private val comingSoonAdapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentHomeBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onClick()
        observe()
    }

    private fun initView() {
        bind.vpBanner.adapter = bannerAdapter
        bind.rvPopularMovies.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    SpacingItemDecoration(
                        Spacing(
                            horizontal = requireContext().convertDpToPx(8),
                            edges = Rect(
                                requireContext().convertDpToPx(20),
                                0,
                                requireContext().convertDpToPx(20),
                                0
                            )
                        )
                    )
                )
            }
        }

        bind.rvComingSoon.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = comingSoonAdapter
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    SpacingItemDecoration(
                        Spacing(
                            horizontal = requireContext().convertDpToPx(8),
                            edges = Rect(
                                requireContext().convertDpToPx(20),
                                0,
                                requireContext().convertDpToPx(20),
                                0
                            )
                        )
                    )
                )
            }
        }
    }

    private fun onClick() {
        bannerAdapter.setOnItemClickListener {
            (requireActivity() as MainActivity).goToDetail(
                it,
                R.id.action_homeFragment_to_detailFragment
            )
        }
        popularMoviesAdapter.setOnItemClickListener {
            (requireActivity() as MainActivity).goToDetail(
                it,
                R.id.action_homeFragment_to_detailFragment
            )
        }
        comingSoonAdapter.setOnItemClickListener {
            (requireActivity() as MainActivity).goToDetail(
                it,
                R.id.action_homeFragment_to_detailFragment
            )
        }
    }

    private fun observe() {
        viewModel
            .stateMovies
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is HomeState.Init -> Unit
                    is HomeState.Loading -> {
                        Timber.d("loading state movies: ${state.isLoading}")
                    }
                    is HomeState.Error -> {
                        Timber.e("error: ${state.message}")
                        when (state.error) {
                            Error.ApiError -> requireView().showSnackbar(state.message.toString())
                            Error.Connection -> requireView().snackWithAction(
                                state.message.toString(),
                                indefinite = true
                            ) {
                                setAction("Retry") {
                                    viewModel.getMovies()
                                }
                            }
                        }
                    }
                    is HomeState.Success -> {
                        Timber.d("success: ${state.movieResponse}")
                        movieResponse = state.movieResponse
                        popularMovies = movieResponse?.results?.filter {
                            it.id!! % 2 == 0
                        }
                        bannerAdapter.differ.submitList(popularMovies?.toList())
                        val limitMovies = movieResponse?.results?.take(10)
                        popularMoviesAdapter.differ.submitList(limitMovies?.toList())
                    }
                }
            }.launchIn(lifecycleScope)

        viewModel
            .stateComingSoon
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is HomeState.Init -> Unit
                    is HomeState.Loading -> {
                        Timber.d("loading: ${state.isLoading}")
                    }
                    is HomeState.Error -> {
                        Timber.e("error: ${state.message}")
                        when (state.error) {
                            Error.ApiError -> requireView().showSnackbar(state.message.toString())
                            Error.Connection -> requireView().snackWithAction(
                                state.message.toString(),
                                indefinite = true
                            ) {
                                setAction("Retry") {
                                    viewModel.getComingMovies()
                                }
                            }
                        }
                    }
                    is HomeState.Success -> {
                        Timber.d("success: ${state.movieResponse}")
                        val limitMovies = state.movieResponse?.results?.take(10)
                        comingSoonAdapter.differ.submitList(limitMovies?.toList())
                    }
                }
            }.launchIn(lifecycleScope)
    }
}