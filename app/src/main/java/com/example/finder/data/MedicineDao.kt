package com.example.finder.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines")
    fun getAllMedicines(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE brandedName LIKE '%' || :query || '%' OR genericName LIKE '%' || :query || '%' OR salt LIKE '%' || :query || '%'")
    fun searchMedicines(query: String): Flow<List<Medicine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<Medicine>)

    @Query("SELECT COUNT(*) FROM medicines")
    suspend fun getCount(): Int

    // Reminders
    @Query("SELECT * FROM reminders ORDER BY id DESC")
    fun getAllReminders(): Flow<List<MedicineReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: MedicineReminder)

    @Delete
    suspend fun deleteReminder(reminder: MedicineReminder)
}
