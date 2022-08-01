package com.vcloset.data.clothes_data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ClothingItems_table")
data class ClothingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String?,
    val description: String?,
    val itemType: String?,
    val coldOrWarm: String?
)

    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClothingItem> {
        override fun createFromParcel(parcel: Parcel): ClothingItem {
            return ClothingItem(parcel)
        }

        override fun newArray(size: Int): Array<ClothingItem?> {
            return arrayOfNulls(size)
        }
    }
}
