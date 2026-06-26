package com.example.data

import kotlinx.coroutines.flow.Flow

class AnamRepository(private val anamDao: AnamDao) {
    val allPartners: Flow<List<DeliveryPartner>> = anamDao.getAllPartners()
    val allBikes: Flow<List<BikeInventory>> = anamDao.getAllBikes()
    val allFinancialRecords: Flow<List<FinancialRecord>> = anamDao.getAllFinancialRecords()

    suspend fun getPartnerById(id: Int): DeliveryPartner? = anamDao.getPartnerById(id)
    suspend fun getBikeById(id: Int): BikeInventory? = anamDao.getBikeById(id)

    suspend fun insertPartner(partner: DeliveryPartner): Long = anamDao.insertPartner(partner)
    suspend fun deletePartner(partner: DeliveryPartner) = anamDao.deletePartner(partner)

    suspend fun insertBike(bike: BikeInventory): Long = anamDao.insertBike(bike)
    suspend fun deleteBike(bike: BikeInventory) = anamDao.deleteBike(bike)

    suspend fun insertFinancialRecord(record: FinancialRecord): Long = anamDao.insertFinancialRecord(record)
    suspend fun deleteFinancialRecord(record: FinancialRecord) = anamDao.deleteFinancialRecord(record)

    suspend fun assignBikeToPartner(partnerId: Int, bikeId: Int, depositPaid: Double) {
        val partner = anamDao.getPartnerById(partnerId)
        val bike = anamDao.getBikeById(bikeId)
        if (partner != null && bike != null) {
            anamDao.insertPartner(partner.copy(
                rentedBikeId = bikeId,
                depositPaid = depositPaid,
                rentStartDate = System.currentTimeMillis()
            ))
            anamDao.insertBike(bike.copy(status = "Rented"))

            val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
            val monthYear = sdf.format(java.util.Date())
            anamDao.insertFinancialRecord(FinancialRecord(
                type = "DEPOSIT_RECEIVED",
                category = "Bike Rental",
                partnerId = partnerId,
                partnerName = partner.name,
                amount = depositPaid,
                date = System.currentTimeMillis(),
                description = "Security Deposit received for renting ${bike.bikeModel} (${bike.plateNumber})",
                monthYear = monthYear
            ))
        }
    }

    suspend fun unassignBikeFromPartner(partnerId: Int) {
        val partner = anamDao.getPartnerById(partnerId)
        if (partner != null) {
            val rentedBikeId = partner.rentedBikeId
            if (rentedBikeId != null) {
                val bike = anamDao.getBikeById(rentedBikeId)
                if (bike != null) {
                    anamDao.insertBike(bike.copy(status = "Available"))

                    if (partner.depositPaid > 0) {
                        val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
                        val monthYear = sdf.format(java.util.Date())
                        anamDao.insertFinancialRecord(FinancialRecord(
                            type = "DEPOSIT_REFUNDED",
                            category = "Bike Rental",
                            partnerId = partnerId,
                            partnerName = partner.name,
                            amount = partner.depositPaid,
                            date = System.currentTimeMillis(),
                            description = "Security Deposit refunded for ${bike.bikeModel} (${bike.plateNumber})",
                            monthYear = monthYear
                        ))
                    }
                }
            }
            anamDao.insertPartner(partner.copy(
                rentedBikeId = null,
                rentStartDate = null,
                depositPaid = 0.0
            ))
        }
    }
}
