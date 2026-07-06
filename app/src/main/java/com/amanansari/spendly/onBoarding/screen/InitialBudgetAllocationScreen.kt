package com.amanansari.spendly.onBoarding.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.Platinum
import com.amanansari.spendly.ui.theme.Primary

@Composable
fun InitialBudgetAllocationScreen(){

    val rupee = "\u20B9"

    Column(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        horizontalAlignment = Alignment.Start
    ) {

        OnboardingTopBar(3, 3)

        Spacer(modifier = Modifier.height(60.dp))

        Column() {
            Text(
                text = "Allocate Budgets",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )

            Text(
                text = "Distribute your income across essential categories to track your spending habits.",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.Gray

            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    color  = Platinum.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
            ){

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOTAL BALANCE",
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Text(
                        text = "LEFT TO ALLOCATE",
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = rupee + "50,000.00",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    Text(
                        text = rupee + "12,000.00",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Primary
                    )
                }

                Column() {
                    LinearProgressIndicator(
                        progress = { 0.68f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .height(9.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Primary,
                        trackColor = LightNavInactive,
                        strokeCap = StrokeCap.Butt, // Natively rounds the ends of the progress bar
                        gapSize = 0.dp, // Removes the Material 3 gap
                        drawStopIndicator = {} // Removes the Material 3 stop indicator
                    )


                    // ? How Much Budget Used and Left.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "76% Allocated",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = rupee+"38,000/" +rupee+"50,000",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

            }
        }


    }

}
@Preview(showBackground = true)
@Composable
fun InitialBudgetAllocationScreenPreview(){
    InitialBudgetAllocationScreen()
}