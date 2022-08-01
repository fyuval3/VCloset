package com.vcloset.data.clothes_data

import androidx.lifecycle.*
import com.vcloset.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutfitsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun getOutfits() = repository.getAllOutfits()

    fun addItem(item: OutfitItem) {
        viewModelScope.launch {
            repository.addOutfitItem(item)
        }
    }

    fun removeItem(item: OutfitItem) {
        viewModelScope.launch {
            repository.removeOutfitItem(item)
        }
    }
}