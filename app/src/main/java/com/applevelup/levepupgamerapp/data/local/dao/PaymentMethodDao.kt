package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.applevelup.levepupgamerapp.data.local.entity.PaymentMethodEntity

@Dao
interface PaymentMethodDao {

    @Insert
    suspend fun insert(method: PaymentMethodEntity): Long

    @Query("SELECT * FROM payment_methods ORDER BY isDefault DESC, createdAt DESC")
    suspend fun getAll(): List<PaymentMethodEntity>

    @Query("SELECT * FROM payment_methods WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): PaymentMethodEntity?

    @Query("DELETE FROM payment_methods WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE payment_methods SET isDefault = 0")
    suspend fun clearDefaults()

    @Query("UPDATE payment_methods SET isDefault = 1 WHERE id = :id")
    suspend fun markAsDefault(id: Long)

    @Query("DELETE FROM payment_methods")
    suspend fun clearAll()

    @Transaction
    suspend fun setDefault(id: Long) {
        clearDefaults()
        markAsDefault(id)
    }
}
