package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delivery_partners")
data class DeliveryPartner(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val mobileNumber: String,
    val partnerType: String, // "BigBasket", "Zepto", "Myntra", "Other"
    val aadhaarNumber: String,
    val panNumber: String,
    val bankName: String,
    val bankAccountNo: String,
    val bankIfsc: String,
    val rentedBikeId: Int? = null, // The ID of the bike rented, or null
    val depositPaid: Double = 0.0,
    val rentStartDate: Long? = null,
    val onboardingStatus: String = "Pending Documents", // "Pending Documents", "Under Review", "Approved", "Rejected"
    val aadhaarDocUri: String = "", // Holds file name if uploaded, e.g. "aadhaar_card.pdf"
    val panDocUri: String = "", // Holds file name if uploaded
    val bankDocUri: String = "", // Holds file name if uploaded
    val notes: String = ""
)
