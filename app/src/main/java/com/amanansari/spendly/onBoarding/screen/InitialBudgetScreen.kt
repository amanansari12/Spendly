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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.onBoarding.state.UserInfoUiState
import com.amanansari.spendly.onBoarding.viewmodel.UserInfoStep
import com.amanansari.spendly.ui.theme.Platinum
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.utils.formatAmount
import java.time.LocalDate
import kotlin.text.isEmpty

data class QuickAmount(
    val label : String,
    val amount : Double
)

val quickAmounts = listOf(
    QuickAmount("10K", 10_000.0),
    QuickAmount("20K", 20_000.0),
    QuickAmount("30K", 30_000.0),
    QuickAmount("40K", 40_000.0),
    QuickAmount("50K", 50_000.0),
    QuickAmount("1L", 100_000.0),
    QuickAmount("2L", 200_000.0),
    QuickAmount("5L", 500_000.0)
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InitialBudgetScreen(
    state : UserInfoUiState,
    onAmountChange : (Double)->Unit,
    onNextStep : ()->Unit
) {
    var amountText by remember { mutableStateOf("") }
    val firstname = state.name.trim().split(" ").firstOrNull() ?: "User"
    val currentMonth = LocalDate.now().month.name

    Column(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        horizontalAlignment = Alignment.Start
    ) {

        OnboardingTopBar(2, 3)

        Spacer(modifier = Modifier.height(60.dp))

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
                        text = "$",
                        fontWeight = FontWeight.Bold,
                        fontSize = 50.sp,
                        color = PrimaryDark,

                        )
                    Spacer(Modifier.width(15.dp))

                    BasicTextField(
                        value = formatAmount(amountText),
                        onValueChange = { newValue ->

                            if(newValue.matches(Regex("^\\d*\\.?\\d*$"))){
                                amountText = newValue
                                onAmountChange(newValue.toDoubleOrNull() ?: 0.0)
                            }

                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                                if (amountText.isEmpty()) {
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
                                if (isSelected) PrimaryDark.copy(alpha = 0.15f)
                                else Color.LightGray.copy(alpha = 0.3f)
                            )
                            .border(
                                width = 2.dp,
                                color = if (isSelected) Primary.copy(alpha = 0.4f) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                amountText = quickAmount.amount.toString()
                                onAmountChange(quickAmount.amount)
                            }
                            .padding(horizontal = 15.dp, vertical = 10.dp)

                    ) {
                        Text(
                            text = "$"+ quickAmount.label,
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

        Button(onClick = {onNextStep()},
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary, // background color
                contentColor = Color.White,

                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray,
            ),
        ) {
            Text(
                text = "Next Step -> ",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
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
            initialAmount = 120000.78,
            currentStep = UserInfoStep.EMAIL
        ),
        onAmountChange = {},
        onNextStep = {}
    )
}