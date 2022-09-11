package com.goldmen.android.nasa_apod.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.goldmen.android.nasa_apod.R
import com.goldmen.android.nasa_apod.databinding.FragmentFavoriteItemsBinding
import com.goldmen.android.nasa_apod.databinding.MainFragmentBinding
import com.goldmen.android.nasa_apod.model.ApodEntity
import com.goldmen.android.nasa_apod.ui.main.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteItemsFragment : Fragment(R.layout.fragment_favorite_items) {

    private val viewModel: FavoriteItemViewModel by viewModels()

    private var _binding: FragmentFavoriteItemsBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = FragmentFavoriteItemsBinding.bind(view)
        setupBinding()
        setupObserver()
    }


    fun setupBinding() {
        favoriteAdapter = FavoriteItemAdapter(arrayListOf(), ::onFavClick)
        binding.mfRcFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.also {
            it.launchWhenStarted {
                viewModel.getFavoriteItems().collect {
                    favoriteAdapter.updateList(it)
                }
            }
        }
    }

    fun onFavClick(entity: ApodEntity) {
        viewModel.updateFavorite(entity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

