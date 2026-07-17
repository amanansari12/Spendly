package com.amanansari.spendly.transaction.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.amanansari.spendly.components.AddIncomeExpenseCategoryItem
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.allIncomeCategories
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.transaction.viewmodel.TransactionCompletionState
import com.amanansari.spendly.transaction.viewmodel.TransactionViewModel
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.LightTextSecondary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionScreen(
    transactionViewmodel : TransactionViewModel = hiltViewModel(),
    onClose: () -> Unit,
    quickSelectedCategoryId : String? = null
){
    LaunchedEffect(quickSelectedCategoryId) {
        transactionViewmodel.toggleCategoryId(quickSelectedCategoryId)
    }

    // React to completion state changes instead of checking once
    LaunchedEffect(transactionViewmodel.completionState) {
        if (transactionViewmodel.completionState == TransactionCompletionState.Success) {
            onClose()
        }
    }

    TransactionScreenContent(
        state = transactionViewmodel.uiState,
        onClose = onClose,
        onCategoryChange = {
            transactionViewmodel.toggleCategoryId(it)
        },
        onAmountChange = {
            transactionViewmodel.updateAmount(it)
        },
        onNoteChange = {
            transactionViewmodel.updateNote(it)
        },
        onDateChange = {
            transactionViewmodel.updateDate(it)
        },
        onTypeChange = {
            transactionViewmodel.updateType(it)
        },
        onSubmit = {
            transactionViewmodel.completeTransaction()
        }

    )
}

@Composable
fun TransactionScreenContent(
    state : TransactionUiState,
    onClose: ()->Unit,
    onCategoryChange : (String) ->Unit,
    onAmountChange : (String) -> Unit,
    onNoteChange : (String) -> Unit,
    onDateChange : (Long) -> Unit,
    onTypeChange : (TransactionType) -> Unit,
    onSubmit : () -> Unit,
    )
{

    val pagerState = rememberPagerState(pageCount = { 2 }) // 0 = Expense, 1 = Income
    val scope = rememberCoroutineScope()
    val isExpense = pagerState.currentPage == 0

    LaunchedEffect(isExpense) {
        onTypeChange(if (isExpense) TransactionType.EXPENSE else TransactionType.INCOME)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        TransactionTopBar(
            isExpense = isExpense,
            onCloseClick = onClose,
            onTypeChanged = { expenseSelected ->
                scope.launch {
                    pagerState.animateScrollToPage(if (expenseSelected) 0 else 1)
                }
            }
        )

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        TransactionTypeToggle(isExpense, onTypeChanged = {expenseSelected ->
            scope.launch {
                pagerState.animateScrollToPage(if (expenseSelected) 0 else 1)
            }})

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .weight(1f)
        ) { page ->

            if (page == 0) {

                TransactionForm(
                    state = state,
                    categories = allExpenseCategories,
                    onCategoryChange = onCategoryChange,
                    onAmountChange = onAmountChange,
                    onDateChange = onDateChange,
                    onNoteChange = onNoteChange,
                    accentColor = ExpenseRed
                )

            } else {
                TransactionForm(
                    state = state,
                    categories = allIncomeCategories,
                    onCategoryChange = onCategoryChange,
                    onAmountChange = onAmountChange,
                    onDateChange = onDateChange,
                    onNoteChange = onNoteChange,
                    accentColor = IncomeGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val accent by animateColorAsState(
            targetValue = if (isExpense) ExpenseRed else IncomeGreen,
            label = "saveAccent"
        )

        Button(onClick = {
            onSubmit()
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accent),

            ) {

            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isExpense) "Save expense" else "Save income",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }


    }





}






@Composable
fun TransactionTopBar(isExpense: Boolean,
                onCloseClick: () -> Unit,
                onTypeChanged: (Boolean) -> Unit,
){
    //? Add Transaction Top Level Bar
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = if (isExpense) "Add Transaction" else "Add Income",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(50)
                )
                .clickable { onCloseClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(35.dp),

                )
        }






    }
}

@Composable
fun TransactionTypeToggle(
    isExpense: Boolean,
    onTypeChanged: (Boolean) -> Unit
) {
    // The outer pill-shaped container
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LightBg) // Soft, slightly tinted background
            .padding(7.dp), // Padding creates the gap between the inner button and outer edge
        // Border with the primary color
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Expense Option
        Box(
            modifier = Modifier
                .height(45.dp)
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isExpense) ExpenseRed else Color.Transparent) // Or your preferred active expense color
                .clickable { onTypeChanged(true) }
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
                    modifier = Modifier.size(15.dp)
                )

                Text(
                    text = "Expense",
                    color = if (isExpense) LightSurface else Color.DarkGray,
                    fontWeight = if (!isExpense) FontWeight.ExtraBold else FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

        }
        // Income Option
        Box(
            modifier = Modifier
                .height(45.dp)
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (!isExpense) IncomeGreen else Color.Transparent)
                .clickable { onTypeChanged(false) }
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
                    modifier = Modifier.size(15.dp)
                )

                Text(
                    text = "Income",
                    color = if (!isExpense) LightSurface else Color.DarkGray,
                    fontWeight = if (!isExpense) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview(){
    TransactionScreenContent(
        state = TransactionUiState(),
        onClose = {},
        onCategoryChange = {},
        onAmountChange = {},
        onNoteChange = {},
        onDateChange = {},
        onTypeChange = {},
        onSubmit = {}

    )
}