package com.vcloset.data.clothes_data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OutfitItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOutfit(outfit: OutfitItem)

    @Delete
    suspend fun removeOutfit(outfit: OutfitItem)

    @Query("SELECT * FROM Outfits_table")
    fun getAllOutfits(): LiveData<List<OutfitItem>>
}