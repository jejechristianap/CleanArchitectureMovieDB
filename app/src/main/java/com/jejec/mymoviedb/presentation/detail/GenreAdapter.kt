package com.jejec.mymoviedb.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.RvGenreBinding
import com.jejec.mymoviedb.databinding.RvMovieBinding
import com.jejec.mymoviedb.domain.model.Genre
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.util.Constant.BASE_URL_IMAGE

class GenreAdapter: RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Genre,
            newItem: Genre
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        val isVisible = differ.currentList.size -1 == position
        holder.binding(item, isVisible)
    }

    class ViewHolder(private val bind: RvGenreBinding): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: Genre, isVisible: Boolean) {
            bind.tvLabel.text = item.name
            bind.viewDot.isVisible = !isVisible
        }
    }
}