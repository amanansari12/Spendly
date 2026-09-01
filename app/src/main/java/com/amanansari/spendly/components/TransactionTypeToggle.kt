package com.amanansari.spendly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightSurface

@Composable
fun TransactionTypeToggle(
    isExpense: Boolean,
    onTypeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 55.dp,
    itemHeight: Dp = 45.dp,
    fontSize: TextUnit = 15.sp,
    iconSize: Dp = 15.dp
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(LightBg)
            .padding(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .height(itemHeight)
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isExpense) ExpenseRed else Color.Transparent
                )
                .clickable {
                    onTypeChanged(true)
                }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Expense Button",
                    tint = if (isExpense) LightSurface else Color.DarkGray,
                    modifier = Modifier.size(iconSize)
                )

                Text(
                    text = "Expense",
                    color = if (isExpense) LightSurface else Color.DarkGray,
                    fontWeight = if (!isExpense)
                        FontWeight.ExtraBold
                    else
                        FontWeight.Medium,
                    fontSize = fontSize
                )
            }
        }

        Box(
            modifier = Modifier
                .height(itemHeight)
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (!isExpense) IncomeGreen else Color.Transparent
                )
                .clickable {
                    onTypeChanged(false)
                }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Income Button",
                    tint = if (!isExpense) LightSurface else Color.DarkGray,
                    modifier = Modifier.size(iconSize)
                )

                Text(
                    text = "Income",
                    color = if (!isExpense) LightSurface else Color.DarkGray,
                    fontWeight = if (!isExpense)
                        FontWeight.Bold
                    else
                        FontWeight.Medium,
                    fontSize = fontSize
                )
            }
        }
    }
}