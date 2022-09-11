package com.goldmen.android.nasa_apod.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goldmen.android.nasa_apod.R
import com.goldmen.android.nasa_apod.databinding.MainFragmentBinding
import com.goldmen.android.nasa_apod.domain.util.Event
import com.goldmen.android.nasa_apod.domain.util.Resource
import com.goldmen.android.nasa_apod.domain.util.showCalender
import com.goldmen.android.nasa_apod.domain.util.showSnackBarError
import com.goldmen.android.nasa_apod.model.ApodEntity
import com.goldmen.android.nasa_apod.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainAdapter: MainAdapter
    private var isFABOpen: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = MainFragmentBinding.bind(view)
        setupBinding()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.refreshData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadData()
    }

    private fun setupBinding() {
        mainAdapter = MainAdapter(::onItemClick, ::onFavClick)
        binding.mfSrl.setOnRefreshListener { viewModel.refreshData() }
        binding.mfRc.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && binding.mfFab.isShown) {
                        binding.mfFab.hide()
                        closeFABMenu()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        binding.mfFab.show()
                        showFABMenu()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
        binding.mfFab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        binding.mfFab1.setOnClickListener {
            (requireActivity() as MainActivity).navController.navigate(R.id.action_nav_main_to_favoriteItemsFragment)
        }

        binding.mfFab2.setOnClickListener {
            childFragmentManager.showCalender {
                viewModel.updateDate(it)
            }
        }
    }


    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.also {
            it.launchWhenStarted {
                viewModel.errorEvents.collect { event ->
                    when (event) {
                        is Event.ShowErrorMessage -> showSnackBarError(
                            getString(
                                R.string.mainErrorEvent,
                                event.error.localizedMessage ?: getString(R.string.mainErrorUnknown)
                            )
                        )
                    }
                }
            }
            it.launchWhenStarted {
                viewModel.lists.collect { resource ->
                    val result = resource ?: return@collect
                    binding.mfSrl.isRefreshing = resource is Resource.Loading
                    binding.mfRc.isVisible = !result.data.isNullOrEmpty()
                    binding.mfGroup.isVisible = !binding.mfRc.isVisible
                    mainAdapter.submitList(result.data)
                }
            }
        }
    }


    private fun showFABMenu() {
        isFABOpen = true
        binding.mfFab1.show()
        binding.mfFab2.show()
    }

    private fun closeFABMenu() {
        isFABOpen = false
        binding.mfFab1.hide()
        binding.mfFab2.hide()
    }

    private fun onFavClick(appEntity: ApodEntity) {
        viewModel.setFavorite(appEntity)
    }

    private fun onItemClick(apodEntity: ApodEntity) {
        (requireActivity() as MainActivity).navController.navigate(
            R.id.action_main_to_detail,
            bundleOf("apod" to apodEntity)
        )
    }
}