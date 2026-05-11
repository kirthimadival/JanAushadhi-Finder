package com.example.finder.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finder.data.MedicineRepository

class MedicineViewModelFactory(
    private val application: Application,
    private val repository: MedicineRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicineSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicineSearchViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
