package com.goldmen.android.nasa_apod.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldmen.android.nasa_apod.domain.repository.ApodRepository
import com.goldmen.android.nasa_apod.model.ApodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteItemViewModel @Inject constructor(private val repository: ApodRepository) : ViewModel() {

    init { getFavoriteItems() }

    fun getFavoriteItems() : Flow<List<ApodEntity>> = repository.getFavoriteList(true)

    fun updateFavorite(apodEntity: ApodEntity) {
        viewModelScope.launch {
            repository.addFavoriteItem(apodEntity)
        }
    }
}