package com.amanansari.spendly.home.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.home.state.HomeUiState
import com.amanansari.spendly.home.viewmodel.HomeViewModel
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.ui.theme.BrightGray
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightGray
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.ui.theme.PrimaryLight
import com.amanansari.spendly.ui.theme.SpendlyTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import androidx.compose.ui.platform.LocalLocale
import com.amanansari.spendly.data.local.entity.TransactionEntity
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.utils.formatTransactionTime
import com.amanansari.spendly.utils.toCurrencyString
import java.math.BigDecimal
import java.time.ZoneId
import java.time.Instant
import java.util.UUID

private fun Long.toMajorUnits(): BigDecimal = BigDecimal(this).movePointLeft(2)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onQuickSelect: (ExpIncCategory.ExpenseCategory) -> Unit,
    onViewAll : () -> Unit,
    onViewAllBudgets : () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.uiState.collectAsState()

    HomeScreenContent(
        state = state,
        onQuickSelect = onQuickSelect,
        onViewAll = onViewAll,
        onViewAllBudgets = onViewAllBudgets
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onQuickSelect: (ExpIncCategory.ExpenseCategory) -> Unit,
    onViewAll: () -> Unit,
    onViewAllBudgets : () -> Unit,
    ) {


    val context = LocalContext.current

    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val currentMonthYear = currentDate.format(formatter)

    val budgetAvailableToSpent = (state.totalAllocatedAmount - state.amountSpentFromAllocated)
        .toMajorUnits()
        .toCurrencyString(state.defaultCurrency)

    val totalBalanceForMonth = (state.totalIncome + state.openingBalance)
        .toMajorUnits()
        .toCurrencyString(state.defaultCurrency)

    val totalAllocatedAmount = state.totalAllocatedAmount
        .toMajorUnits()
        .toCurrencyString(state.defaultCurrency)

    val amountSpentFromAllocated = state.amountSpentFromAllocated
        .toMajorUnits()
        .toCurrencyString(state.defaultCurrency)

    val unAllocatedAmount = (state.totalIncome + state.openingBalance - state.totalAllocatedAmount)
        .toMajorUnits()
        .toCurrencyString(state.defaultCurrency)

    val budgetStatusColor = when {
        state.budgetUsedPercentage >= BigDecimal(100) -> ExpenseRed
        state.budgetUsedPercentage >= BigDecimal(75) -> Color(0xFFFFDB58) // same amber as the unallocated warning
        else -> LightSurface
    }


    val surplusMonth: String? = state.carriedFromMonth
        .let { runCatching { YearMonth.parse(it) }.getOrNull() }
        ?.month
        ?.getDisplayName(TextStyle.FULL, LocalLocale.current.platformLocale)

    var showBottomSheet by remember { mutableStateOf(false) }

    BudgetInformationModal(
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        partialBudgetDetails = state.budgetPartialDetail,
        onViewAllBudgets = onViewAllBudgets
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start

    ) {
        //! Redesigned the Entire BalanceSummaryCard
        //? We use a Box instead of a Card because making a Card's background transparent
        //? (which is necessary to show our custom gradient) breaks its built-in drop shadow.
        //? A Box with explicit .shadow() and .background() modifiers gives us perfect control.

        //> Balance Summary Card
        item {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val gradient = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.0f to PrimaryDark,
                        0.5f to Primary,
                        1.0f to PrimaryLight
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(
                        constraints.maxWidth.toFloat(),
                        constraints.maxHeight.toFloat() * 0.7f
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Keeps your horizontal size
                        .clip(RoundedCornerShape(20.dp)) // Keeps the gradient inside the rounded corners
                        .background(gradient) // Applies the purple gradient
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        // Current Income Column Styling
                        Column {

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text(
                                    text = "AVAILABLE TO SPEND",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LightGray
                                )

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(40))
                                        .background(color = Color.White.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp)

                                ){
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = BrightGray,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .padding(end = 2.dp)
                                    )

                                    Text(
                                        text = currentMonthYear,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrightGray,
                                    )
                                }

                            }

                            Text(
                                text = budgetAvailableToSpent,
                                style = MaterialTheme.typography.headlineLarge,
                                color = LightSurface,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "of $totalAllocatedAmount this Month",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = LightGray
                            )

                            if(state.isUnAllocatedAmountLeft){

                                val isOverspentWithUnallocated = state.budgetUsedPercentage >= BigDecimal(100)

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(40))
                                        .background(color = if (isOverspentWithUnallocated)
                                            ExpenseRed.copy(alpha = 0.22f)
                                        else
                                            Color.Red.copy(alpha = 0.14f))
                                        .clickable {
                                            Toast.makeText(context, "Unallocated Amount Clicked", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 6.dp)

                                ) {

                                    Icon(
                                        imageVector = Icons.Default.WarningAmber,
                                        contentDescription = null,
                                        tint = if (isOverspentWithUnallocated) ExpenseRed else Color(0xFFFFDB58),
                                        modifier = Modifier
                                            .size(14.dp)
                                            .padding(end = 2.dp)
                                    )


                                    Text(
                                        text = if (isOverspentWithUnallocated)
                                            "$unAllocatedAmount Unassigned — Reassign to Cover Overspend"
                                        else
                                            "$unAllocatedAmount Unallocated - Tap to Assign",
                                        fontSize = if (isOverspentWithUnallocated) 10.sp else 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightSurface,
                                        modifier = Modifier.clickable{
                                            Toast.makeText(context, "Unallocated Amount Clicked", Toast.LENGTH_SHORT).show()
                                            onViewAllBudgets()
                                        }

                                    )
                                }

                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            color = Color.White.copy(alpha = 0.2f) // Optional: makes the divider blend better with the gradient
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(4.dp)

                                ){
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = IncomeGreen,
                                        modifier = Modifier
                                            .size(15.dp)
                                            .padding(end = 2.dp)
                                    )

                                    Text(
                                        text = "TOTAL INCOME",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightSurface
                                    )
                                }

                                Text(
                                    text = "+$totalBalanceForMonth",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IncomeGreen
                                )

                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .padding(horizontal = 12.dp)
                                        .width(100.dp),

                                    verticalArrangement = Arrangement.Center

                                ){
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.Payments,
                                            contentDescription = null,
                                            tint = LightGray,
                                            modifier = Modifier
                                                .size(12.dp)
                                                .padding(end = 2.dp)
                                        )

                                        Text(
                                            text = "THIS MONTH",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightGray
                                        )
                                    }

                                    Text(
                                        text = state.totalIncome.toMajorUnits().toCurrencyString(state.defaultCurrency),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightSurface
                                    )

                                    Text(
                                        text = "",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightGray
                                    )

                                }

                                VerticalDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier.height(70.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                )

                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .padding(horizontal = 12.dp)
                                        .width(100.dp),

                                    verticalArrangement = Arrangement.Center

                                ){
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.SouthWest,
                                            contentDescription = null,
                                            tint = IncomeGreen,
                                            modifier = Modifier
                                                .size(12.dp)
                                                .padding(end = 2.dp)
                                        )

                                        Text(
                                            text = "CARRIED",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IncomeGreen
                                        )
                                    }

                                    Text(
                                        text = "+" + state.openingBalance.toMajorUnits().toCurrencyString(state.defaultCurrency),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IncomeGreen
                                    )

                                    Text(
                                        text = surplusMonth?.let { "$it surplus" } ?: "No carryover",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightGray
                                    )

                                }
                            }


                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            color = Color.White.copy(alpha = 0.2f) // Optional: makes the divider blend better with the gradient
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Income Column Styling
                            Column {
                                Text(
                                    text = "SPENT",
                                    fontSize = 12.sp,
                                    color = LightGray
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = "Spent",
                                        tint = ExpenseRed,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "-$amountSpentFromAllocated",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightSurface
                                    )
                                }
                            }

                            // Expense Column Styling
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "ALLOCATED",
                                    fontSize = 12.sp,
                                    color = LightGray
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = "Expense Down", // Updated content description
                                        tint = IncomeGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "+$totalAllocatedAmount",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IncomeGreen
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Column(
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Budget Used",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LightGray
                                )

                                Text(
                                    text = "${"%.1f".format(state.budgetUsedPercentage)}%",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = budgetStatusColor,
                                    fontSize = 12.sp,
                                )
                            }

                            LinearProgressIndicator(
                                progress = { (state.budgetUsedPercentage.toFloat() / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp, bottom = 10.dp)
                                    .height(9.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = budgetStatusColor,
                                trackColor = LightNavInactive,
                                strokeCap = StrokeCap.Butt, // Natively rounds the ends of the progress bar
                                gapSize = 0.dp, // Removes the Material 3 gap
                                drawStopIndicator = {} // Removes the Material 3 stop indicator
                            )

                        }

                    }
                }
            }


        }

        item {
            Column() {
                Button(
                    onClick = {showBottomSheet = true},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightSurface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Tap to View Budgets",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Primary
                        )
                    }
                }
            }
        }

        //? Daily Expense

        item {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Recent Transaction",
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(
                        onClick = { onViewAll()},
                    ) {
                        Text(
                            text = "View All",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                }

            }
        }


        //? Daily Transactions Shown If Transaction is Done
        if(state.isTransaction){
            items(
                items = state.recentTransaction,
                key = { it.transactionId }
            ) { transaction ->

                val category = categoryFromId(transaction.categoryId)

                val isIncome = transaction.type == TransactionType.INCOME

                val displayCategory = category
                    ?: if (isIncome) ExpIncCategory.IncomeCategory.Other else ExpIncCategory.ExpenseCategory.Misc



                val formattedAmount = transaction.amount
                    .toMajorUnits()
                    .toCurrencyString(transaction.currencyCode)



                val categoryTitle = category?.title
                    ?: if (transaction.type == TransactionType.INCOME) "Income" else "Uncategorized"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = LightSurface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            // TODO: Make Changes in This,
                            // TODO: Make the Create Separate Code for Viewing Icon here
                            CategoryIconBox(category = displayCategory, isSelected = false, onClick = {  })

                            Column {
                                Text(
                                    text = transaction.note?.takeIf { it.isNotBlank() } ?: categoryTitle,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "$categoryTitle • ${formatTransactionTime(transaction.occurredAt)}",
                                    fontSize = 10.sp
                                )
                            }
                        }


                        Text(
                            text = if (isIncome) "+$formattedAmount" else "-$formattedAmount",
                            fontWeight = FontWeight.Bold,
                            color = if (isIncome) IncomeGreen else ExpenseRed
                        )
                    }
                }

            }
        }
        else{
            item{
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = LightSurface
                    )


                ){
                    Column(
                        modifier = Modifier
                            .padding(25.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Primary.copy(alpha = 0.15f)),

                            contentAlignment = Alignment.Center

                        ){
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint= PrimaryLight,
                                modifier = Modifier.size(32.dp)

                            )
                        }

                        Text(
                            text = "Add Transaction",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Tap the + button below to log your first transaction of the month",
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        )

                    }

                }
            }

        }


        //? Quick Add Button

        item{
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ){
                Text(
                    text = "Quick Add",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(allExpenseCategories){ category ->

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ){
                            CategoryIconBox(
                                category = category,
                                isSelected = false,//category == quickSelectedCategory,
                                onClick = {
                                    // quickSelectedCategory = category
                                    onQuickSelect(category)

                                }
                            )

                            Text(
                                text = category.title.split(" ").firstOrNull() ?: "",
                                fontSize = 11.sp,
                                color = LightTextSecondary
                            )
                        }

                    }
                }
            }
        }

    }
}


