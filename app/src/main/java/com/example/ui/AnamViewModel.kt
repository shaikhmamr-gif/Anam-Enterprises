package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AnamRepository
import com.example.data.AppDatabase
import com.example.data.BikeInventory
import com.example.data.DeliveryPartner
import com.example.data.FinancialRecord
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AnamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AnamRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AnamRepository(database.anamDao())
        
        // Seed database if it's empty
        viewModelScope.launch {
            repository.allPartners.first().let { partners ->
                if (partners.isEmpty()) {
                    seedSampleData()
                }
            }
        }
    }

    // Tab state: 0 = Home/Dashboard, 1 = Partners, 2 = Inventory, 3 = Financials, 4 = Reports
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(tab: Int) {
        _currentTab.value = tab
    }

    // Raw data flows
    val allPartners: StateFlow<List<DeliveryPartner>> = repository.allPartners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBikes: StateFlow<List<BikeInventory>> = repository.allBikes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFinancialRecords: StateFlow<List<FinancialRecord>> = repository.allFinancialRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter states for Partners
    private val _partnerSearchQuery = MutableStateFlow("")
    val partnerSearchQuery: StateFlow<String> = _partnerSearchQuery.asStateFlow()

    private val _selectedPartnerFilter = MutableStateFlow("All")
    val selectedPartnerFilter: StateFlow<String> = _selectedPartnerFilter.asStateFlow()

    fun updateSearchQuery(query: String) {
        _partnerSearchQuery.value = query
    }

    fun updatePartnerFilter(filter: String) {
        _selectedPartnerFilter.value = filter
    }

    // Filtered partners
    val filteredPartners: StateFlow<List<DeliveryPartner>> = combine(
        allPartners,
        _partnerSearchQuery,
        _selectedPartnerFilter
    ) { partners, query, filter ->
        partners.filter { partner ->
            val matchesQuery = partner.name.contains(query, ignoreCase = true) ||
                    partner.mobileNumber.contains(query) ||
                    partner.aadhaarNumber.contains(query)
            val matchesFilter = filter == "All" || partner.partnerType.equals(filter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active sheet/dialog states
    val activePartnerForDetails = MutableStateFlow<DeliveryPartner?>(null)
    val activeBikeForDetails = MutableStateFlow<BikeInventory?>(null)

    fun selectPartnerForDetails(partner: DeliveryPartner?) {
        activePartnerForDetails.value = partner
    }

    fun selectBikeForDetails(bike: BikeInventory?) {
        activeBikeForDetails.value = bike
    }

    // DB Operations
    fun savePartner(partner: DeliveryPartner) {
        viewModelScope.launch {
            repository.insertPartner(partner)
        }
    }

    fun deletePartner(partner: DeliveryPartner) {
        viewModelScope.launch {
            if (partner.rentedBikeId != null) {
                repository.unassignBikeFromPartner(partner.id)
            }
            repository.deletePartner(partner)
        }
    }

    fun saveBike(bike: BikeInventory) {
        viewModelScope.launch {
            repository.insertBike(bike)
        }
    }

    fun deleteBike(bike: BikeInventory) {
        viewModelScope.launch {
            allPartners.value.find { it.rentedBikeId == bike.id }?.let { partner ->
                repository.unassignBikeFromPartner(partner.id)
            }
            repository.deleteBike(bike)
        }
    }

    fun assignBike(partnerId: Int, bikeId: Int, depositPaid: Double) {
        viewModelScope.launch {
            repository.assignBikeToPartner(partnerId, bikeId, depositPaid)
        }
    }

    fun unassignBike(partnerId: Int) {
        viewModelScope.launch {
            repository.unassignBikeFromPartner(partnerId)
        }
    }

    // Financial Operations
    fun saveFinancialRecord(record: FinancialRecord) {
        viewModelScope.launch {
            repository.insertFinancialRecord(record)
        }
    }

    fun deleteFinancialRecord(record: FinancialRecord) {
        viewModelScope.launch {
            repository.deleteFinancialRecord(record)
        }
    }

    // Pre-populate sample database
    fun seedSampleData() {
        viewModelScope.launch {
            val sampleBikes = listOf(
                BikeInventory(
                    bikeModel = "Hero Splendor iSmart",
                    plateNumber = "KA-03-HA-1234",
                    weeklyRent = 700.0,
                    depositAmount = 2000.0,
                    status = "Rented",
                    rcNumber = "RC-KA03-2024-9128",
                    rcExpiryDate = "2039-05-15",
                    rcStatus = "Active",
                    rcOwnerName = "Anam Enterprises",
                    rcEngineNumber = "ENG-SPL-9283719",
                    rcChassisNumber = "CHA-SPL-827391827",
                    insurancePolicyNumber = "INS-NAV-9928310",
                    insuranceProvider = "National Insurance Co.",
                    insuranceExpiryDate = "2027-06-12",
                    insuranceStatus = "Active",
                    insurancePremium = 1450.0,
                    insuranceSumInsured = 55000.0
                ),
                BikeInventory(
                    bikeModel = "TVS XL 100 Comfort",
                    plateNumber = "KA-05-EY-8844",
                    weeklyRent = 500.0,
                    depositAmount = 1500.0,
                    status = "Rented",
                    rcNumber = "RC-KA05-2023-8821",
                    rcExpiryDate = "2038-08-20",
                    rcStatus = "Active",
                    rcOwnerName = "Anam Enterprises",
                    rcEngineNumber = "ENG-TVS-1192832",
                    rcChassisNumber = "CHA-TVS-009182736",
                    insurancePolicyNumber = "INS-DIG-8827312",
                    insuranceProvider = "Digit General Insurance",
                    insuranceExpiryDate = "2027-09-30",
                    insuranceStatus = "Active",
                    insurancePremium = 1200.0,
                    insuranceSumInsured = 42000.0
                ),
                BikeInventory(
                    bikeModel = "Honda Activa 6G",
                    plateNumber = "KA-01-JM-5678",
                    weeklyRent = 800.0,
                    depositAmount = 2500.0,
                    status = "Available",
                    rcNumber = "RC-KA01-2025-0012",
                    rcExpiryDate = "2040-02-10",
                    rcStatus = "Active",
                    rcOwnerName = "Anam Enterprises",
                    rcEngineNumber = "ENG-ACT-445678",
                    rcChassisNumber = "CHA-ACT-11223344",
                    insurancePolicyNumber = "INS-ICI-5544321",
                    insuranceProvider = "ICICI Lombard",
                    insuranceExpiryDate = "2025-02-10", // expired
                    insuranceStatus = "Expired",
                    insurancePremium = 1800.0,
                    insuranceSumInsured = 65000.0
                ),
                BikeInventory(
                    bikeModel = "Bajaj CT 110X",
                    plateNumber = "KA-51-Q-9911",
                    weeklyRent = 650.0,
                    depositAmount = 1800.0,
                    status = "Available",
                    rcNumber = "RC-KA51-2024-4411",
                    rcExpiryDate = "2039-11-25",
                    rcStatus = "Active",
                    rcOwnerName = "Anam Enterprises",
                    rcEngineNumber = "ENG-CT110-8877",
                    rcChassisNumber = "CHA-CT110-998877",
                    insurancePolicyNumber = "INS-HDF-221199",
                    insuranceProvider = "HDFC ERGO",
                    insuranceExpiryDate = "2027-11-24",
                    insuranceStatus = "Active",
                    insurancePremium = 1350.0,
                    insuranceSumInsured = 48000.0
                )
            )
            val bikeIds = sampleBikes.map { repository.insertBike(it).toInt() }

            val samplePartners = listOf(
                DeliveryPartner(
                    name = "Amit Kumar",
                    mobileNumber = "9876543210",
                    partnerType = "BigBasket",
                    aadhaarNumber = "123456789012",
                    panNumber = "ABCDE1234F",
                    bankName = "State Bank of India",
                    bankAccountNo = "10002223334",
                    bankIfsc = "SBIN0001234",
                    rentedBikeId = bikeIds[0],
                    depositPaid = 2000.0,
                    rentStartDate = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000L), // 14 days ago
                    notes = "Experienced delivery rider. High rating on BigBasket."
                ),
                DeliveryPartner(
                    name = "Suresh Raina",
                    mobileNumber = "9123456789",
                    partnerType = "Zepto",
                    aadhaarNumber = "987654321098",
                    panNumber = "XYZWP5678G",
                    bankName = "HDFC Bank",
                    bankAccountNo = "50100234567",
                    bankIfsc = "HDFC0000123",
                    rentedBikeId = bikeIds[1],
                    depositPaid = 1500.0,
                    rentStartDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L), // 7 days ago
                    notes = "Requires a reliable bike for heavy Zepto grocery shifts."
                ),
                DeliveryPartner(
                    name = "Mohammad Shafi",
                    mobileNumber = "9988776655",
                    partnerType = "Myntra",
                    aadhaarNumber = "112233445566",
                    panNumber = "PLMOK2233Z",
                    bankName = "ICICI Bank",
                    bankAccountNo = "001205001122",
                    bankIfsc = "ICIC0000012",
                    notes = "Myntra fashion courier. Needs high cargo capacity bike."
                )
            )
            val partnerIds = samplePartners.map { repository.insertPartner(it).toInt() }

            // Let's seed financial records for the current and previous month to have gorgeous dashboards and reports
            val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            val currentMonth = sdf.format(Date())
            
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            val prevMonth = sdf.format(calendar.time)

            val sampleFinancials = listOf(
                // Security Deposits Received
                FinancialRecord(
                    type = "DEPOSIT_RECEIVED",
                    category = "Bike Rental",
                    partnerId = partnerIds[0],
                    partnerName = "Amit Kumar",
                    amount = 2000.0,
                    date = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000L),
                    description = "Security Deposit received for renting Hero Splendor iSmart",
                    monthYear = currentMonth
                ),
                FinancialRecord(
                    type = "DEPOSIT_RECEIVED",
                    category = "Bike Rental",
                    partnerId = partnerIds[1],
                    partnerName = "Suresh Raina",
                    amount = 1500.0,
                    date = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L),
                    description = "Security Deposit received for renting TVS XL 100 Comfort",
                    monthYear = currentMonth
                ),
                
                // Last month's bike rental income
                FinancialRecord(
                    type = "INCOME",
                    category = "Bike Rental",
                    partnerId = partnerIds[0],
                    partnerName = "Amit Kumar",
                    amount = 2800.0, // 4 weeks of rent at 700/wk
                    date = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L),
                    description = "Weekly bike rental payment (4 Weeks) - Hero Splendor",
                    monthYear = prevMonth
                ),
                FinancialRecord(
                    type = "INCOME",
                    category = "Bike Rental",
                    partnerId = partnerIds[1],
                    partnerName = "Suresh Raina",
                    amount = 2000.0, // 4 weeks of rent at 500/wk
                    date = System.currentTimeMillis() - (32 * 24 * 60 * 60 * 1000L),
                    description = "Weekly bike rental payment (4 Weeks) - TVS XL 100",
                    monthYear = prevMonth
                ),

                // Current month's bike rental income
                FinancialRecord(
                    type = "INCOME",
                    category = "Bike Rental",
                    partnerId = partnerIds[0],
                    partnerName = "Amit Kumar",
                    amount = 1400.0, // 2 weeks of rent
                    date = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000L),
                    description = "Weekly bike rental payment (2 Weeks) - Hero Splendor",
                    monthYear = currentMonth
                ),
                FinancialRecord(
                    type = "INCOME",
                    category = "Bike Rental",
                    partnerId = partnerIds[1],
                    partnerName = "Suresh Raina",
                    amount = 500.0, // 1 week of rent
                    date = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L),
                    description = "Weekly bike rental payment (1 Week) - TVS XL 100",
                    monthYear = currentMonth
                ),

                // Revenue generated by delivery services (Anam's client commissions / platform fees)
                FinancialRecord(
                    type = "INCOME",
                    category = "BigBasket",
                    partnerId = null,
                    partnerName = "BigBasket Corporate",
                    amount = 15000.0,
                    date = System.currentTimeMillis() - (28 * 24 * 60 * 60 * 1000L),
                    description = "BigBasket logistics commission for May 2026",
                    monthYear = prevMonth
                ),
                FinancialRecord(
                    type = "INCOME",
                    category = "Zepto",
                    partnerId = null,
                    partnerName = "Zepto Corporate",
                    amount = 18000.0,
                    date = System.currentTimeMillis() - (27 * 24 * 60 * 60 * 1000L),
                    description = "Zepto delivery agency commission for May 2026",
                    monthYear = prevMonth
                ),
                FinancialRecord(
                    type = "INCOME",
                    category = "Myntra",
                    partnerId = null,
                    partnerName = "Myntra Corporate",
                    amount = 12000.0,
                    date = System.currentTimeMillis() - (26 * 24 * 60 * 60 * 1000L),
                    description = "Myntra courier agency payout for May 2026",
                    monthYear = prevMonth
                ),

                // Current Month Commission Incomes
                FinancialRecord(
                    type = "INCOME",
                    category = "BigBasket",
                    partnerId = null,
                    partnerName = "BigBasket Corporate",
                    amount = 8500.0,
                    date = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000L),
                    description = "BigBasket mid-month agency fees",
                    monthYear = currentMonth
                ),
                FinancialRecord(
                    type = "INCOME",
                    category = "Zepto",
                    partnerId = null,
                    partnerName = "Zepto Corporate",
                    amount = 9200.0,
                    date = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000L),
                    description = "Zepto logistics commission",
                    monthYear = currentMonth
                ),
                FinancialRecord(
                    type = "INCOME",
                    category = "Myntra",
                    partnerId = null,
                    partnerName = "Myntra Corporate",
                    amount = 6100.0,
                    date = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000L),
                    description = "Myntra courier services commission",
                    monthYear = currentMonth
                ),

                // Payouts to partners (Expenses)
                FinancialRecord(
                    type = "EXPENSE",
                    category = "BigBasket",
                    partnerId = partnerIds[0],
                    partnerName = "Amit Kumar",
                    amount = 12000.0,
                    date = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000L),
                    description = "Monthly delivery rider payout - BigBasket",
                    monthYear = prevMonth
                ),
                FinancialRecord(
                    type = "EXPENSE",
                    category = "Zepto",
                    partnerId = partnerIds[1],
                    partnerName = "Suresh Raina",
                    amount = 14500.0,
                    date = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000L),
                    description = "Monthly delivery rider payout - Zepto",
                    monthYear = prevMonth
                ),
                FinancialRecord(
                    type = "EXPENSE",
                    category = "Myntra",
                    partnerId = partnerIds[2],
                    partnerName = "Mohammad Shafi",
                    amount = 9500.0,
                    date = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000L),
                    description = "Monthly delivery rider payout - Myntra",
                    monthYear = prevMonth
                ),

                // Current month payouts
                FinancialRecord(
                    type = "EXPENSE",
                    category = "BigBasket",
                    partnerId = partnerIds[0],
                    partnerName = "Amit Kumar",
                    amount = 6000.0,
                    date = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L),
                    description = "Rider fortnightly payout - BigBasket",
                    monthYear = currentMonth
                ),
                FinancialRecord(
                    type = "EXPENSE",
                    category = "Zepto",
                    partnerId = partnerIds[1],
                    partnerName = "Suresh Raina",
                    amount = 7200.0,
                    date = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L),
                    description = "Rider fortnightly payout - Zepto",
                    monthYear = currentMonth
                ),
                FinancialRecord(
                    type = "EXPENSE",
                    category = "Myntra",
                    partnerId = partnerIds[2],
                    partnerName = "Mohammad Shafi",
                    amount = 4800.0,
                    date = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000L),
                    description = "Rider fortnightly payout - Myntra",
                    monthYear = currentMonth
                )
            )

            sampleFinancials.forEach { repository.insertFinancialRecord(it) }
        }
    }
}
