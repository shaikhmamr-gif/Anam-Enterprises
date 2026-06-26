package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AnamDao {
    @Query("SELECT * FROM delivery_partners ORDER BY name ASC")
    fun getAllPartners(): Flow<List<DeliveryPartner>>

    @Query("SELECT * FROM delivery_partners WHERE id = :id")
    suspend fun getPartnerById(id: Int): DeliveryPartner?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartner(partner: DeliveryPartner): Long

    @Delete
    suspend fun deletePartner(partner: DeliveryPartner)

    @Query("SELECT * FROM bike_inventories ORDER BY bikeModel ASC")
    fun getAllBikes(): Flow<List<BikeInventory>>

    @Query("SELECT * FROM bike_inventories WHERE id = :id")
    suspend fun getBikeById(id: Int): BikeInventory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBike(bike: BikeInventory): Long

    @Delete
    suspend fun deleteBike(bike: BikeInventory)

    @Query("SELECT * FROM financial_records ORDER BY date DESC")
    fun getAllFinancialRecords(): Flow<List<FinancialRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancialRecord(record: FinancialRecord): Long

    @Delete
    suspend fun deleteFinancialRecord(record: FinancialRecord)
}
