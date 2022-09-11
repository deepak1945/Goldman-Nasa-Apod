package com.goldmen.android.nasa_apod.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goldmen.android.nasa_apod.databinding.MainViewholderBinding
import com.goldmen.android.nasa_apod.model.ApodEntity

class MainAdapter(
    private val onItemClick: (ApodEntity) -> Unit,
    private val onFavClick: (ApodEntity) -> Unit
) : ListAdapter<ApodEntity, MainViewHolder>(MainAdapterComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            MainViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick, onFavClick
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}

class MainViewHolder(
    private val binding: MainViewholderBinding,
    private val onItemClick: (ApodEntity) -> Unit,
    private val onFavClick: (ApodEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: ApodEntity) {
        binding.apply {
            mvhFav.setOnCheckedChangeListener(null)
            mvhFav.isChecked = entity.isFav

            //Loading image
            Glide.with(itemView)
                .load(entity.hdurl)
                .into(binding.mvhImage)

            mvhTitle.text = entity.title.orEmpty()
            mvhDate.text = entity.date.orEmpty()

            //Set up listeners
            root.setOnClickListener { onItemClick(entity) }
            mvhFav.setOnCheckedChangeListener { _, isChecked ->
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
                mvhFav.isChecked = isChecked
            }
        }
    }

}

class MainAdapterComparator : DiffUtil.ItemCallback<ApodEntity>() {
    override fun areItemsTheSame(oldItem: ApodEntity, newItem: ApodEntity): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: ApodEntity, newItem: ApodEntity): Boolean =
        oldItem == newItem
}