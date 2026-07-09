package com.amanansari.spendly.navigation.graph

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.onBoarding.viewmodel.AllocationRow
import com.amanansari.spendly.ui.theme.LightGray
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Primary



@Composable
fun BudgetCard(
    totalIncome : Double,
    item : AllocationRow,
    onAmountChange : (String, String)->Unit
){

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIconBox(category = item.category)

                Column {
                    Text(
                        text = item.category.title,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (totalIncome > 0)
                            String.format(
                                "%.2f%% of income",
                                (item.amount / totalIncome) * 100
                            )
                        else
                            "Not allocated yet",
                        fontSize = 10.sp,
                        color = Color.DarkGray
                    )
                }
            }

            BasicTextField(
                value = item.amountText,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d{0,10}(\\.\\d{0,2})?$"))) {
                        val parsedValue = newValue.toDoubleOrNull() ?: 0.0
                        if (parsedValue <= totalIncome) {
                            onAmountChange(item.category.id, newValue)
                        }
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(36.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black,
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                cursorBrush = SolidColor(Primary),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = item.amountText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,

                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        placeholder = {
                            Text(text = "0.00", fontSize = 12.sp, color = Color.DarkGray)
                        },
                        leadingIcon = {
                            Text(
                                text = "₹",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = LightGray.copy(alpha = 0.5f),
                                    unfocusedContainerColor = LightGray.copy(alpha = 0.5f),
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Primary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    )
                }
            )
        }
    }


}