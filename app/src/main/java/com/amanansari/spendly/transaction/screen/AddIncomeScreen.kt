package com.amanansari.spendly.transaction.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.amanansari.spendly.components.AmountInputField
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allIncomeCategories
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightNavInactive
import java.math.BigDecimal

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
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
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

            AmountInputField(IncomeGreen)

        }

        Column(
            modifier = Modifier.padding(vertical =  12.dp, horizontal = 16.dp),
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