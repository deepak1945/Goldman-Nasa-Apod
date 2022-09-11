package com.goldmen.android.nasa_apod.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goldmen.android.nasa_apod.databinding.LayoutFavoriteItemBinding
import com.goldmen.android.nasa_apod.model.ApodEntity

internal class FavoriteItemAdapter(
    private var itemList: List<ApodEntity> = arrayListOf(),
    val onFavClick: (ApodEntity) -> Unit
) : RecyclerView.Adapter<FavoriteItemAdapter.FavoriteItemViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(itemList: List<ApodEntity>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteItemViewHolder =
        FavoriteItemViewHolder(
            LayoutFavoriteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    inner class FavoriteItemViewHolder(private val binding: LayoutFavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: ApodEntity) {
            binding.iconFav.setOnCheckedChangeListener(null)
            binding.iconFav.isChecked = entity.isFav

            binding.favTitle.text = entity.title

            //Loading image
            Glide.with(itemView)
                .load(entity.hdurl)
                .into(binding.favImage)

            binding.iconFav.setOnCheckedChangeListener { compoundButton, isChecked ->
                onFavClick(
                    ApodEntity(
                        entity.id,
                        entity.copyright,
                        entity.date,
                        entity.explanation,
                        entity.hdurl,
                        entity.mediaType,
                        entity.serviceVersion,
                        entity.title,
                        entity.url,
                        isChecked
                    )
                )
                binding.iconFav.isChecked = isChecked
                removeItem(adapterPosition)
            }
        }

    }

    private fun removeItem(position: Int) {
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemList.size)
    }
}