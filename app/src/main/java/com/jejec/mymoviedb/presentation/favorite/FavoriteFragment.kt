package com.jejec.mymoviedb.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jejec.mymoviedb.MainActivity
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.FragmentFavoriteBinding
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.domain.model.toMovie
import com.jejec.mymoviedb.util.showSnackbar
import com.jejec.mymoviedb.util.snackWithAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var bind: FragmentFavoriteBinding

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter = FavoriteAdapter()

    private var movies: List<FavoriteMovies>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentFavoriteBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onClick()
        observe()
    }

    private fun initView() {
        bind.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    private fun onClick() {
        favoriteAdapter.setOnItemClickListener { item, click ->
            when (click) {
                Click.Love -> {
                    viewModel.delete(item)
                    requireView().snackWithAction("${item.title} deleted from favorite") {
                        setAction("Undo") {
                            viewModel.addMovie(item)
                            requireView().showSnackbar(
                                "Undo successfully"
                            )
                        }
                    }
                }
                Click.Item -> {
                    (requireActivity() as MainActivity).goToDetail(
                        item.toMovie(),
                        R.id.action_favoriteFragment_to_detailFragment
                    )
                }
            }
        }
    }

    private fun observe() {
        bind.etSearch.addTextChangedListener { text ->
            /*val filtered = movies?.filter {
                it.title!!.lowercase().contains(text.toString().lowercase()) ||
                        it.genres.filter {  }
            }*/
            val filtered = mutableListOf<FavoriteMovies>()
            movies?.let { list ->
                for (i in list) {
                    if (i.title!!.lowercase().contains(text.toString().lowercase())) {
                        // search from titles
                        filtered.add(i)
                    } else {
                        // Search from genres
                        i.genres?.let { genres ->
                            for (j in genres) {
                                if (j.name!!.lowercase().contains(text.toString().lowercase())) {
                                    filtered.add(i)
                                }
                            }
                        }

                    }
                }
            }
            favoriteAdapter.differ.submitList(filtered.toList())
        }
        lifecycleScope.launch {
            viewModel
                .stateMovies
                .collect { state ->
                    when (state) {
                        is FavoriteState.Empty -> {

                        }
                        is FavoriteState.Movies -> {
                            movies = state.movies
                            movies?.let {
                                favoriteAdapter.differ.submitList(it.toList())
                            }
                        }
                    }
                }
        }
    }
}