package com.goldmen.android.nasa_apod.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.goldmen.android.nasa_apod.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkInfo


@RunWith(RobolectricTestRunner::class)
class NetworkHelperTest {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var context: Context
    private lateinit var networkHelper: NetworkHelper

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        networkHelper = NetworkHelper(context)
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `test active network disabled`() {
        shadowOf(connectivityManager).setDefaultNetworkActive(false)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(false))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `test active network capabilities null`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_WIFI,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        shadowOf(connectivityManager).setNetworkCapabilities(
            connectivityManager.activeNetwork,
            null
        )
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(false))
    }


    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `test active network capability is Wifi`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_WIFI,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(true))
    }


    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `test active network capability is Cellular`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_MOBILE,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(true))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `test active network capability is Ethernet`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_ETHERNET,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(false))
    }


    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun `test active network capability on lollipop is Wifi`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_WIFI,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(true))
    }


    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun `test active network capability on lollipop is Cellular`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_MOBILE,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(true))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun `test active network capability on lollipop is Ethernet`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_ETHERNET,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(true))
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun `test active network capability on lollipop is dummy`() {
        val networkInfo = ShadowNetworkInfo.newInstance(
            NetworkInfo.DetailedState.CONNECTED,
            ConnectivityManager.TYPE_DUMMY,
            0 /* subType */,
            true /* isAvailable */,
            NetworkInfo.State.CONNECTED  /* isConnected */
        )
        shadowOf(connectivityManager).setActiveNetworkInfo(networkInfo)
        MatcherAssert.assertThat(networkHelper.isNetworkConnected(), CoreMatchers.`is`(false))
    }

}