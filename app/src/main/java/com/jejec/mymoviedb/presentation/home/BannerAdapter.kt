package com.jejec.mymoviedb.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.islamkhsh.CardSliderAdapter
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.BannerBinding
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.util.Constant.BASE_URL_IMAGE

class BannerAdapter : CardSliderAdapter<BannerAdapter.ViewHolder>() {

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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindVH(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(item)
            }
        }
    }

    class ViewHolder(private val bind: BannerBinding): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: Movie) {
            Glide.with(itemView.context)
                .load("$BASE_URL_IMAGE${item.backdropPath}")
                .into(bind.ivBanner)
        }
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }
}