package com.amanansari.spendly.transaction.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.AddIncomeExpenseCategoryItem
import com.amanansari.spendly.components.AmountInputField
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.utils.formatDate
import androidx.compose.ui.tooling.preview.Preview
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.SpendlyTheme
import com.amanansari.spendly.ui.theme.WarningOrange
import com.amanansari.spendly.utils.toCurrencyString
import java.math.BigDecimal
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

    val spent = BigDecimal(state.amountSpentToCategory).movePointLeft(2)
        .toCurrencyString(state.defaultCurrency.code)

    val allocated = BigDecimal(state.allocatedAmountToCategory).movePointLeft(2)
        .toCurrencyString(state.defaultCurrency.code)

    val remaining = BigDecimal(state.allocatedAmountToCategory - state.amountSpentToCategory).movePointLeft(2)
        .toCurrencyString(state.defaultCurrency.code)

    val budgetStatusColor = when {
        state.budgetSpentPercentage >= BigDecimal(100) -> ExpenseRed
        state.budgetSpentPercentage >= BigDecimal(75) -> Color(0xFFFFDB58) // same amber as the unallocated warning
        else -> IncomeGreen
    }

    val categorySelected = categoryFromId(state.categoryId)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ){

            item {
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

                    if(state.categoryId.isNotBlank() && state.type == TransactionType.EXPENSE){
                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 2.dp
                            ).background(
                                color = Color(0xFFE65100).copy(0.85f),
                                shape = RoundedCornerShape(18.dp)
                            )
                        ){
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {

                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    modifier = Modifier.size(10.dp),
                                    tint = LightSurface
                                )

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                color = LightSurface,
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append(remaining)
                                        }

                                        append(" left in the ")

                                        append(categorySelected?.title ?: "")

                                        append(" Budget")
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFE8E6E6)
                                )
                            }

                        }
                    }

                }
            }

            item {
                Column(
                    modifier = Modifier.padding(top =  12.dp, bottom = 8.dp, start = 10.dp, end = 10.dp),
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

                    if(state.categoryId.isNotBlank() && state.type == TransactionType.EXPENSE){
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFFFF3CD),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ){
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(modifier = Modifier
                                    .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${categorySelected?.title}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                    Text(
                                        text = "$spent / $allocated",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LightTextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                LinearProgressIndicator(
                                    progress = { state.budgetSpentPercentage.toFloat().coerceIn(0f, 1f) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(9.dp)
                                        .clip(RoundedCornerShape(50)),
                                    color = budgetStatusColor,
                                    trackColor = LightNavInactive,
                                    strokeCap = StrokeCap.Butt, // Natively rounds the ends of the progress bar
                                    gapSize = 0.dp, // Removes the Material 3 gap
                                    drawStopIndicator = {} // Removes the Material 3 stop indicator
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$remaining left before this transaction",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LightTextSecondary

                                )
                            }
                        }
                    }

                }
            }


            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 10.dp),
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

    val columns = 2
    val singleRowHeight = 84.dp   // enough for 1 row
    val twoRowHeight = 180.dp
    val gridHeight = if (categories.size <= columns) singleRowHeight else twoRowHeight

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(gridHeight),
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

@Preview(showBackground = true)
@Composable
fun TransactionFormPreview() {
    SpendlyTheme {
        TransactionForm(
            state = TransactionUiState(),
            categories = allExpenseCategories,
            onCategoryChange = {},
            onAmountChange = {},
            onDateChange = {},
            onNoteChange = {},
            accentColor = Color(0xFFE65100)
        )
    }
}
