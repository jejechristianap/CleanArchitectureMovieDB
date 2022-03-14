package com.jejec.mymoviedb.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jejec.mymoviedb.R
import com.jejec.mymoviedb.databinding.RvCastBinding
import com.jejec.mymoviedb.databinding.RvGenreBinding
import com.jejec.mymoviedb.databinding.RvMovieBinding
import com.jejec.mymoviedb.domain.model.Genre
import com.jejec.mymoviedb.domain.model.Movie
import com.jejec.mymoviedb.domain.model.ProductionCompany
import com.jejec.mymoviedb.util.Constant.BASE_URL_IMAGE

class CastAdapter: RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ProductionCompany>() {
        override fun areItemsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductionCompany,
            newItem: ProductionCompany
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
            RvCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding(item)
    }

    class ViewHolder(private val bind: RvCastBinding): RecyclerView.ViewHolder(bind.root) {
        fun binding(item: ProductionCompany) {
            Glide.with(itemView.context)
                .load("$BASE_URL_IMAGE${item.logoPath}")
                .circleCrop()
                .into(bind.ivCast)
            bind.tvName.text = item.name
        }
    }
}