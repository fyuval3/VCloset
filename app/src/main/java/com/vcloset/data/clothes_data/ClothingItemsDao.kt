package com.vcloset.data.clothes_data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ClothingItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: ClothingItem)

    @Delete
    suspend fun removeItem(item: ClothingItem)

    @Query("SELECT * FROM ClothingItems_table WHERE itemType = :itemType AND coldOrWarm = :weather")
    fun getItems(itemType: String, weather: String): LiveData<List<ClothingItem>>

    @Query("SELECT * FROM ClothingItems_table WHERE itemType = :itemType")
    fun getItems(itemType: String): LiveData<List<ClothingItem>>
}