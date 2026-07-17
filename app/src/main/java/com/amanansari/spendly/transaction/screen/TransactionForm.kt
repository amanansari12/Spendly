package com.amanansari.spendly.transaction.screen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.AddIncomeExpenseCategoryItem
import com.amanansari.spendly.components.AmountInputField
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun<T : ExpIncCategory> TransactionForm(
    state : TransactionUiState,
    categories : List<T>,
    onCategoryChange : (String) -> Unit,
    onAmountChange : (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onNoteChange : (String) -> Unit,
    accentColor: Color,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "AMOUNT",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            AmountInputField(
                amount = state.amountText,
                onAmountChange = onAmountChange,
                currencySymbol = state.currency.symbol,
                colorProvided = accentColor
            )

        }

        Column(
            modifier = Modifier.padding(vertical =  12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Category",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategoryGrid(
                categories = categories,
                selectedCategoryId = state.categoryId,
                onCategoryChange = onCategoryChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExpenseNoteField(value = state.note, onValueChange = {value -> onNoteChange(value)} )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerField(selectedDate = state.date, onDateChange = { onDateChange(it) })
            }

        }
    }
}





@Composable
fun<T : ExpIncCategory> CategoryGrid(
    categories: List<T>,
    selectedCategoryId: String,
    onCategoryChange: (String) -> Unit
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
                isSelected = category.id == selectedCategoryId,
                onCategoryChange = { onCategoryChange(category.id) }
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

