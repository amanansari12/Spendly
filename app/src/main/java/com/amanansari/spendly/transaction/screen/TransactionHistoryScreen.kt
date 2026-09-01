package com.amanansari.spendly.transaction.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.transaction.viewmodel.TransactionHistoryViewModel
import com.amanansari.spendly.transaction.state.TransactionHistoryUiState
import com.amanansari.spendly.ui.theme.Primary
import java.time.Month
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.amanansari.spendly.components.MonthYearWheelPicker
import com.amanansari.spendly.components.TransactionTypeToggle
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.utils.monthKeyToDisplayLabel
import com.amanansari.spendly.utils.monthYear
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionHistoryScreen(
    historyViewModel: TransactionHistoryViewModel = hiltViewModel(),
    onBack: () -> Unit
){

    TransactionHistoryScreenContent(
        state = historyViewModel.UiState,
        onBack = onBack,
        selectedCategory = historyViewModel.selectedCategory,
        onDateChange = {
            historyViewModel.updateDate(it)
        },
        updateType = {
            historyViewModel.updateType(it)
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionHistoryScreenContent(
    state : TransactionHistoryUiState,
    selectedCategory : List<String>,
    onBack: () -> Unit,
    onDateChange : (String) -> Unit,
    updateType : (TransactionType)->Unit
){

    var showDatePicker by remember { mutableStateOf<Boolean>(false) }
    var showCategoryFilter by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        HistoryTopBar(onBack = onBack)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = {
                        showDatePicker = true
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ){

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Date Picker",
                        tint = Primary
                    )
                    Text(
                        text = if (state.selectedMonth.isEmpty()) monthYear(null)
                        else monthKeyToDisplayLabel(state.selectedMonth),
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,

                        )
                }

            }

            ElevatedButton(
                onClick = {
                    showCategoryFilter = true
                },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = "Category Filter",
                        tint = Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "All Categories",
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                        )
                }
            }
        }

    }


    if (showDatePicker) {
        FilterByDate(
            selectedDate = state.selectedMonth,
            onDateChange = onDateChange,
            closePicker = {
                showDatePicker = false
            }
        )
    }

    if(showCategoryFilter){
        FilterByCategory(
            isExpense = state.selectedType == TransactionType.EXPENSE,
            selectedCategory = selectedCategory,
            closeFilter = {
                showCategoryFilter = false
            },
            updateType = updateType
        )
    }

}


@Composable
fun HistoryTopBar(
    onBack: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back Button",
                tint = Primary,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onBack()
                    }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Transactions",
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp
            )
        }

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Primary,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    // Handle click
                }
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterByDate(
    selectedDate : String,
    onDateChange : (String) -> Unit,
    closePicker : () -> Unit
){

    val calendar = Calendar.getInstance()

    // Seed from whatever month is already selected (if any), instead of
    // always defaulting to today's real date.
    val initialYearMonth = remember(selectedDate) {
        selectedDate.takeIf { it.isNotEmpty() }?.let { YearMonth.parse(it) }
    }

    var selectedMonth by remember {
        mutableIntStateOf((initialYearMonth?.monthValue ?: (calendar.get(Calendar.MONTH) + 1)) - 1)
    }

    var selectedYear by remember {
        mutableIntStateOf(
            initialYearMonth?.year ?: calendar.get(Calendar.YEAR)
        )
    }

    // ============================================================
    // WHEEL PICKER INTEGRATION STARTS HERE
    // This replaces the old two-DropdownMenuBox Row that used to sit
    // inside the `text = { ... }` slot below.
    // ============================================================
    val monthLabels = remember {
        (1..12).map { Month.of(it).getDisplayName(TextStyle.SHORT, Locale.getDefault()) }
    }
    val yearLabels = remember { (selectedYear - 10..selectedYear + 10).map { it.toString() } }
    val selectedYearIndex = yearLabels.indexOf(selectedYear.toString()).coerceAtLeast(0)

    AlertDialog(
        onDismissRequest = closePicker,

        title = {
            Text("Select Month")
        },

        text = {
            MonthYearWheelPicker(
                months = monthLabels,
                years = yearLabels,
                selectedMonthIndex = selectedMonth,
                selectedYearIndex = selectedYearIndex,
                onMonthChange = { selectedMonth = it },
                onYearChange = { index -> selectedYear = yearLabels[index].toInt() }
            )
        },
        // ============================================================
        // WHEEL PICKER INTEGRATION ENDS HERE
        // ============================================================

        confirmButton = {
            TextButton(
                onClick = {
                    val monthKey = "%04d-%02d".format(selectedYear, selectedMonth + 1)

                    onDateChange(monthKey)
                    closePicker()
                }
            ) {
                Text("OK")
            }
        },

        dismissButton = {
            TextButton(
                onClick = closePicker
            ) {
                Text("Cancel")
            }
        }
    )



}



@Composable
fun FilterByCategory(
    isExpense : Boolean,
    selectedCategory: List<String>,
    closeFilter : () -> Unit,
    updateType : (TransactionType)->Unit
){
    AlertDialog(
        onDismissRequest = {},

        containerColor = Color.White,

        title = {
            Text(
                text = "Select Type and Category",
                fontSize = 18.sp,
                color = Color.Black
                )
        },

        text = {
            TypeAndCategory(
                isExpense = isExpense,
                selectedCategoryId = selectedCategory,
                updateType = updateType
            )


        },
        // ============================================================
        // WHEEL PICKER INTEGRATION ENDS HERE
        // ============================================================

        confirmButton = {
            TextButton(
                onClick = {

                }
            ) {
                Text("OK")
            }
        },

        dismissButton = {
            TextButton(
                onClick = {
                    closeFilter()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TypeAndCategory(
    isExpense: Boolean,
    selectedCategoryId : List<String>,
    updateType : (TransactionType)->Unit

){

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TransactionTypeToggle(
            isExpense = isExpense,
            onTypeChanged = { isExpense ->

                if (isExpense) {
                    updateType(TransactionType.EXPENSE)
                }
                else{
                    updateType(TransactionType.INCOME)
                }
            },
            height = 45.dp,
            itemHeight = 35.dp,
            fontSize = 13.sp,
            iconSize = 13.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp)
        ) {
            items(selectedCategoryId) { category ->

//                val selected = category == selectedCategoryId
//
//                    Row(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(50))
//                            .background(
//                                if (selected) Primary.copy(alpha = 0.12f)
//                                else LightBg
//                            )
//                            .clickable(onClick = toggleCategory(category.id))
//                            .padding(
//                                horizontal = 14.dp,
//                                vertical = 8.dp
//                            ),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(6.dp)
//                    ) {
//
//                        if (selected) {
//                            Icon(
//                                imageVector = Icons.Default.Check,
//                                contentDescription = "Selected",
//                                modifier = Modifier.size(16.dp),
//                                tint = Primary
//                            )
//                        }
//
//                        Text(
//                            text = category.name,
//                            fontSize = 14.sp,
//                            fontWeight = if (selected)
//                                FontWeight.SemiBold
//                            else
//                                FontWeight.Normal,
//                            color = if (selected)
//                                Primary
//                            else
//                                Color.DarkGray
//                        )
//                    }
//
            }
        }



    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TransactionHistoryScreenContentPreview(){
    TransactionHistoryScreenContent(
        state = TransactionHistoryUiState(),
        selectedCategory = listOf("Food", "Entertainment", "Groceries"),
        onBack = {},
        onDateChange = {},
        updateType = {}
    )
}