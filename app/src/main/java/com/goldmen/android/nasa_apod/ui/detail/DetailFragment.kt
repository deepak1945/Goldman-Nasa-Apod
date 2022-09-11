package com.goldmen.android.nasa_apod.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.goldmen.android.nasa_apod.R
import com.goldmen.android.nasa_apod.databinding.DetailFragmentBinding
import com.goldmen.android.nasa_apod.model.ApodEntity

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DetailFragmentBinding.bind(view)
        setupBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBinding() {
        arguments?.getParcelable<ApodEntity>("apod")?.let {
            binding.dfTitle.text = it.title ?: getString(R.string.mainErrorUnknown)
            binding.dfCopyright.text = it.copyright ?: getString(R.string.mainErrorUnknown)
            binding.dfDate.text = it.date ?: getString(R.string.mainErrorUnknown)
            binding.dfExplanation.text = it.explanation ?: getString(R.string.mainErrorUnknown)
            Glide.with(binding.root)
                .load(it.hdurl)
                .into(binding.dfImage)
        }
    }
}