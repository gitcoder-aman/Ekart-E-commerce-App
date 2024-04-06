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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tech.ekart.R
import com.tech.ekart.adapters.AddressAdapter
import com.tech.ekart.adapters.BillingProductAdapter
import com.tech.ekart.data.CartProduct
import com.tech.ekart.databinding.FragmentBillingBinding
import com.tech.ekart.util.HorizontalItemDecoration
import com.tech.ekart.util.Resource
import com.tech.ekart.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding : FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductAdapter() }
    private val viewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products = args.products.toList()
        totalPrice = args.totalPrice

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBillingProductRv()
        setupAddressRv()

        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.address.collectLatest {
                    when(it){
                        is Resource.Loading->{
                            binding.progressbarAddress.visibility = View.VISIBLE
                        }
                        is Resource.Success-> {
                            addressAdapter.differ.submitList(it.data)
                            binding.progressbarAddress.visibility = View.GONE
                        }
                        is Resource.Error->{
                            binding.progressbarAddress.visibility = View.GONE
                            Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                        }

                        else -> {Unit}
                    }

                }
            }
        }
        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = "$ $totalPrice"
        Log.d("billingFrag", "onCreate: $products")
        Log.d("billingFrag", "onCreate: $totalPrice")
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }
}