package com.vcloset.data.clothes_data

import androidx.lifecycle.*
import com.vcloset.data.AppRepository
import com.vcloset.data.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothingItemsViewModel @Inject constructor(private val repository: AppRepository) :
    ViewModel() {

    fun getShirts(weather: String) = repository.getClothingItems(Constants.SHIRT, weather)
    fun getPants(weather: String) = repository.getClothingItems(Constants.PANTS, weather)
    fun getAllShirts() = repository.getClothingItems(Constants.SHIRT)
    fun getAllPants() = repository.getClothingItems(Constants.PANTS)

    fun addItem(item: ClothingItem) {
        viewModelScope.launch {
            repository.addClothingItem(item)
        }
    }

    fun removeItem(item: ClothingItem) {
        viewModelScope.launch {
            repository.removeClothingItem(item)
        }
    }
}