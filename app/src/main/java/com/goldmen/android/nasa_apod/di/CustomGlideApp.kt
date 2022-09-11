package com.goldmen.android.nasa_apod.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.goldmen.android.nasa_apod.R


@GlideModule
class CustomGlideApp : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val cacheSize = (1024 * 1024 * 20).toLong()
        builder.setMemoryCache(LruResourceCache(cacheSize))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, cacheSize))
        builder.setDefaultRequestOptions(
            RequestOptions()
                .timeout(20000)
                .format(DecodeFormat.PREFER_RGB_565)
                .fallback(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .skipMemoryCache(false)
        )
    }
}