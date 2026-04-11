package com.amanansari.spendly.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// This is where we "store" the logic for your 10 categories
sealed class SpendlyCategory(
    val title: String,
    val icon: ImageVector,
    val color: Color
) {
    object Food : SpendlyCategory("Food & Dining", Icons.Default.LunchDining, Color(0xFFE65100))
    object Groceries : SpendlyCategory("Groceries", Icons.Default.ShoppingBasket, Color(0xFF2E7D32))
    object Transport : SpendlyCategory("Transport", Icons.Default.DirectionsBus, Color(0xFF1976D2))
    object Shopping : SpendlyCategory("Shopping", Icons.Default.ShoppingBag, Color(0xFF7B1FA2))
    object Bills : SpendlyCategory("Bills & Utilities", Icons.AutoMirrored.Filled.ReceiptLong, Color(0xFFC2185B))
    object Entertainment : SpendlyCategory("Entertainment", Icons.Default.ConfirmationNumber, Color(0xFFE91E63))
    object Health : SpendlyCategory("Health & Wellness", Icons.Default.MedicalServices, Color(0xFFD32F2F))
    object Education : SpendlyCategory("Education", Icons.Default.School, Color(0xFF00796B))
    object PersonalCare : SpendlyCategory("Personal Care", Icons.Default.ContentCut, Color(0xFF616161))
    object Misc : SpendlyCategory("Miscellaneous", Icons.Default.Category, Color(0xFF455A64))
}


val allCategories = listOf(
    SpendlyCategory.Food,
    SpendlyCategory.Groceries,
    SpendlyCategory.Transport,
    SpendlyCategory.Shopping,
    SpendlyCategory.Bills,
    SpendlyCategory.Entertainment,
    SpendlyCategory.Health,
    SpendlyCategory.Education,
    SpendlyCategory.PersonalCare,
    SpendlyCategory.Misc
)