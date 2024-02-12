package com.tech.ekart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.tech.ekart.data.Category
import com.tech.ekart.data.Product
import com.tech.ekart.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }
    private fun fetchOfferProducts() {
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null).get().addOnSuccessListener {
            val products = it.toObjects(Product::class.java)
            viewModelScope.launch {
                _offerProducts.emit(Resource.Success(products))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _offerProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null).get().addOnSuccessListener {
            val products = it.toObjects(Product::class.java)
            viewModelScope.launch {
                _bestProducts.emit(Resource.Success(products))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}