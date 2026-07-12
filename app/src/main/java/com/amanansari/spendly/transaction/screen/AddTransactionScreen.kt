package com.amanansari.spendly.transaction.screen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.amanansari.spendly.components.AddIncomeExpenseCategoryItem
import com.amanansari.spendly.components.AmountInputField
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.ui.theme.BrightGray
import com.amanansari.spendly.ui.theme.DarkBorder
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightGray
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.Primary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddTransactionScreen(
    onClose: ()->Unit,
    quickSelectedCategoryId : String? = null){

    var selectedCategory by remember { mutableStateOf<ExpIncCategory?>(categoryFromId(quickSelectedCategoryId)) }
    var selectedDate by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }

    val pagerState = rememberPagerState(pageCount = { 2 }) // 0 = Expense, 1 = Income
    val scope = rememberCoroutineScope()
    val isExpense = pagerState.currentPage == 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        ModalTopBar(
            isExpense = isExpense,
            onCloseClick = onClose,
            onTypeChanged = { expenseSelected ->
                scope.launch {
                    pagerState.animateScrollToPage(if (expenseSelected) 0 else 1)
                }
            }
        )

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

                AddExpenseScreen(
                    selectedCategory = selectedCategory as? ExpIncCategory.ExpenseCategory,
                    onCategoryChange = { selectedCategory = it },
                    selectedDate = selectedDate,
                    onDateChange = { selectedDate = it }
                )
            } else {
                AddIncomeScreen(
                    selectedCategory = selectedCategory as? ExpIncCategory.IncomeCategory,
                    onCategoryChange = { selectedCategory = it },
                    selectedDate = selectedDate,
                    onDateChange = { selectedDate = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val accent by animateColorAsState(
            targetValue = if (isExpense) ExpenseRed else IncomeGreen,
            label = "saveAccent"
        )

        Button(onClick = {  },
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
fun<T : ExpIncCategory> CategoryGrid(
    categories: List<T>,
    selectedCategory: T?,
    onCategorySelected: (T) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), //? Fixed the height of the Grid to Show only 2 Rows
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(6.dp)
    ) {
        items(categories) { category ->

            AddIncomeExpenseCategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}


@Composable
fun ExpenseNoteField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        placeholder = {
            Text("What was this for?", color = Color.Gray)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = LightTextSecondary

            )
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,

        )
}

@Composable
fun DatePickerField(
    selectedDate: Long?,
    onDateChange: (Long) -> Unit
){

    var showDatePicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = formatDate(selectedDate),
        onValueChange = {},
        readOnly = true,
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        leadingIcon = {
            Icon(imageVector = Icons.Default.DateRange,
                contentDescription = "Date Picker",
                tint = LightTextSecondary
            )
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        singleLine = true
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            showDatePicker = true
        }
    }


    if (showDatePicker) {

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                ?: System.currentTimeMillis() // default today
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateChange(it)   // 🔥 send to parent
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


fun formatDate(millis: Long?): String {
    return millis?.let {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(it))
    } ?: ""
}


@Composable
fun ModalTopBar(isExpense: Boolean,
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
fun AddTransactionScreenPreview(){
    AddTransactionScreen(
        onClose = {}
    )
}