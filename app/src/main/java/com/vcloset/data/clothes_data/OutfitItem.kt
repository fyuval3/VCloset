package com.vcloset.data.clothes_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Outfits_table")
data class OutfitItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val shirtDescription: String?,
    val pantsDescription: String?,
    val shirtImage: String?,
    val pantsImage: String?,
    val coldOrWarm: String?
)
