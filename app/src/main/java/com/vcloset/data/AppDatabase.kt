package com.vcloset.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vcloset.data.clothes_data.ClothingItem
import com.vcloset.data.clothes_data.ClothingItemsDao
import com.vcloset.data.clothes_data.OutfitItem
import com.vcloset.data.clothes_data.OutfitItemsDao

@Database(entities = [ClothingItem::class, OutfitItem::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun clothingItemsDao(): ClothingItemsDao

    abstract fun outfitItemsDao(): OutfitItemsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}