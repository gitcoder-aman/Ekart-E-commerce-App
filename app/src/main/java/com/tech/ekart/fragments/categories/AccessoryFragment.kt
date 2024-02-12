package com.tech.ekart.fragments.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.tech.ekart.data.Category
import com.tech.ekart.util.Resource
import com.tech.ekart.viewmodel.CategoryViewModel
import com.tech.ekart.viewmodel.factory.BaseCategoryViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccessoryFragment : BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    private val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Accessory)
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.offerProducts.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            showOfferLoading()
                        }

                        is Resource.Success -> {
                            offerAdapter.differ.submitList(it.data)
                            hideOfferLoading()
                        }

                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                it.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                            hideOfferLoading()
                        }

                        else -> Unit
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestProducts.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            showBestProductsLoading()
                        }

                        is Resource.Success -> {
                            bestProductAdapter.differ.submitList(it.data)
                            hideBestProductLoading()
                        }

                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                it.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                            hideBestProductLoading()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onBestProductsPagingRequest() {

    }

    override fun onOfferPagingRequest() {
    }
}