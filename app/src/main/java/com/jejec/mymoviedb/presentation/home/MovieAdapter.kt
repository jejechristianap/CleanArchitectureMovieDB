package com.jejec.mymoviedb.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.RvMovieBinding
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.util.Constant.BASE_URL_IMAGE

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ViewHolder(private val bind: RvMovieBinding): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: Movie) {
            Glide.with(itemView.context)
                .load("$BASE_URL_IMAGE${item.posterPath}")
                .into(bind.ivMoviePoster)
        }
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }
}