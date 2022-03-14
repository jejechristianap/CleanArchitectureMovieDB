package com.jejec.mymoviedb.presentation.detail

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import com.jejec.mymoviedb.data.data_source.remote.response.toFavoriteMovies
import com.jejec.mymoviedb.databinding.FragmentDetailBinding
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.domain.model.toFavoriteMovie
import com.jejec.mymoviedb.util.Constant.BASE_URL_IMAGE
import com.jejec.mymoviedb.util.convertDpToPx
import com.jejec.mymoviedb.util.fromMinutesToHHmm
import com.jejec.mymoviedb.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var bind: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    private var movie: Movie? = null

    private var favoriteMovies: FavoriteMovies? = null

    private val genreAdapter = GenreAdapter()

    private val castAdapter = CastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentDetailBinding.inflate(inflater, container, false)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupToolbar()
        onClick()
        observe()
    }

    private fun initView() {
        movie = args.movie
        viewModel.getDetailMovie(movie?.id.toString())

        bind.tvOverview.text = movie?.overview
        with(bind.layoutDetail) {
            Glide.with(requireContext())
                .load("$BASE_URL_IMAGE${movie?.posterPath}")
                .into(ivPoster)
            tvTitle.text = movie?.title
            tvTitle.setOnClickListener {
                viewModel.addMovie(movie.toFavoriteMovie())
            }
            rvGenre.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = genreAdapter
                if (itemDecorationCount == 0) {
                    addItemDecoration(
                        SpacingItemDecoration(
                            Spacing(
                                horizontal = requireContext().convertDpToPx(6),
                            )
                        )
                    )
                }
            }

            bind.rvCast.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = castAdapter
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
    }

    private fun setupToolbar() {
        var scrollRange = -1
        (requireActivity() as AppCompatActivity).setSupportActionBar(bind.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) scrollRange = barLayout?.totalScrollRange!!
                (scrollRange.plus(verticalOffset) == 0).let {
                    bind.tvToolbarTitle.text = if (it) movie?.title else ""
                }
            }
        )
        bind.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onClick() {
        bind.layoutDetail.btnAddToFavorite.setOnClickListener {
            favoriteMovies?.let {
                Timber.d("fav: $it")
                viewModel.addMovie(it)
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.addToFavoriteFlow.collectLatest {
                when (it) {
                    is AddFavoriteState.Added -> {
                        requireView().showSnackbar(
                            "Movie added to Favorite successfully"
                        )
                    }
                    is AddFavoriteState.Error -> {
                        requireView().showSnackbar(
                            it.message
                        )
                    }
                }
            }
        }
        viewModel.stateDetailMovie
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is DetailState.Init -> Unit
                    is DetailState.Loading -> {
                        Timber.d("loading state detail movies: ${state.isLoading}")
                    }
                    is DetailState.Error -> {
                        Timber.e("detail movies error: ${state.message}")
                    }
                    is DetailState.Success -> {
                        state.response?.let { movie ->
                            favoriteMovies = movie.toFavoriteMovies()
                            val convertTime = movie.runtime?.fromMinutesToHHmm()
                            bind.layoutDetail.tvRuntime.text = convertTime
                            genreAdapter.differ.submitList(movie.genres?.toList())
                            castAdapter.differ.submitList(movie.productionCompanies?.toList())
                        }
                    }
                }
            }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}