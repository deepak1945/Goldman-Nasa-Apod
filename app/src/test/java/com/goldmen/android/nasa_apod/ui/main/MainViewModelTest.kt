package com.goldmen.android.nasa_apod.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.goldmen.android.nasa_apod.CoroutinesTestRule
import com.goldmen.android.nasa_apod.domain.repository.ApodRepositoryImpl
import com.goldmen.android.nasa_apod.domain.util.toCustomMap
import com.goldmen.android.nasa_apod.utils.NetworkHelper
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class MainViewModelTest {

    private val apodRepositoryImpl = Mockito.mock(ApodRepositoryImpl::class.java)
    private val networkHelper = Mockito.mock(NetworkHelper::class.java)
    private lateinit var mainViewModel: MainViewModel

    @Inject
    private val gson = Gson()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    @Ignore("Network caching not required now")
    fun `test verify error state network disconnected`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            Mockito.`when`(networkHelper.isNetworkConnected()).thenReturn(false)
            mainViewModel = MainViewModel(apodRepositoryImpl)
            val resource = mainViewModel.lists.value
            MatcherAssert.assertThat(mainViewModel.lists, CoreMatchers.notNullValue())
            MatcherAssert.assertThat(
                resource!!.error,
                CoreMatchers.`is`(Exception("Network disconnected"))
            )
            MatcherAssert.assertThat(resource.data, CoreMatchers.nullValue())
            Mockito.verify(apodRepositoryImpl, Mockito.times(0)).getLatestApods(
                isRefresh =  true,
                param = mainViewModel.lists.first().toCustomMap(gson),
                onSuccess = {},
                onError = {}
            )
        }

}