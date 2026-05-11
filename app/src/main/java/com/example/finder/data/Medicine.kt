package com.example.finder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brandedName: String,
    val genericName: String,
    val salt: String,
    val usage: String,
    val brandedPrice: Double,
    val genericPrice: Double
) {
    val savings: Double
        get() = brandedPrice - genericPrice

    val savingsPercentage: Double
        get() = (savings / brandedPrice) * 100
}
