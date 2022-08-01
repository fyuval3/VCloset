package com.vcloset.data

import com.vcloset.data.clothes_data.ClothingItem
import com.vcloset.data.clothes_data.ClothingItemsDao
import com.vcloset.data.clothes_data.OutfitItem
import com.vcloset.data.clothes_data.OutfitItemsDao
import com.vcloset.data.weather_service.WeatherRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val clothingItemsDao: ClothingItemsDao,
    private val outfitItemsDao: OutfitItemsDao
) {

    suspend fun getTemp(latitude: Double, longitude: Double) =
        weatherRemoteDataSource.getTemp(latitude, longitude)

    fun getClothingItems(itemType: String, weather: String) =
        clothingItemsDao.getItems(itemType, weather)

    fun getClothingItems(itemType: String) = clothingItemsDao.getItems(itemType)

    fun getAllOutfits() = outfitItemsDao.getAllOutfits()

    suspend fun addClothingItem(item: ClothingItem) = clothingItemsDao.addItem(item)

    suspend fun addOutfitItem(outfit: OutfitItem) = outfitItemsDao.addOutfit(outfit)

    suspend fun removeClothingItem(item: ClothingItem) = clothingItemsDao.removeItem(item)

    suspend fun removeOutfitItem(outfit: OutfitItem) = outfitItemsDao.removeOutfit(outfit)
}