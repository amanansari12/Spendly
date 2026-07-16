package com.amanansari.spendly.onBoarding.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.model.CurrencyInfo
import com.amanansari.spendly.onBoarding.state.UserInfoUiState
import com.amanansari.spendly.onBoarding.viewmodel.UserInfoStep
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Platinum
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.text.isEmpty

data class QuickAmount(
    val label : String,
    val amount : Long
)

val quickAmounts = listOf(
    QuickAmount("10K", 1_000_000L),  // ₹10,000.00
    QuickAmount("20K", 2_000_000L),  // ₹20,000.00
    QuickAmount("30K", 3_000_000L),  // ₹30,000.00
    QuickAmount("40K", 4_000_000L),  // ₹40,000.00
    QuickAmount("50K", 5_000_000L),  // ₹50,000.00
    QuickAmount("1L", 10_000_000L),  // ₹1,00,000.00
    QuickAmount("2L", 20_000_000L),  // ₹2,00,000.00
    QuickAmount("5L", 50_000_000L)   // ₹5,00,000.00
)

private const val MAX_AMOUNT_LENGTH = 12
private val amountRegex = Regex("^\\d{0,10}(\\.\\d{0,2})?$")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InitialBudgetScreen(
    state : UserInfoUiState,
    onAmountChange : (String)->Unit,
    onNextStep : ()->Unit,
    onPrevStep : () -> Unit
) {

    val firstname = state.name.trim().split(" ").firstOrNull() ?: "User"
    val currentMonth = LocalDate.now().month.name



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.Start
    ) {

        OnboardingTopBar(2, 3, onBackClick = onPrevStep)

        Spacer(modifier = Modifier.height(30.dp))

        Column() {
            Text(
                text = "Welcome, $firstname!",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp

            )

            Text(
                text = "How much are you starting with this month?",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Gray

            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = state.currency.symbol,
                        fontWeight = FontWeight.Bold,
                        fontSize = 50.sp,
                        color = PrimaryDark,

                        )
                    Spacer(Modifier.width(15.dp))

                    BasicTextField(
                        value = state.amountFieldValue,
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
                        cursorBrush = SolidColor(PrimaryDark),
                        textStyle = TextStyle(
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .widthIn(min = 120.dp)
                            .heightIn(min = 56.dp),
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.heightIn(min = 56.dp).padding(4.dp),
                                contentAlignment = Alignment.CenterStart) {
                                if (state.amountFieldValue.text.isEmpty()) {
                                    Text(text = "0.00",
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
                    modifier = Modifier.fillMaxWidth(), // <-- now matches the Row above it exactly
                    thickness = 2.dp,
                    color = PrimaryDark
                )


            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column() {
            Text(
                text = "QUICK SELECT",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ){
                items(quickAmounts) { quickAmount ->

                    val isSelected = state.initialAmount == quickAmount.amount

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected && state.initialAmount > 0L) PrimaryDark.copy(alpha = 0.15f)
                                else Color.LightGray.copy(alpha = 0.3f)
                            )
                            .border(
                                width = 2.dp,
                                color = if (isSelected && state.initialAmount > 0.0) Primary.copy(alpha = 0.4f) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                val newText = BigDecimal(quickAmount.amount)
                                    .movePointLeft(2)
                                    .toPlainString()
                                onAmountChange(newText)
                            }
                            .padding(horizontal = 15.dp, vertical = 10.dp)

                    ) {
                        Text(
                            text = state.currency.symbol + quickAmount.label,
                            color = if (isSelected) Primary else Color.Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 15.sp
                        )
                    }

                }
            }
        }
        Spacer(modifier = Modifier.height(25.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.DarkGray.copy(alpha = 0.1f))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(Primary)
                )

                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Primary
                )

                Text(
                    text = buildAnnotatedString {
                        append("This is your total pool for ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(currentMonth)
                        }
                        append(". You will divide this into budgets next.")
                    },
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onNextStep() },
            enabled = state.initialAmount > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray,
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Next Step",
                    fontWeight = FontWeight.Bold,
                    color = LightSurface
                )

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = LightSurface,
                    modifier = Modifier.size(18.dp)
                )
            }
        }




    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun InitialBudgetScreenPreview(){
    InitialBudgetScreen(
        state = UserInfoUiState(
            name = "Aman",
            email = "aman@email.com",
            initialAmount = 12000078L,
            amountFieldValue = TextFieldValue("120000.78"),
            currentUserInfoStep = UserInfoStep.EMAIL,
            currency = CurrencyInfo("INR", "₹")
        ),
        onAmountChange = {_ ->},
        onNextStep = {},
        onPrevStep = {}
    )
}