package com.jejec.mymoviedb.presentation.popular

import android.graphics.Rect
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.jejec.mymoviedb.MainActivity
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.FragmentPopularBinding
import com.jejec.mymoviedb.domain.model.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import com.jejec.mymoviedb.util.convertDpToPx


@AndroidEntryPoint
class PopularFragment : Fragment() {

    private lateinit var bind: FragmentPopularBinding

    private val viewModel: PopularViewModel by viewModels()

    private val popularMovieAdapter = PopularMovieAdapter()

    private var movies: List<Movie>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentPopularBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onClick()
        observe()
    }

    private fun initView() {
        bind.rvPopularMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = popularMovieAdapter
            val spacing = requireContext().convertDpToPx(15) / 2
            setPadding(spacing, spacing, spacing, spacing)
            clipToPadding = false
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect[spacing, spacing, spacing] = spacing
                }
            })
        }
    }


    private fun onClick() {
        popularMovieAdapter.setOnItemClickListener {
            (requireActivity() as MainActivity).goToDetail(
                it,
                R.id.action_popularFragment_to_detailFragment
            )
        }
    }

    private fun observe() {
        bind.etSearch.addTextChangedListener { text ->
            MainScope().launch {
                text?.let {
                    bind.tvSearchResult.isInvisible = text.toString().isEmpty()
                    val searchResult = requireContext().resources.getString(
                        R.string.showing_result_of,
                        text.toString()
                    )
                    bind.tvSearchResult.text = Html.fromHtml(searchResult)
                    movies?.let { list ->
                        val filtered = list.filter {
                            it.title!!.lowercase().contains(text.toString().lowercase())
                        }
                        popularMovieAdapter.differ.submitList(filtered)
                    }
                    // delay(300)
                }

            }
        }
        viewModel.stateMovies
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { state ->
                when (state) {
                    is PopularState.Init -> Unit
                    is PopularState.Loading -> {
                        Timber.d("loading: ${state.isLoading}")
                    }
                    is PopularState.Error -> {
                        Timber.e("error: ${state.message}")
                    }
                    is PopularState.Success -> {
                        movies = state.movieResponse?.results
                        popularMovieAdapter.differ.submitList(movies?.toList())
                    }
                }
            }.launchIn(lifecycleScope)
    }
}