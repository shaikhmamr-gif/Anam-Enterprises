package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.BikeInventory
import com.example.data.DeliveryPartner
import com.example.data.FinancialRecord
import com.example.ui.AnamViewModel
import com.example.ui.theme.AnamEnterprisesTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnamEnterprisesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: AnamViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val allPartners by viewModel.allPartners.collectAsStateWithLifecycle()
    val allBikes by viewModel.allBikes.collectAsStateWithLifecycle()
    val allFinancialRecords by viewModel.allFinancialRecords.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBarSection()
        },
        bottomBar = {
            BottomNavigationSection(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentTab) {
                0 -> DashboardTab(viewModel = viewModel)
                1 -> PartnersTab(viewModel = viewModel)
                2 -> InventoryTab(viewModel = viewModel)
                3 -> FinancialsTab(viewModel = viewModel)
                4 -> ReportsTab(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSection() {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Anam Enterprises",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "LOGISTICS & RENTALS PARTNER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(36.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "AE",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        },
        actions = {
            IconButton(
                onClick = { /* Could navigate to about / settings */ },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Account",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
        )
    )
}

@Composable
fun BottomNavigationSection(
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
    ) {
        NavigationBarItem(
            selected = currentTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.People, contentDescription = "Partners") },
            label = { Text("Partners", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.DirectionsBike, contentDescription = "Inventory") },
            label = { Text("Bikes", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Financials") },
            label = { Text("Ledger", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        NavigationBarItem(
            selected = currentTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.Assessment, contentDescription = "Reports") },
            label = { Text("Reports", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

// ==========================================
// TAB 0: DASHBOARD (HOME)
// ==========================================
@Composable
fun DashboardTab(
    viewModel: AnamViewModel,
    modifier: Modifier = Modifier
) {
    val partners by viewModel.allPartners.collectAsStateWithLifecycle()
    val bikes by viewModel.allBikes.collectAsStateWithLifecycle()
    val financials by viewModel.allFinancialRecords.collectAsStateWithLifecycle()

    val totalPartners = partners.size
    val activeRentedBikes = bikes.filter { it.status == "Rented" }.size
    val totalBikes = bikes.size

    // Calculate this month's financials
    val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val currentMonthStr = sdf.format(Date())

    val currentMonthTransactions = financials.filter { it.monthYear == currentMonthStr }
    val thisMonthRevenue = currentMonthTransactions.filter { it.type == "INCOME" }.sumOf { it.amount }
    val thisMonthExpenses = currentMonthTransactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val thisMonthDeposits = currentMonthTransactions.filter { it.type == "DEPOSIT_RECEIVED" }.sumOf { it.amount }
    val thisMonthNet = thisMonthRevenue - thisMonthExpenses

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Brand Promise Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OUR BRAND PROMISE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ANAM ENTERPRISES",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\"We promise not just to earn, but to grow together.\"",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Providing reliable delivery channels for top commerce platforms, and enabling sustainable livelihood through smart bike rentals.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Quick Stats row
        item {
            Text(
                text = "Operational Metrics",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$totalPartners",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total Partners",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBike,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$activeRentedBikes/$totalBikes",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bikes Rented",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Financial Overview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "This Month's Financials ($currentMonthStr)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = if (thisMonthNet >= 0) MaterialTheme.colorScheme.primary else Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Revenue",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${"%,.0f".format(thisMonthRevenue)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column {
                            Text(
                                text = "Payouts / Expenses",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${"%,.0f".format(thisMonthExpenses)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                        }
                        Column {
                            Text(
                                text = "Net Profit",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${"%,.0f".format(thisMonthNet)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (thisMonthNet >= 0) MaterialTheme.colorScheme.primary else Color.Red
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Security Deposits Held:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${"%,.0f".format(partners.sumOf { it.depositPaid })}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }

        // Platform breakdown
        item {
            Text(
                text = "Affiliated Platforms",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PlatformStatRow(
                    name = "BigBasket",
                    count = partners.count { it.partnerType == "BigBasket" },
                    color = Color(0xFF0284C7)
                )
                PlatformStatRow(
                    name = "Zepto",
                    count = partners.count { it.partnerType == "Zepto" },
                    color = Color(0xFF7C3AED)
                )
                PlatformStatRow(
                    name = "Myntra",
                    count = partners.count { it.partnerType == "Myntra" },
                    color = Color(0xFFDB2777)
                )
            }
        }
    }
}

@Composable
fun PlatformStatRow(
    name: String,
    count: Int,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$count Active Partners",
                    fontSize = 11.sp,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// TAB 1: DELIVERY PARTNERS MANAGEMENT
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnersTab(
    viewModel: AnamViewModel,
    modifier: Modifier = Modifier
) {
    val partners by viewModel.filteredPartners.collectAsStateWithLifecycle()
    val bikes by viewModel.allBikes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.partnerSearchQuery.collectAsStateWithLifecycle()
    val currentFilter by viewModel.selectedPartnerFilter.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    val activePartnerDetails by viewModel.activePartnerForDetails.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search & Filter header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search partners...", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("partner_search_input")
            )

            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                modifier = Modifier.testTag("new_partner_button")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Partner")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Category Filter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "BigBasket", "Zepto", "Myntra").forEach { category ->
                val selected = currentFilter == category
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.updatePartnerFilter(category) },
                    label = { Text(category, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Partner List
        if (partners.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No delivery partners found",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Try adjusting your search filters or add a new partner.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(partners, key = { it.id }) { partner ->
                    PartnerRow(
                        partner = partner,
                        bikes = bikes,
                        onClick = { viewModel.selectPartnerForDetails(partner) }
                    )
                }
            }
        }
    }

    // New Partner Onboarding Dialog
    if (showAddDialog) {
        PartnerFormDialog(
            onDismiss = { showAddDialog = false },
            onSave = { partner ->
                viewModel.savePartner(partner)
                showAddDialog = false
            }
        )
    }

    // Detail Sheet
    activePartnerDetails?.let { partner ->
        PartnerDetailsDialog(
            partner = partner,
            bikes = bikes,
            onDismiss = { viewModel.selectPartnerForDetails(null) },
            onAssignBike = { bikeId, deposit ->
                viewModel.assignBike(partner.id, bikeId, deposit)
                viewModel.selectPartnerForDetails(null)
            },
            onUnassignBike = {
                viewModel.unassignBike(partner.id)
                viewModel.selectPartnerForDetails(null)
            },
            onDelete = {
                viewModel.deletePartner(partner)
                viewModel.selectPartnerForDetails(null)
            }
        )
    }
}

@Composable
fun PartnerRow(
    partner: DeliveryPartner,
    bikes: List<BikeInventory>,
    onClick: () -> Unit
) {
    val rentedBike = partner.rentedBikeId?.let { bikeId -> bikes.find { it.id == bikeId } }
    val hasAadhaar = partner.aadhaarNumber.length >= 12
    val hasPan = partner.panNumber.length >= 10
    val hasBank = partner.bankAccountNo.isNotEmpty() && partner.bankIfsc.isNotEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("partner_row_${partner.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Initials Avatar
            val initials = partner.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase()
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = when (partner.partnerType) {
                            "BigBasket" -> Color(0xFF0284C7).copy(alpha = 0.15f)
                            "Zepto" -> Color(0xFF7C3AED).copy(alpha = 0.15f)
                            "Myntra" -> Color(0xFFDB2777).copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = when (partner.partnerType) {
                        "BigBasket" -> Color(0xFF0284C7)
                        "Zepto" -> Color(0xFF7C3AED)
                        "Myntra" -> Color(0xFFDB2777)
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = partner.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    // Partner type label
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (partner.partnerType) {
                                    "BigBasket" -> Color(0xFF0284C7).copy(alpha = 0.15f)
                                    "Zepto" -> Color(0xFF7C3AED).copy(alpha = 0.15f)
                                    "Myntra" -> Color(0xFFDB2777).copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = partner.partnerType,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = when (partner.partnerType) {
                                "BigBasket" -> Color(0xFF0284C7)
                                "Zepto" -> Color(0xFF7C3AED)
                                "Myntra" -> Color(0xFFDB2777)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Mob: ${partner.mobileNumber}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Documents Checklist indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DocumentBadge(label = "AADHAAR", verified = hasAadhaar)
                    DocumentBadge(label = "PAN", verified = hasPan)
                    DocumentBadge(label = "BANK", verified = hasBank)
                    
                    if (rentedBike != null) {
                        Spacer(modifier = Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = rentedBike.bikeModel.split(" ").firstOrNull() ?: "Rented",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentBadge(label: String, verified: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (verified) Color(0xFFD1FAE5) else Color(0xFFFEE2E2),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 5.dp, vertical = 1.5.dp)
    ) {
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            color = if (verified) Color(0xFF065F46) else Color(0xFF991B1B)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerFormDialog(
    onDismiss: () -> Unit,
    onSave: (DeliveryPartner) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var partnerType by remember { mutableStateOf("BigBasket") }
    var aadhaarNumber by remember { mutableStateOf("") }
    var panNumber by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var bankAccountNo by remember { mutableStateOf("") }
    var bankIfsc by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val platformOptions = listOf("BigBasket", "Zepto", "Myntra", "Other")
    var expandedDropdown by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "New Partner Registration",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Divider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Partner Full Name *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("partner_form_name")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { mobileNumber = it },
                            label = { Text("Mobile Number *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("partner_form_mobile")
                        )
                    }

                    item {
                        ExposedDropdownMenuBox(
                            expanded = expandedDropdown,
                            onExpandedChange = { expandedDropdown = !expandedDropdown }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = partnerType,
                                onValueChange = {},
                                label = { Text("Delivery Platform *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                platformOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            partnerType = option
                                            expandedDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Registration Documents",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = aadhaarNumber,
                            onValueChange = { if (it.length <= 12) aadhaarNumber = it },
                            label = { Text("Aadhaar Card No. (12 digits)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("partner_form_aadhaar")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = panNumber,
                            onValueChange = { if (it.length <= 10) panNumber = it.uppercase() },
                            label = { Text("PAN Card No.") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("partner_form_pan")
                        )
                    }

                    item {
                        Text(
                            text = "Bank Details for Payouts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = bankName,
                            onValueChange = { bankName = it },
                            label = { Text("Bank Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = bankAccountNo,
                            onValueChange = { bankAccountNo = it },
                            label = { Text("Account Number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("partner_form_account")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = bankIfsc,
                            onValueChange = { bankIfsc = it.uppercase() },
                            label = { Text("IFSC Code") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("partner_form_ifsc")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Verification Notes / Bio") },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (name.trim().isNotEmpty() && mobileNumber.trim().isNotEmpty()) {
                                onSave(
                                    DeliveryPartner(
                                        name = name.trim(),
                                        mobileNumber = mobileNumber.trim(),
                                        partnerType = partnerType,
                                        aadhaarNumber = aadhaarNumber.trim(),
                                        panNumber = panNumber.trim(),
                                        bankName = bankName.trim(),
                                        bankAccountNo = bankAccountNo.trim(),
                                        bankIfsc = bankIfsc.trim(),
                                        notes = notes.trim()
                                    )
                                )
                            }
                        },
                        enabled = name.isNotEmpty() && mobileNumber.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Register")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailsDialog(
    partner: DeliveryPartner,
    bikes: List<BikeInventory>,
    onDismiss: () -> Unit,
    onAssignBike: (Int, Double) -> Unit,
    onUnassignBike: () -> Unit,
    onDelete: () -> Unit
) {
    val rentedBike = partner.rentedBikeId?.let { id -> bikes.find { it.id == id } }
    val availableBikes = bikes.filter { it.status == "Available" }
    
    var showAssignSection by remember { mutableStateOf(false) }
    var selectedBikeId by remember { mutableStateOf<Int?>(null) }
    var depositPaidInput by remember { mutableStateOf("") }
    var expandedBikeDropdown by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = partner.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Platform: ${partner.partnerType}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Partner", tint = Color.Red)
                    }
                }

                Divider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .heightIn(max = 340.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        // Contact Details Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = partner.mobileNumber, fontSize = 14.sp)
                        }
                    }

                    item {
                        // Documents Verification
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("REGISTRATION DOCUMENTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(
                                        imageVector = if (partner.aadhaarNumber.length >= 12) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (partner.aadhaarNumber.length >= 12) Color(0xFF059669) else Color(0xFFD97706),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text("Aadhaar Document", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = if (partner.aadhaarNumber.isNotEmpty()) partner.aadhaarNumber else "Not Provided",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(
                                        imageVector = if (partner.panNumber.length >= 10) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (partner.panNumber.length >= 10) Color(0xFF059669) else Color(0xFFD97706),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text("PAN Card", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = if (partner.panNumber.isNotEmpty()) partner.panNumber else "Not Provided",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // Bank Account Info
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("BANK PAYOUT DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                if (partner.bankAccountNo.isNotEmpty()) {
                                    Text("Bank: ${partner.bankName}", fontSize = 12.sp)
                                    Text("A/C No: ${partner.bankAccountNo}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text("IFSC: ${partner.bankIfsc}", fontSize = 12.sp)
                                } else {
                                    Text("No Bank Details registered. Payouts cannot be made.", fontSize = 12.sp, color = Color.Red)
                                }
                            }
                        }
                    }

                    item {
                        // Rented Bike Details
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("BIKE RENTAL STATUS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(4.dp))
                                if (rentedBike != null) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = rentedBike.bikeModel, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "Plate: ${rentedBike.plateNumber}", fontSize = 11.sp)
                                            Text(text = "Weekly Rent: ₹${rentedBike.weeklyRent}", fontSize = 11.sp)
                                            Text(text = "Deposit Paid: ₹${partner.depositPaid}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                        Button(
                                            onClick = onUnassignBike,
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                                        ) {
                                            Text("Return", fontSize = 11.sp)
                                        }
                                    }
                                } else {
                                    Text("No rented bike assigned to this partner.", fontSize = 12.sp)
                                    
                                    if (!showAssignSection) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OutlinedButton(
                                            onClick = { showAssignSection = true },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.DirectionsBike, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Rent Bike to Partner", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (showAssignSection && rentedBike == null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("SELECT BIKE FROM FLEET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    
                                    if (availableBikes.isEmpty()) {
                                        Text("No bikes available in fleet. Please add bikes in Inventory first.", fontSize = 12.sp, color = Color.Red)
                                    } else {
                                        ExposedDropdownMenuBox(
                                            expanded = expandedBikeDropdown,
                                            onExpandedChange = { expandedBikeDropdown = !expandedBikeDropdown }
                                        ) {
                                            val selectedBike = bikes.find { it.id == selectedBikeId }
                                            val displayText = selectedBike?.let { "${it.bikeModel} (${it.plateNumber})" } ?: "Select Available Bike"
                                            
                                            OutlinedTextField(
                                                readOnly = true,
                                                value = displayText,
                                                onValueChange = {},
                                                label = { Text("Available Bikes") },
                                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBikeDropdown) },
                                                modifier = Modifier.fillMaxWidth().menuAnchor()
                                            )
                                            ExposedDropdownMenu(
                                                expanded = expandedBikeDropdown,
                                                onDismissRequest = { expandedBikeDropdown = false }
                                            ) {
                                                availableBikes.forEach { bike ->
                                                    DropdownMenuItem(
                                                        text = { Text("${bike.bikeModel} [${bike.plateNumber}] (₹${bike.weeklyRent}/wk)") },
                                                        onClick = {
                                                            selectedBikeId = bike.id
                                                            depositPaidInput = bike.depositAmount.toString()
                                                            expandedBikeDropdown = false
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        OutlinedTextField(
                                            value = depositPaidInput,
                                            onValueChange = { depositPaidInput = it },
                                            label = { Text("Deposit Paid (₹)") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = { showAssignSection = false },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Cancel", fontSize = 11.sp)
                                            }
                                            Button(
                                                onClick = {
                                                    selectedBikeId?.let { id ->
                                                        val dep = depositPaidInput.toDoubleOrNull() ?: 0.0
                                                        onAssignBike(id, dep)
                                                    }
                                                },
                                                enabled = selectedBikeId != null,
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Rent Out", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (partner.notes.isNotEmpty()) {
                        item {
                            Column {
                                Text("Verification Bio Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = partner.notes, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

// ==========================================
// TAB 2: BIKE INVENTORY
// ==========================================
@Composable
fun InventoryTab(
    viewModel: AnamViewModel,
    modifier: Modifier = Modifier
) {
    val bikes by viewModel.allBikes.collectAsStateWithLifecycle()
    val partners by viewModel.allPartners.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    val activeBikeDetails by viewModel.activeBikeForDetails.collectAsStateWithLifecycle()
    var showDocumentsBike by remember { mutableStateOf<BikeInventory?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Rental Fleet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${bikes.count { it.status == "Available" }} Available | ${bikes.count { it.status == "Rented" }} Rented",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_bike_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Bike")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bikes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBike,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No bikes in fleet",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Add vehicles to enable rental management and weekly income tracking.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bikes, key = { it.id }) { bike ->
                    BikeRow(
                        bike = bike,
                        partners = partners,
                        onClick = { viewModel.selectBikeForDetails(bike) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        BikeFormDialog(
            onDismiss = { showAddDialog = false },
            onSave = { bike ->
                viewModel.saveBike(bike)
                showAddDialog = false
            }
        )
    }

    activeBikeDetails?.let { bike ->
        BikeDetailsDialog(
            bike = bike,
            partners = partners,
            onDismiss = { viewModel.selectBikeForDetails(null) },
            onDelete = {
                viewModel.deleteBike(bike)
                viewModel.selectBikeForDetails(null)
            },
            onViewDocuments = {
                showDocumentsBike = bike
                viewModel.selectBikeForDetails(null)
            }
        )
    }

    showDocumentsBike?.let { bike ->
        BikeDocumentsDialog(
            bike = bike,
            onDismiss = { showDocumentsBike = null },
            onSave = { updated ->
                viewModel.saveBike(updated)
                showDocumentsBike = null
            }
        )
    }
}

@Composable
fun BikeRow(
    bike: BikeInventory,
    partners: List<DeliveryPartner>,
    onClick: () -> Unit
) {
    val currentRider = if (bike.status == "Rented") partners.find { it.rentedBikeId == bike.id } else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("bike_row_${bike.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = when (bike.status) {
                            "Available" -> Color(0xFFD1FAE5)
                            "Rented" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFFEE2E2)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBike,
                    contentDescription = null,
                    tint = when (bike.status) {
                        "Available" -> Color(0xFF059669)
                        "Rented" -> Color(0xFFD97706)
                        else -> Color(0xFFDC2626)
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bike.bikeModel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    // Status Badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (bike.status) {
                                    "Available" -> Color(0xFFD1FAE5)
                                    "Rented" -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFFEE2E2)
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(start = 6.dp, top = 2.dp, end = 6.dp, bottom = 2.dp)
                    ) {
                        Text(
                            text = bike.status.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = when (bike.status) {
                                "Available" -> Color(0xFF065F46)
                                "Rented" -> Color(0xFF92400E)
                                else -> Color(0xFF991B1B)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Plate: ${bike.plateNumber}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹${bike.weeklyRent.toInt()}/wk",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (currentRider != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Rider: ${currentRider.name} (${currentRider.partnerType})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BikeFormDialog(
    onDismiss: () -> Unit,
    onSave: (BikeInventory) -> Unit
) {
    var model by remember { mutableStateOf("") }
    var plateNumber by remember { mutableStateOf("") }
    var weeklyRent by remember { mutableStateOf("700") }
    var depositAmount by remember { mutableStateOf("2000") }
    var status by remember { mutableStateOf("Available") }
    var notes by remember { mutableStateOf("") }

    val statusOptions = listOf("Available", "Maintenance")
    var expandedDropdown by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Add Bike to Fleet",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = model,
                            onValueChange = { model = it },
                            label = { Text("Bike Model Name *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("bike_form_model")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = plateNumber,
                            onValueChange = { plateNumber = it.uppercase() },
                            label = { Text("Registration Number / Plate *") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("bike_form_plate")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = weeklyRent,
                            onValueChange = { weeklyRent = it },
                            label = { Text("Weekly Rent Fee (₹) *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = depositAmount,
                            onValueChange = { depositAmount = it },
                            label = { Text("Security Deposit Amount (₹) *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { expandedDropdown = !expandedDropdown },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Initial Status: $status")
                            }
                            DropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                statusOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            status = option
                                            expandedDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Vehicle Notes (e.g. Insurance info)") },
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (model.isNotEmpty() && plateNumber.isNotEmpty()) {
                                onSave(
                                    BikeInventory(
                                        bikeModel = model.trim(),
                                        plateNumber = plateNumber.trim(),
                                        weeklyRent = weeklyRent.toDoubleOrNull() ?: 700.0,
                                        depositAmount = depositAmount.toDoubleOrNull() ?: 2000.0,
                                        status = status,
                                        notes = notes.trim()
                                    )
                                )
                            }
                        },
                        enabled = model.isNotEmpty() && plateNumber.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add Bike")
                    }
                }
            }
        }
    }
}

@Composable
fun BikeDetailsDialog(
    bike: BikeInventory,
    partners: List<DeliveryPartner>,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onViewDocuments: () -> Unit
) {
    val currentRider = partners.find { it.rentedBikeId == bike.id }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = bike.bikeModel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Plate: ${bike.plateNumber}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }

                Divider()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Weekly Rental Rate:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${bike.weeklyRent}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Required Security Deposit:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${bike.depositAmount}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Fleet Status:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = bike.status,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (bike.status) {
                                "Available" -> Color(0xFF059669)
                                "Rented" -> Color(0xFFD97706)
                                else -> Color(0xFFDC2626)
                            }
                        )
                    }
                }

                if (currentRider != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("ACTIVE RIDER DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Name: ${currentRider.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Platform: ${currentRider.partnerType}", fontSize = 12.sp)
                            Text(text = "Contact: ${currentRider.mobileNumber}", fontSize = 12.sp)
                            Text(text = "Deposit Paid: ₹${currentRider.depositPaid}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Documents & Insurance Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onViewDocuments() }
                        .testTag("bike_details_documents_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Documents & Insurance",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // RC badge
                                    val hasRc = bike.rcNumber.isNotEmpty()
                                    Box(
                                        modifier = Modifier
                                            .background(if (hasRc) Color(0xFFD1FAE5) else Color(0xFFFEE2E2), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "RC: ${if (hasRc) "OK" else "MISSING"}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (hasRc) Color(0xFF065F46) else Color(0xFF991B1B)
                                        )
                                    }
                                    
                                    // Insurance badge
                                    val insStatus = bike.insuranceStatus
                                    val insExpired = insStatus == "Expired"
                                    Box(
                                        modifier = Modifier
                                            .background(if (insExpired) Color(0xFFFEE2E2) else Color(0xFFD1FAE5), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "INS: ${insStatus.uppercase()}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (insExpired) Color(0xFF991B1B) else Color(0xFF065F46)
                                        )
                                    }
                                }
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Open Documents",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (bike.notes.isNotEmpty()) {
                    Column {
                        Text("Vehicle Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = bike.notes, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

// ==========================================
// TAB 3: FINANCIAL LEDGER
// ==========================================
@Composable
fun FinancialsTab(
    viewModel: AnamViewModel,
    modifier: Modifier = Modifier
) {
    val records by viewModel.allFinancialRecords.collectAsStateWithLifecycle()
    val partners by viewModel.allPartners.collectAsStateWithLifecycle()

    var showRentDialog by remember { mutableStateOf(false) }
    var showPayoutDialog by remember { mutableStateOf(false) }
    var showMiscDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Quick Ledger Controls
        Text(
            text = "Record Financial Ledger Transactions",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showRentDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("record_rent_income_button"),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text("Bike Rent", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showPayoutDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier
                    .weight(1.5f)
                    .height(44.dp)
                    .testTag("record_payout_button"),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Partner Payout", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showMiscDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("record_misc_button"),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text("Misc Fee", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recent Transactions Ledger",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No financial transactions recorded yet.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(records, key = { it.id }) { record ->
                    LedgerRow(record = record, onDelete = { viewModel.deleteFinancialRecord(record) })
                }
            }
        }
    }

    // Rent Income dialog
    if (showRentDialog) {
        BikeRentIncomeDialog(
            partners = partners,
            bikes = viewModel.allBikes.value,
            onDismiss = { showRentDialog = false },
            onSave = { record ->
                viewModel.saveFinancialRecord(record)
                showRentDialog = false
            }
        )
    }

    // Partner Payout dialog
    if (showPayoutDialog) {
        PartnerPayoutDialog(
            partners = partners,
            onDismiss = { showPayoutDialog = false },
            onSave = { record ->
                viewModel.saveFinancialRecord(record)
                showPayoutDialog = false
            }
        )
    }

    // Misc entry dialog
    if (showMiscDialog) {
        MiscFinancialDialog(
            onDismiss = { showMiscDialog = false },
            onSave = { record ->
                viewModel.saveFinancialRecord(record)
                showMiscDialog = false
            }
        )
    }
}

@Composable
fun LedgerRow(
    record: FinancialRecord,
    onDelete: () -> Unit
) {
    val isIncome = record.type == "INCOME" || record.type == "DEPOSIT_RECEIVED"
    val isDeposit = record.type == "DEPOSIT_RECEIVED" || record.type == "DEPOSIT_REFUNDED"
    
    val displayColor = when {
        isDeposit -> MaterialTheme.colorScheme.tertiary
        isIncome -> Color(0xFF059669) // Green
        else -> Color(0xFFDC2626) // Red
    }

    val typeLabel = when (record.type) {
        "INCOME" -> "INCOME"
        "EXPENSE" -> "PAYOUT"
        "DEPOSIT_RECEIVED" -> "DEP RECV"
        "DEPOSIT_REFUNDED" -> "DEP REF"
        else -> record.type
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateStr = dateFormat.format(Date(record.date))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(displayColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(start = 6.dp, top = 2.dp, end = 6.dp, bottom = 2.dp)
                        ) {
                            Text(
                                text = typeLabel,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = displayColor
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = record.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = if (isIncome) "+₹${"%,.0f".format(record.amount)}" else "-₹${"%,.0f".format(record.amount)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = displayColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = record.description,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Partner: ${record.partnerName}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dateStr,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete transaction",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BikeRentIncomeDialog(
    partners: List<DeliveryPartner>,
    bikes: List<BikeInventory>,
    onDismiss: () -> Unit,
    onSave: (FinancialRecord) -> Unit
) {
    val rentingPartners = partners.filter { it.rentedBikeId != null }
    var selectedPartnerId by remember { mutableStateOf<Int?>(null) }
    var weeksCount by remember { mutableStateOf("1") }
    var description by remember { mutableStateOf("") }
    
    var expandedDropdown by remember { mutableStateOf(false) }

    val partner = rentingPartners.find { it.id == selectedPartnerId }
    val rentedBike = partner?.rentedBikeId?.let { id -> bikes.find { it.id == id } }
    
    val weeklyRate = rentedBike?.weeklyRent ?: 0.0
    val weeksNum = weeksCount.toIntOrNull() ?: 1
    val calculatedAmount = weeklyRate * weeksNum

    LaunchedEffect(selectedPartnerId, weeksCount) {
        if (partner != null && rentedBike != null) {
            description = "Weekly rental income from ${partner.name} for ${rentedBike.bikeModel} (${weeksCount} Week/s)"
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Record Bike Rent Income",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider()

                if (rentingPartners.isEmpty()) {
                    Text(
                        text = "There are currently no active delivery partners renting bikes. Please assign a bike to a partner first.",
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expandedDropdown,
                        onExpandedChange = { expandedDropdown = !expandedDropdown }
                    ) {
                        val displayText = partner?.let { "${it.name} (${it.partnerType})" } ?: "Select Renting Partner"
                        OutlinedTextField(
                            readOnly = true,
                            value = displayText,
                            onValueChange = {},
                            label = { Text("Delivery Partner *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false }
                        ) {
                            rentingPartners.forEach { rp ->
                                DropdownMenuItem(
                                    text = { Text("${rp.name} (${rp.partnerType})") },
                                    onClick = {
                                        selectedPartnerId = rp.id
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    if (rentedBike != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Rented: ${rentedBike.bikeModel}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Plate: ${rentedBike.plateNumber}", fontSize = 11.sp)
                                Text(text = "Weekly Rental Rate: ₹${weeklyRate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = weeksCount,
                        onValueChange = { weeksCount = it },
                        label = { Text("Rental Weeks Duration *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("rent_weeks_input")
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Internal Ledger Description") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL RECORDED INCOME:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("₹${"%,.0f".format(calculatedAmount)}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF059669))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            partner?.let { p ->
                                val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                                onSave(
                                    FinancialRecord(
                                        type = "INCOME",
                                        category = "Bike Rental",
                                        partnerId = p.id,
                                        partnerName = p.name,
                                        amount = calculatedAmount,
                                        date = System.currentTimeMillis(),
                                        description = description,
                                        monthYear = sdf.format(Date())
                                    )
                                )
                            }
                        },
                        enabled = selectedPartnerId != null && weeksNum > 0,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Record")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerPayoutDialog(
    partners: List<DeliveryPartner>,
    onDismiss: () -> Unit,
    onSave: (FinancialRecord) -> Unit
) {
    var selectedPartnerId by remember { mutableStateOf<Int?>(null) }
    var payoutAmount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    var expandedDropdown by remember { mutableStateOf(false) }

    val partner = partners.find { it.id == selectedPartnerId }

    LaunchedEffect(selectedPartnerId, payoutAmount) {
        if (partner != null) {
            description = "Earning payout to ${partner.name} for delivery duties on ${partner.partnerType}"
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Record Partner Payout (Expense)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider()

                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    val displayText = partner?.let { "${it.name} (${it.partnerType})" } ?: "Select Partner for Payout"
                    OutlinedTextField(
                        readOnly = true,
                        value = displayText,
                        onValueChange = {},
                        label = { Text("Delivery Partner *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        partners.forEach { rp ->
                            DropdownMenuItem(
                                text = { Text("${rp.name} (${rp.partnerType})") },
                                onClick = {
                                    selectedPartnerId = rp.id
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = payoutAmount,
                    onValueChange = { payoutAmount = it },
                    label = { Text("Payout Earning Amount (₹) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("payout_amount_input")
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Ledger Description") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val amt = payoutAmount.toDoubleOrNull() ?: 0.0
                            partner?.let { p ->
                                val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                                onSave(
                                    FinancialRecord(
                                        type = "EXPENSE",
                                        category = p.partnerType,
                                        partnerId = p.id,
                                        partnerName = p.name,
                                        amount = amt,
                                        date = System.currentTimeMillis(),
                                        description = description,
                                        monthYear = sdf.format(Date())
                                    )
                                )
                            }
                        },
                        enabled = selectedPartnerId != null && payoutAmount.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Disburse Payout")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiscFinancialDialog(
    onDismiss: () -> Unit,
    onSave: (FinancialRecord) -> Unit
) {
    var type by remember { mutableStateOf("INCOME") }
    var category by remember { mutableStateOf("Bike Rental") }
    var amount by remember { mutableStateOf("") }
    var partnerName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val categoryOptions = listOf("Bike Rental", "BigBasket", "Zepto", "Myntra")
    var expandedCat by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Record Miscellaneous Ledger Transaction",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider()

                // Income or Expense Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { type = "INCOME" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "INCOME") Color(0xFFD1FAE5) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("INCOME", color = if (type == "INCOME") Color(0xFF065F46) else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { type = "EXPENSE" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "EXPENSE") Color(0xFFFEE2E2) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("EXPENSE", color = if (type == "EXPENSE") Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    }
                }

                // Category Selection
                ExposedDropdownMenuBox(
                    expanded = expandedCat,
                    onExpandedChange = { expandedCat = !expandedCat }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = category,
                        onValueChange = {},
                        label = { Text("Service Segment Category *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCat,
                        onDismissRequest = { expandedCat = false }
                    ) {
                        categoryOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    category = opt
                                    expandedCat = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = partnerName,
                    onValueChange = { partnerName = it },
                    label = { Text("Associated Party / Entity") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description details *") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val amt = amount.toDoubleOrNull() ?: 0.0
                            if (amt > 0 && description.isNotEmpty()) {
                                val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                                onSave(
                                    FinancialRecord(
                                        type = type,
                                        category = category,
                                        partnerId = null,
                                        partnerName = if (partnerName.isEmpty()) "General Logistics" else partnerName,
                                        amount = amt,
                                        date = System.currentTimeMillis(),
                                        description = description.trim(),
                                        monthYear = sdf.format(Date())
                                    )
                                )
                            }
                        },
                        enabled = amount.isNotEmpty() && description.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Record Entry")
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 4: MONTHLY P&L SEGMENTED REPORTS
// ==========================================
@Composable
fun ReportsTab(
    viewModel: AnamViewModel,
    modifier: Modifier = Modifier
) {
    val records by viewModel.allFinancialRecords.collectAsStateWithLifecycle()

    // Extract available month-years
    val availableMonths = records.map { it.monthYear }.distinct().sortedDescending()
    
    var selectedMonth by remember { mutableStateOf("") }
    
    LaunchedEffect(availableMonths) {
        if (selectedMonth.isEmpty() && availableMonths.isNotEmpty()) {
            selectedMonth = availableMonths.first()
        }
    }

    val filteredRecords = records.filter { it.monthYear == selectedMonth }

    // Computations
    val totalRevenue = filteredRecords.filter { it.type == "INCOME" }.sumOf { it.amount }
    val totalExpense = filteredRecords.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val netProfit = totalRevenue - totalExpense

    val segments = listOf("Bike Rental", "BigBasket", "Zepto", "Myntra")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Month Selector Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Reporting Period", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Review segmented agency performance", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    var expandedMonthDropdown by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(
                            onClick = { expandedMonthDropdown = true },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = if (selectedMonth.isEmpty()) "Select Month" else selectedMonth, fontSize = 12.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expandedMonthDropdown,
                            onDismissRequest = { expandedMonthDropdown = false }
                        ) {
                            availableMonths.forEach { m ->
                                DropdownMenuItem(
                                    text = { Text(m) },
                                    onClick = {
                                        selectedMonth = m
                                        expandedMonthDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CONSOLIDATED PERFORMANCE SUMMARY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Agency Revenue", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text("₹${"%,.0f".format(totalRevenue)}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Expenses & Payouts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text("₹${"%,.0f".format(totalExpense)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NET COMPASS PROFIT:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "₹${"%,.0f".format(netProfit)}",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = if (netProfit >= 0) Color(0xFF047857) else Color.Red
                        )
                    }
                }
            }
        }

        // Segment breakdown list header
        item {
            Text(
                text = "Service Segment Analytics",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Render each segment cards
        items(segments) { segment ->
            val segRevenue = filteredRecords.filter { it.category == segment && it.type == "INCOME" }.sumOf { it.amount }
            val segExpense = filteredRecords.filter { it.category == segment && it.type == "EXPENSE" }.sumOf { it.amount }
            val segNet = segRevenue - segExpense

            val segmentColor = when (segment) {
                "Bike Rental" -> Color(0xFF059669)
                "BigBasket" -> Color(0xFF0284C7)
                "Zepto" -> Color(0xFF7C3AED)
                "Myntra" -> Color(0xFFDB2777)
                else -> MaterialTheme.colorScheme.primary
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Segment title & color block
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp, 24.dp)
                                    .background(segmentColor, RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = segment, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Box(
                            modifier = Modifier
                                .background(segmentColor.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 2.dp)
                        ) {
                            Text(
                                text = "Net: ₹${"%,.0f".format(segNet)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = segmentColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mini Progress bar showing revenue proportion of this segment
                    val revRatio = if (totalRevenue > 0) (segRevenue / totalRevenue).toFloat() else 0f
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = revRatio.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .background(segmentColor, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Revenue Earned", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${"%,.0f".format(segRevenue)}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Expenses / Partner Payouts", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${"%,.0f".format(segExpense)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BikeDocumentsDialog(
    bike: BikeInventory,
    onDismiss: () -> Unit,
    onSave: (BikeInventory) -> Unit
) {
    var selectedSubTab by remember { mutableStateOf(0) } // 0 = RC, 1 = Insurance
    var isEditing by remember { mutableStateOf(false) }

    // RC Fields
    var rcNumber by remember { mutableStateOf(bike.rcNumber) }
    var rcExpiryDate by remember { mutableStateOf(bike.rcExpiryDate) }
    var rcStatus by remember { mutableStateOf(bike.rcStatus) }
    var rcOwnerName by remember { mutableStateOf(bike.rcOwnerName) }
    var rcEngineNumber by remember { mutableStateOf(bike.rcEngineNumber) }
    var rcChassisNumber by remember { mutableStateOf(bike.rcChassisNumber) }

    // Insurance Fields
    var insurancePolicyNumber by remember { mutableStateOf(bike.insurancePolicyNumber) }
    var insuranceProvider by remember { mutableStateOf(bike.insuranceProvider) }
    var insuranceExpiryDate by remember { mutableStateOf(bike.insuranceExpiryDate) }
    var insuranceStatus by remember { mutableStateOf(bike.insuranceStatus) }
    var insurancePremium by remember { mutableStateOf(if (bike.insurancePremium > 0) bike.insurancePremium.toInt().toString() else "") }
    var insuranceSumInsured by remember { mutableStateOf(if (bike.insuranceSumInsured > 0) bike.insuranceSumInsured.toInt().toString() else "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Documents & Records",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${bike.bikeModel} • ${bike.plateNumber}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider()

                // Custom Tab Selector (Capsule Style)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Registration (RC)", "Insurance Policy").forEachIndexed { index, title ->
                        val selected = selectedSubTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { 
                                    selectedSubTab = index 
                                    isEditing = false // Reset editing when toggling tabs
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (selectedSubTab == 0) {
                        // ==================== RC TAB ====================
                        item {
                            // RC Card Preview
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCFBF4)),
                                border = BorderStroke(1.5.dp, Color(0xFFD6C5A0))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "GOVERNMENT OF INDIA",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E3A8A),
                                                letterSpacing = 1.2.sp
                                            )
                                            Text(
                                                text = "REGISTRATION CERTIFICATE (FORM 23)",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF4B5563)
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .border(1.dp, Color(0xFF1E3A8A).copy(alpha = 0.4f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("STA", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                                        }
                                    }

                                    Divider(color = Color(0xFFD6C5A0).copy(alpha = 0.3f))

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("REGISTRATION NO", fontSize = 9.sp, color = Color.Gray)
                                                Text(bike.plateNumber, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("CERTIFICATE NO", fontSize = 9.sp, color = Color.Gray)
                                                Text(rcNumber.ifEmpty { "NOT RECORDED" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if(rcNumber.isEmpty()) Color.Red else Color.Black)
                                            }
                                        }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.weight(1.2f)) {
                                                Text("REGISTERED OWNER", fontSize = 9.sp, color = Color.Gray)
                                                Text(rcOwnerName.ifEmpty { "Anam Enterprises" }, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                            }
                                            Column(modifier = Modifier.weight(0.8f)) {
                                                Text("EXPIRY DATE", fontSize = 9.sp, color = Color.Gray)
                                                Text(rcExpiryDate.ifEmpty { "2039-01-01" }, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                            }
                                        }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("ENGINE NUMBER", fontSize = 9.sp, color = Color.Gray)
                                                Text(rcEngineNumber.ifEmpty { "ENG-928371X" }, fontSize = 11.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = Color.DarkGray)
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("CHASSIS NUMBER", fontSize = 9.sp, color = Color.Gray)
                                                Text(rcChassisNumber.ifEmpty { "MBL-82739X" }, fontSize = 11.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = Color.DarkGray)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isEditing) {
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = rcNumber,
                                        onValueChange = { rcNumber = it.uppercase() },
                                        label = { Text("Registration/RC Number") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = rcOwnerName,
                                        onValueChange = { rcOwnerName = it },
                                        label = { Text("Registered Owner Name") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = rcExpiryDate,
                                        onValueChange = { rcExpiryDate = it },
                                        label = { Text("RC Expiry Date (YYYY-MM-DD)") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = rcEngineNumber,
                                        onValueChange = { rcEngineNumber = it.uppercase() },
                                        label = { Text("Engine Number") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = rcChassisNumber,
                                        onValueChange = { rcChassisNumber = it.uppercase() },
                                        label = { Text("Chassis Number") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    } else {
                        // ==================== INSURANCE TAB ====================
                        item {
                            // Insurance Card Preview
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                                border = BorderStroke(1.5.dp, Color(0xFF86EFAC))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = insuranceProvider.ifEmpty { "Digit General Insurance" }.uppercase(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF15803D),
                                                letterSpacing = 1.sp
                                            )
                                            Text(
                                                text = "COMPREHENSIVE MOTOR INSURANCE POLICY",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF374151)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF15803D).copy(alpha = 0.8f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Divider(color = Color(0xFF86EFAC).copy(alpha = 0.3f))

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("POLICY NUMBER", fontSize = 9.sp, color = Color.Gray)
                                                Text(insurancePolicyNumber.ifEmpty { "NOT RECORDED" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if(insurancePolicyNumber.isEmpty()) Color.Red else Color.Black)
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("POLICY STATUS", fontSize = 9.sp, color = Color.Gray)
                                                val isExpired = insuranceStatus == "Expired"
                                                Box(
                                                    modifier = Modifier
                                                        .background(if (isExpired) Color(0xFFFEE2E2) else Color(0xFFD1FAE5), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = insuranceStatus.uppercase(),
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isExpired) Color(0xFF991B1B) else Color(0xFF065F46)
                                                    )
                                                }
                                            }
                                        }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("IDV (SUM INSURED)", fontSize = 9.sp, color = Color.Gray)
                                                val sumVal = insuranceSumInsured.toDoubleOrNull() ?: 0.0
                                                Text("₹${"%,.0f".format(sumVal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text("PREMIUM PAID", fontSize = 9.sp, color = Color.Gray)
                                                val premVal = insurancePremium.toDoubleOrNull() ?: 0.0
                                                Text("₹${"%,.0f".format(premVal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                            }
                                        }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.weight(1.2f)) {
                                                Text("VEHICLE MODEL (PLATE)", fontSize = 9.sp, color = Color.Gray)
                                                Text("${bike.bikeModel} (${bike.plateNumber})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                            Column(modifier = Modifier.weight(0.8f)) {
                                                Text("POLICY EXPIRY", fontSize = 9.sp, color = Color.Gray)
                                                Text(insuranceExpiryDate.ifEmpty { "2027-01-01" }, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isEditing) {
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = insurancePolicyNumber,
                                        onValueChange = { insurancePolicyNumber = it.uppercase() },
                                        label = { Text("Policy Number") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = insuranceProvider,
                                        onValueChange = { insuranceProvider = it },
                                        label = { Text("Insurance Provider Co.") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = insuranceExpiryDate,
                                        onValueChange = { insuranceExpiryDate = it },
                                        label = { Text("Policy Expiry Date (YYYY-MM-DD)") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = insurancePremium,
                                            onValueChange = { insurancePremium = it },
                                            label = { Text("Premium Paid (₹)") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = insuranceSumInsured,
                                            onValueChange = { insuranceSumInsured = it },
                                            label = { Text("IDV Sum (₹)") },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Policy Status Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Policy Expiry Status:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            listOf("Active", "Expired").forEach { statusLabel ->
                                                val isSelected = insuranceStatus == statusLabel
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = if (isSelected) {
                                                                if (statusLabel == "Active") Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
                                                            } else {
                                                                MaterialTheme.colorScheme.surfaceVariant
                                                            },
                                                            shape = RoundedCornerShape(8.dp)
                                                        )
                                                        .clickable { insuranceStatus = statusLabel }
                                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                                ) {
                                                    Text(
                                                        text = statusLabel,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp,
                                                        color = if (isSelected) {
                                                            if (statusLabel == "Active") Color(0xFF065F46) else Color(0xFF991B1B)
                                                        } else {
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Footer Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isEditing) {
                        OutlinedButton(
                            onClick = { isEditing = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                val updatedBike = bike.copy(
                                    rcNumber = rcNumber,
                                    rcExpiryDate = rcExpiryDate,
                                    rcStatus = rcStatus,
                                    rcOwnerName = rcOwnerName,
                                    rcEngineNumber = rcEngineNumber,
                                    rcChassisNumber = rcChassisNumber,
                                    insurancePolicyNumber = insurancePolicyNumber,
                                    insuranceProvider = insuranceProvider,
                                    insuranceExpiryDate = insuranceExpiryDate,
                                    insuranceStatus = insuranceStatus,
                                    insurancePremium = insurancePremium.toDoubleOrNull() ?: 0.0,
                                    insuranceSumInsured = insuranceSumInsured.toDoubleOrNull() ?: 0.0
                                )
                                onSave(updatedBike)
                                isEditing = false
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Changes")
                        }
                    } else {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Details")
                        }

                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}
