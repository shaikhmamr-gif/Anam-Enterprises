package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_records")
data class FinancialRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "INCOME" or "EXPENSE" or "DEPOSIT_RECEIVED" or "DEPOSIT_REFUNDED"
    val category: String, // "Bike Rental", "BigBasket", "Zepto", "Myntra"
    val partnerId: Int?, // Linked delivery partner
    val partnerName: String, // Cached partner name for display stability
    val amount: Double,
    val date: Long, // Timestamp
    val description: String, // Details: e.g. "Rent for 2 weeks", "Payout for week 25", "Security Deposit"
    val monthYear: String // "YYYY-MM" format, e.g. "2026-06", used for monthly grouping
)
