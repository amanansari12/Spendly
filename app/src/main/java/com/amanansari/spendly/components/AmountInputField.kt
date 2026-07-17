package com.amanansari.spendly.components

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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.ui.theme.Platinum
import com.amanansari.spendly.ui.theme.PrimaryDark


private const val MAX_AMOUNT_LENGTH = 12
private val amountRegex = Regex("^\\d{0,10}(\\.\\d{0,2})?$")

@Composable
fun AmountInputField(
    amount: TextFieldValue,
    onAmountChange: (String) -> Unit,
    currencySymbol: String,
    colorProvided: Color = PrimaryDark
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currencySymbol,
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp,
                    color = colorProvided
                )

                Spacer(modifier = Modifier.width(15.dp))

                BasicTextField(
                    value = amount,
                    onValueChange = { newValue ->

                        val newText = newValue.text
                        if (newText.length <= MAX_AMOUNT_LENGTH && newText.matches(amountRegex)) {

                            onAmountChange(newText)
                        }

                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    cursorBrush = SolidColor(colorProvided),
                    textStyle = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .widthIn(min = 120.dp)
                        .heightIn(min = 56.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .heightIn(min = 56.dp)
                                .padding(4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (amount.text.isEmpty()) {
                                Text(
                                    text = "0.00",
                                    fontSize = 38.sp,
                                    lineHeight = 44.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Platinum
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = colorProvided
            )
        }
    }
}