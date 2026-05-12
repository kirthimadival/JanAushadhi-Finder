package com.example.finder.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finder.data.Medicine
import com.example.finder.data.MedicineReminder
import com.example.finder.data.MedicineRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MedicineSearchViewModel(
    application: Application,
    private val repository: MedicineRepository
) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("finder_prefs", Context.MODE_PRIVATE)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMedicine = MutableStateFlow<Medicine?>(null)
    val selectedMedicine: StateFlow<Medicine?> = _selectedMedicine.asStateFlow()

    private val _totalSavings = MutableStateFlow(prefs.getFloat("total_savings", 0f).toDouble())
    val totalSavings: StateFlow<Double> = _totalSavings.asStateFlow()

    // Persistent reminders from Room database
    val reminders: StateFlow<List<MedicineReminder>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResults: StateFlow<List<Medicine>> = _searchQuery
        .debounce(100)
        .flatMapLatest { query ->
            if (query.isEmpty()) flowOf(emptyList()) else repository.searchMedicines(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isEmpty()) _selectedMedicine.value = null
    }

    fun selectMedicine(medicine: Medicine) {
        _selectedMedicine.value = medicine
    }

    fun clearSelection() {
        _selectedMedicine.value = null
    }

    fun addSavings(amount: Double) {
        val newTotal = _totalSavings.value + amount
        _totalSavings.value = newTotal
        prefs.edit().putFloat("total_savings", newTotal.toFloat()).apply()
        _selectedMedicine.value = null
        _searchQuery.value = ""
    }

    fun addReminder(name: String, date: String) {
        if (name.isNotBlank() && date.isNotBlank()) {
            viewModelScope.launch {
                repository.addReminder(MedicineReminder(name = name, date = date))
            }
        }
    }

    fun deleteReminder(reminder: MedicineReminder) {
        viewModelScope.launch {
            repository.removeReminder(reminder)
        }
    }
}
