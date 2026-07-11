package com.amanansari.spendly.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// This is where we "store" the logic for your 10 categories
sealed class ExpIncCategory(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
) {

    sealed class ExpenseCategory(
        id: String,
        title: String,
        icon: ImageVector,
        color: Color
    ) : ExpIncCategory(id,title, icon, color) {

        object Food : ExpenseCategory("food","Food & Dining", Icons.Default.LunchDining, Color(0xFFE65100))

        object Rent : ExpenseCategory("rent", "Rent", Icons.Default.House, Color(0xFF29D9BA))
        object Groceries : ExpenseCategory("groceries","Groceries", Icons.Default.ShoppingBasket, Color(0xFF2E7D32))
        object Transport : ExpenseCategory("transport","Transport", Icons.Default.DirectionsBus, Color(0xFF1976D2))
        object Shopping : ExpenseCategory("shopping","Shopping", Icons.Default.ShoppingBag, Color(0xFF7B1FA2))
        object Bills : ExpenseCategory("bills", "Bills & Utilities", Icons.AutoMirrored.Filled.ReceiptLong, Color(0xFFC2185B))
        object Entertainment : ExpenseCategory("entertainment","Entertainment", Icons.Default.ConfirmationNumber, Color(0xFFE91E63))
        object Health : ExpenseCategory("health","Health & Wellness", Icons.Default.MedicalServices, Color(0xFFD32F2F))
        object Education : ExpenseCategory("education","Education", Icons.Default.School, Color(0xFF00796B))
        object PersonalCare : ExpenseCategory("personal_care","Personal Care", Icons.Default.ContentCut, Color(0xFF616161))
        object Misc : ExpenseCategory("misc","Miscellaneous", Icons.Default.Category, Color(0xFF455A64))
    }

    sealed class IncomeCategory(
        id: String,
        title: String,
        icon: ImageVector,
        color: Color
    ) : ExpIncCategory(id, title, icon, color) {

        object Salary : IncomeCategory("salary","Salary", Icons.Default.Work, Color(0xFF2ECC71))
        object Freelance : IncomeCategory("freelance","Freelance", Icons.Default.LaptopMac, Color(0xFF00BCD4))
        object Investment : IncomeCategory("investment","Investment", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFF2196F3))
        object Business : IncomeCategory("business","Business", Icons.Default.Storefront, Color(0xFF3F51B5))
        object Gift : IncomeCategory("gift","Gift", Icons.Default.CardGiftcard, Color(0xFF009688))
        object Refund : IncomeCategory("refund","Refund", Icons.Default.CurrencyExchange, Color(0xFF8BC34A))
        object Benefits : IncomeCategory("benefits","Benefits", Icons.Default.AccountBalance, Color(0xFF00ACC1))
        object Other : IncomeCategory("other","Other", Icons.Default.MoreHoriz, Color(0xFF607D8B))
    }
}

// 2. Added explicit scoping (ExpIncCategory.ExpenseCategory)
val allExpenseCategories = listOf(
    ExpIncCategory.ExpenseCategory.Food,
    ExpIncCategory.ExpenseCategory.Rent,
    ExpIncCategory.ExpenseCategory.Groceries,
    ExpIncCategory.ExpenseCategory.Transport,
    ExpIncCategory.ExpenseCategory.Shopping,
    ExpIncCategory.ExpenseCategory.Bills,
    ExpIncCategory.ExpenseCategory.Entertainment,
    ExpIncCategory.ExpenseCategory.Health,
    ExpIncCategory.ExpenseCategory.Education,
    ExpIncCategory.ExpenseCategory.PersonalCare,
    ExpIncCategory.ExpenseCategory.Misc
)

// 2. Added explicit scoping (ExpIncCategory.IncomeCategory)
val allIncomeCategories = listOf(
    ExpIncCategory.IncomeCategory.Salary,
    ExpIncCategory.IncomeCategory.Freelance,
    ExpIncCategory.IncomeCategory.Investment,
    ExpIncCategory.IncomeCategory.Business,
    ExpIncCategory.IncomeCategory.Gift,
    ExpIncCategory.IncomeCategory.Refund,
    ExpIncCategory.IncomeCategory.Benefits,
    ExpIncCategory.IncomeCategory.Other
)

private val categoryById: Map<String, ExpIncCategory> =
    (allIncomeCategories + allExpenseCategories).associateBy { it.id }

fun categoryFromId(id: String?) : ExpIncCategory? = categoryById[id]