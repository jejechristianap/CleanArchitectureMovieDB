package com.jejec.mymoviedb.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jejec.mymoviedb.databinding.RvFavoriteBinding
import com.jejec.mymoviedb.domain.model.FavoriteMovies
import com.jejec.mymoviedb.util.Constant.BASE_URL_IMAGE
import com.jejec.mymoviedb.util.formatDate

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<FavoriteMovies>() {
        override fun areItemsTheSame(oldItem: FavoriteMovies, newItem: FavoriteMovies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavoriteMovies,
            newItem: FavoriteMovies
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvFavoriteBinding.inflate(
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
                it(item, Click.Item)
            }
        }
        holder.bind.ivLove.setOnClickListener {
            onItemClickListener?.let {
                it(item, Click.Love)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ViewHolder(val bind: RvFavoriteBinding) : RecyclerView.ViewHolder(bind.root) {
        fun binding(item: FavoriteMovies) {
            with(bind) {
                Glide.with(itemView.context)
                    .load("$BASE_URL_IMAGE${item.posterPath}")
                    .into(ivPoster)
                tvTitle.text = item.title
                tvYear.text = item.releaseDate?.formatDate()
                val str = mutableListOf<String>()
                item.genres?.map {
                    str.add(it.name.toString())
                }
                tvGenre.text = str.toString().replace("[", "").replace("]", "")
            }
        }
    }

    private var onItemClickListener: ((FavoriteMovies, Click) -> Unit)? = null

    fun setOnItemClickListener(listener: (FavoriteMovies, Click) -> Unit) {
        onItemClickListener = listener
    }
}

enum class Click {
    Love, Item
}