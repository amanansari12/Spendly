package com.amanansari.spendly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.ui.theme.LightTextSecondary

@Composable
fun CategoryIconBox(
    category: ExpIncCategory.ExpenseCategory,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {

    val color = category.color

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            // We take the category color and make it very light for the background
            .background(category.color.copy(alpha = 0.1f))
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),

        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = category.title,
            modifier = Modifier.size(24.dp),
            tint = color // The solid dark color for the icon
        )
    }
}


@Composable
fun AddIncomeExpenseCategoryItem(
    category: ExpIncCategory,
    isSelected: Boolean,
    onClick: () -> Unit
){
    val backgroundColor =
        if (isSelected) category.color
        else Color(0xFFF1F1F5)

    val contentColor =
        if (isSelected) Color.White
        else LightTextSecondary

    Column(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Icon Box (reused from the  existing component logic)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isSelected) Color.White.copy(alpha = 0.2f)
                    else category.color.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.title,
                tint = if (isSelected) Color.White else category.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.title.split(" ")[0], // short label like "Food"
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }

}
