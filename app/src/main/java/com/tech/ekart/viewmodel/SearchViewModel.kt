package com.tech.ekart.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.tech.ekart.data.Product
import com.tech.ekart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

   private val _searchResult = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val searchResult = _searchResult.asStateFlow()

    private var searchResultList = mutableListOf<Product>()

    fun getSearchResult(searchName: String) {
        viewModelScope.launch {
            _searchResult.emit(Resource.Loading())
        }
        searchResultList.clear()
        firestore.collection("Products").addSnapshotListener { value, error ->
            if (error != null && value == null) {
                viewModelScope.launch {
                    _searchResult.emit(Resource.Error(error.message.toString()))
                }
            } else {
                val getSearchResult = value?.toObjects(Product::class.java)
                getSearchResult?.forEach {
                    if (it.name.lowercase(Locale.ROOT) == searchName.lowercase(Locale.ROOT) || it.category.lowercase(
                            Locale.ROOT
                        ) == searchName.lowercase(
                            Locale.ROOT
                        )
                    ) {
                        Log.d("searchViewModel", "onViewCreated: ${it.name}")

                        if (it.category != "Best Deals") {
                            searchResultList.add(it)
                        }
                    }
                }
                viewModelScope.launch {
                    _searchResult.emit(Resource.Success(searchResultList))
                }
            }
        }
    }
}