package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bike_inventories")
data class BikeInventory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bikeModel: String,
    val plateNumber: String,
    val weeklyRent: Double,
    val depositAmount: Double,
    val status: String = "Available", // "Available", "Rented", "Maintenance"
    val notes: String = "",
    val rcNumber: String = "",
    val rcExpiryDate: String = "",
    val rcStatus: String = "Active",
    val rcOwnerName: String = "",
    val rcEngineNumber: String = "",
    val rcChassisNumber: String = "",
    val insurancePolicyNumber: String = "",
    val insuranceProvider: String = "",
    val insuranceExpiryDate: String = "",
    val insuranceStatus: String = "Active",
    val insurancePremium: Double = 0.0,
    val insuranceSumInsured: Double = 0.0
)
