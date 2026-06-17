package com.amanansari.spendly.transaction.presentation

import androidx.compose.material3.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.AddIncomeExpenseCategoryItem
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.allIncomeCategories
import com.amanansari.spendly.ui.theme.BrightGray
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Platinum
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTxScreen(showBottomSheet : Boolean,
                onClick : () -> Unit,
                selectedCategory : ExpIncCategory? = null,
                onCategoryChange: (ExpIncCategory) -> Unit,
                selectedDate : Long? = null,
                onDateChange: (Long) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isExpense by remember { mutableStateOf(true) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onClick() },
            sheetState = sheetState,
            containerColor = LightSurface
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
                
            ) {

                Column() {
                    ModalTopBar(isExpense = isExpense,
                        onCloseClick = onClick,
                        onTypeChanged = { isExpense = it }
                    )

                    if(isExpense){
                        AddExpenseScreen(
                            selectedCategory = selectedCategory as? ExpIncCategory.ExpenseCategory?,
                            onCategoryChange = onCategoryChange,
                            selectedDate = selectedDate,
                            onDateChange = onDateChange
                        )

                    }
                    else{
                        AddIncomeScreen(
                            selectedCategory = selectedCategory as? ExpIncCategory.IncomeCategory?,
                            onCategoryChange = onCategoryChange,
                            selectedDate = selectedDate,
                            onDateChange = onDateChange
                        )
                    }

                }

                Button(onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary // background color
                    ),

                    ) {
                    Text(
                        text = "Save Transaction",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            }

        }

    }

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

        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.clickable { onCloseClick() }.size(40.dp)
        )

        Text(
            text = if (isExpense) "Add Transaction" else "Add Income",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        TransactionTypeToggle(isExpense, onTypeChanged = onTypeChanged)
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
            .clip(RoundedCornerShape(50))
            .background(BrightGray) // Soft, slightly tinted background
            .padding(1.dp), // Padding creates the gap between the inner button and outer edge
        // Border with the primary color
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Expense Option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(if (isExpense) ExpenseRed else Color.Transparent) // Or your preferred active expense color
                .clickable { onTypeChanged(true) }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Expense",
                color = if (isExpense) LightBg else Color.Black,
                fontWeight = if (!isExpense) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp
            )
        }
        // Income Option
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(if (!isExpense) IncomeGreen else Color.Transparent)
                .clickable { onTypeChanged(false) }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Income",
                color = if (!isExpense) LightBg else Color.Black,
                fontWeight = if (!isExpense) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp
            )
        }
    }
}


@Composable
fun AddExpenseScreen(selectedCategory: ExpIncCategory.ExpenseCategory?,
                     onCategoryChange: (ExpIncCategory.ExpenseCategory) -> Unit,
                     selectedDate : Long? = null,
                     onDateChange: (Long) -> Unit
) {

    var amount by remember { mutableStateOf("") }
    val amountDecimal = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
    var expenseNote by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(top = 30.dp).fillMaxWidth(),
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "ENTER AMOUNT",
                color = LightNavInactive,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Text(
                        text = "$",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PrimaryDark,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    BasicTextField(
                        value = amount,
                        onValueChange = {amount = it},
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        cursorBrush = SolidColor(PrimaryDark),
                        modifier = Modifier.width(IntrinsicSize.Min),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.heightIn(min = 36.dp),
                                contentAlignment = Alignment.CenterStart) {
                                if (amount.isEmpty()) {
                                    Text(text = "0.00",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Platinum
                                    )
                                }
                                innerTextField()
                            }
                        }


                    )

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier.width(80.dp),
                thickness = 2.dp,
                color = ExpenseRed
            )
        }

        Column(
            modifier = Modifier.padding(vertical =  12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Category",
                color = LightNavInactive,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryGrid(
                categories = allExpenseCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategoryChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExpenseNoteField(value = expenseNote, onValueChange = {value -> expenseNote = value} )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(selectedDate = selectedDate, onDateChange = onDateChange)
            }

        }
    }
}



@Composable
fun AddIncomeScreen(
    selectedCategory: ExpIncCategory.IncomeCategory?,
    onCategoryChange: (ExpIncCategory.IncomeCategory) -> Unit,
    selectedDate : Long? = null,
    onDateChange: (Long) -> Unit
){
    var amount by remember { mutableStateOf("") }
    val amountDecimal = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
    var expenseNote by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(top = 40.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "ENTER AMOUNT",
                color = LightNavInactive,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Text(
                        text = "$",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PrimaryDark,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    BasicTextField(
                        value = amount,
                        onValueChange = {amount = it},
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        cursorBrush = SolidColor(PrimaryDark),
                        modifier = Modifier.width(IntrinsicSize.Min),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.heightIn(min = 36.dp),
                                contentAlignment = Alignment.CenterStart) {
                                if (amount.isEmpty()) {
                                    Text(text = "0.00",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Platinum
                                    )
                                }
                                innerTextField()
                            }
                        }


                    )


                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier.width(80.dp),
                thickness = 2.dp,
                color = IncomeGreen
            )

        }

        Column(
            modifier = Modifier.padding(vertical =  12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Category",
                color = LightNavInactive,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryGrid(
                categories = allIncomeCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategoryChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExpenseNoteField(value = expenseNote, onValueChange = {value -> expenseNote = value} )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(selectedDate = selectedDate, onDateChange = onDateChange)
            }

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
        columns = GridCells.Fixed(3),
        modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp), //? Fixed the height of the Grid to Show only 2 Rows
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
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
            Text("What was this for?")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
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
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Picker")
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(),
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


@Preview(showBackground = true)
@Composable
fun AddTxScreenPreview() {
    AddTxScreen(true,
        onClick = {},
        selectedCategory = ExpIncCategory.ExpenseCategory.Food,
        onCategoryChange = {},
        selectedDate = System.currentTimeMillis(),
        onDateChange = {})
}