fun getInitials(name : String) : String {
    val initials = name
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { it[0].uppercaseChar() }
        .joinToString("")


    return initials
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val now = LocalDate.now() // 2026-07-11

    fun epochAt(date: LocalDate, hour: Int = 14, minute: Int = 45): Long =
        date.atTime(hour, minute)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    val dummyTimestamps = listOf(
        epochAt(now),                    // Today       -> "2:45 PM"
        epochAt(now.minusDays(1)),       // Yesterday   -> "Yesterday"
        epochAt(now.minusDays(3)),       // 3 days ago  -> "3 days ago"
        epochAt(now.minusDays(6)),       // 6 days ago  -> "6 days ago"
        epochAt(LocalDate.of(2026, 6, 6)), // Same year -> "6 June"
        epochAt(LocalDate.of(2025, 12, 25)) // Prior year -> "25 December 2025"
    )

    val previewState = HomeUiState(
        userName = "Aman",
        defaultCurrency = "INR",
        openingBalance = 5_000_00L,
        totalIncome = 25_000_00L,
        totalAllocatedAmount = 20_000_00L,
        amountSpentFromAllocated = 20_000_00L,
        closingBalance = 21_500_00L,
        carriedFromMonth = "2026-06",
        recentTransaction = listOf(
            TransactionEntity(
                userId = UUID.randomUUID(),
                categoryId = "food",
                type = TransactionType.EXPENSE,
                currencyCode = "INR",
                amount = 450_00L,
                occurredAt = epochAt(now),
                monthKey = "2026-07",
                note = "Lunch"
            ),
            TransactionEntity(
                userId = UUID.randomUUID(),
                categoryId = "transport",
                type = TransactionType.EXPENSE,
                currencyCode = "INR",
                amount = 4020_00L,
                occurredAt = epochAt(now.minusDays(1)),
                monthKey = "2026-07",
                note = "Metro"
            ),
            TransactionEntity(
                userId = UUID.randomUUID(),
                categoryId = "salary",
                type = TransactionType.INCOME,
                currencyCode = "INR",
                amount = 25_000_00L,
                occurredAt = epochAt(now.minusDays(7)),
                monthKey = "2026-07",
                note = "Salary"
            )
        )
    )

    SpendlyTheme {
        HomeScreenContent(
            state = previewState,
            onQuickSelect = {},
            onViewAll = {},
            onViewAllBudgets = {},
        )
    }
}