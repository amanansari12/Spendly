package com.amanansari.spendly.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.LightTextSecondary

@Composable
fun CategoryIconBox(
    category: ExpIncCategory,
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
    onCategoryChange: (String) -> Unit
){

    val scale by animateFloatAsState(
        targetValue = if(isSelected) 1f else 0.96f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "categoryScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) category.color else Color(0xFFF1F1F5),
        label = "categoryBg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) LightSurface else Color.DarkGray,
        label = "categoryContent"
    )

    var modifier = Modifier
        .graphicsLayer { scaleX = scale; scaleY = scale }
    if (isSelected) {
        modifier = modifier.shadow(10.dp, RoundedCornerShape(18.dp), spotColor = category.color)
    }

    modifier = modifier
        .clip(RoundedCornerShape(18.dp))
        .background(backgroundColor)
        .clickable { onCategoryChange(category.id) }
        .padding(8.dp)
        .height(50.dp)
        .width(140.dp)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        // Icon Box (reused from the  existing component logic)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isSelected) Color.White.copy(alpha = 0.25f)
                    else category.color.copy(alpha = 0.12f)
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

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = category.title.split(" ")[0], // short label like "Food"
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }

}


@Composable
@Preview
fun AddIncomeExpenseCategoryItemPreview(){
//    AddIncomeExpenseCategoryItem(
//        category = ExpIncCategory.IncomeCategory.Salary,
//        isSelected = false,
//        onClick = {}
//    )
}