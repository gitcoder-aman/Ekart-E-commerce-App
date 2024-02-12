package com.tech.ekart.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.tech.ekart.data.Category
import com.tech.ekart.viewmodel.CategoryViewModel

class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category : Category
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category) as T
    }
}