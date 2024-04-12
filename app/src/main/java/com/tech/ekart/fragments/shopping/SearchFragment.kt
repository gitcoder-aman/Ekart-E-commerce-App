package com.tech.ekart.fragments.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tech.ekart.R
import com.tech.ekart.adapters.SearchProductAdapter
import com.tech.ekart.databinding.FragmentSearchBinding
import com.tech.ekart.util.Resource
import com.tech.ekart.util.VerticalItemDecoration
import com.tech.ekart.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search){

    private lateinit var binding : FragmentSearchBinding
    private val searchProductAdapter by lazy { SearchProductAdapter() }
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchRv()

        searchProductAdapter.onSearchProductClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment,b)
        }
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.edSearchView.text.toString().trim()
            viewModel.getSearchResult(searchText)
        }
        binding.tvAccessory.setOnClickListener {
            viewModel.getSearchResult(binding.tvAccessory.text.toString().trim())
        }
        binding.tvCupBoard.setOnClickListener {
            viewModel.getSearchResult(binding.tvCupBoard.text.toString().trim())
        }
        binding.tvChair.setOnClickListener {
            viewModel.getSearchResult(binding.tvChair.text.toString().trim())
        }
        binding.tvFurniture.setOnClickListener {
            viewModel.getSearchResult(binding.tvFurniture.text.toString().trim())
        }

        binding.imageCloseSearch.setOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.searchResult.collectLatest {
                    when(it){
                        is Resource.Loading->{
                            binding.progressbarSearch.visibility = View.VISIBLE
                        }
                        is Resource.Success->{
                            Log.d("searchFrag", "onViewCreated: ${it.data?.size}")
                            binding.progressbarSearch.visibility = View.GONE
                            if(it.data.isNullOrEmpty()){
                                showNotFoundPage()
                                hideOtherViews()
                            }else{
                                hideNotFoundPage()
                                showOtherViews()
                                searchProductAdapter.notifyDataSetChanged()
                                searchProductAdapter.differ.submitList(it.data)
                            }
                        }
                        is Resource.Error->{
                            binding.progressbarSearch.visibility = View.GONE
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun hideOtherViews() {
        binding.tvSearchRv.visibility = View.GONE
        binding.tvDiscoverText.visibility = View.GONE
        binding.discoverMoreLayout.visibility = View.GONE
        binding.view1.visibility = View.GONE
    }

    private fun showOtherViews() {
        binding.tvSearchRv.visibility = View.VISIBLE
        binding.tvDiscoverText.visibility = View.VISIBLE
        binding.discoverMoreLayout.visibility = View.VISIBLE
        binding.view1.visibility = View.VISIBLE
    }

    private fun hideNotFoundPage() {
        binding.layoutSearchNotFound.visibility = View.GONE
    }

    private fun showNotFoundPage() {
        binding.layoutSearchNotFound.visibility = View.VISIBLE
    }

    private fun setupSearchRv() {
        binding.tvSearchRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = searchProductAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}