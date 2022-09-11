package com.goldmen.android.nasa_apod.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldmen.android.nasa_apod.di.key.ApiKey
import com.goldmen.android.nasa_apod.domain.repository.ApodRepository
import com.goldmen.android.nasa_apod.domain.util.*
import com.goldmen.android.nasa_apod.model.ApodEntity
import com.goldmen.android.nasa_apod.model.ApodPost
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApodRepository) : ViewModel() {

    @ApiKey
    @Inject
    lateinit var apiKey: String

    @Inject
    lateinit var gson: Gson

    private val errorEventChannel = Channel<Event>()
    val errorEvents = errorEventChannel.receiveAsFlow()

    private val refreshChannel = Channel<Refresh>()
    private val refreshEvent = refreshChannel.receiveAsFlow()

    private val dateUpdateChannel = Channel<ApodPost>(1)
    private val dateEvent = dateUpdateChannel.receiveAsFlow()

    val lists = dateEvent.combine(refreshEvent) { date, refresh ->
        Pair(date, refresh)
    }.flatMapLatest { value ->
        repository.getLatestApods(
            isRefresh = value.second == Refresh.FORCE,
            param = value.first.toCustomMap(gson),
            onSuccess = {},
            onError = { throwable ->
                throwable.printStackTrace()
                viewModelScope.launch {
                    errorEventChannel.send(Event.ShowErrorMessage(throwable))
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun loadData() {
        if (lists.value !is Resource.Loading) {
            viewModelScope.launch { refreshChannel.send(Refresh.NORMAL) }
            viewModelScope.launch { dateUpdateChannel.send(currentDate()) }
        }
    }

    fun refreshData(date: ApodPost = currentDate()) {
        if (lists.value !is Resource.Loading) {
            viewModelScope.launch {
                dateUpdateChannel.send(date)
            }
            viewModelScope.launch {
                refreshChannel.send(Refresh.FORCE)
            }
        }
    }

    fun updateDate(epoch: Long) {
        ApodPost(
            apiKey = apiKey,
            endDate = epoch.toFormattedDate(),
            startDate = epoch.toPastFormattedDate()
        ).also {
            refreshData(it)
        }

    }

    private fun currentDate(): ApodPost =
        ApodPost(
            apiKey = apiKey,
            endDate = getCurrentFormattedDate,
            startDate = getPastFormattedDate
        )

   fun setFavorite(apodEntity: ApodEntity) {
       viewModelScope.launch {
           repository.addFavoriteItem(apodEntity)
       }
   }
}