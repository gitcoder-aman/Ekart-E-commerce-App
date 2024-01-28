package com.tech.ekart.fragments.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tech.ekart.R
import com.tech.ekart.adapters.BestDealsAdapter
import com.tech.ekart.adapters.BestProductAdapter
import com.tech.ekart.adapters.SpecialProductAdapter
import com.tech.ekart.databinding.FragmentMainCategoryBinding
import com.tech.ekart.util.Resource
import com.tech.ekart.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()
    private lateinit var bestDealsAdapter : BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductRv()
        setupBestDealsRv()
        setupBestProductRv()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.specialProducts.collectLatest{
                    when (it) {
                        is Resource.Loading -> {
                            showLoading()
                        }

                        is Resource.Success -> {
                            specialProductAdapter.differ.submitList(it.data)
                            hideLoading()
                        }

                        is Resource.Error -> {
                            hideLoading()
                            Log.e("MainCategoryFragment", it.message.toString())
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestDealProducts.collectLatest{
                    when (it) {
                        is Resource.Loading -> {
                            showLoading()
                        }

                        is Resource.Success -> {
                            bestDealsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }

                        is Resource.Error -> {
                            hideLoading()
                            Log.e("MainCategoryFragment", it.message.toString())
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bestProducts.collectLatest{
                    when (it) {
                        is Resource.Loading -> {
                            showLoading()
                        }

                        is Resource.Success -> {
                            bestProductAdapter.differ.submitList(it.data)
                            hideLoading()
                        }

                        is Resource.Error -> {
                            hideLoading()
                            Log.e("MainCategoryFragment", it.message.toString())
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupBestProductRv() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProduct.apply {
            layoutManager =
                GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductRv() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }
    }
}