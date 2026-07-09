package com.amanansari.spendly.onBoarding.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allIncomeCategories
import com.amanansari.spendly.onBoarding.state.IncomeSourceUistate
import com.amanansari.spendly.ui.theme.LightGray
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark

@Composable
fun IncomeSourceScreen(
    state: IncomeSourceUistate,
    onContinue : () -> Unit,
    onSkip: () -> Unit,
    onIncomeToggle : (String) -> Unit,
    onPrevStep : () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
    ) {
        OnboardingTopBar(3,3, onPrevStep)

        Spacer(modifier = Modifier.height(15.dp))

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Where's this money from?",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp
        )

        Text(
            text = "This helps us track recurring income later.",
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = Color.Gray

        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(12.dp)
        ) {

            items(state.availableIncomeSource){ category ->

                val isSelected = category.id == state.selectedIncomeSourceId

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onIncomeToggle(category.id)
                        },

                    border = if (isSelected)
                        BorderStroke(2.dp, category.color)
                    else
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            category.color.copy(alpha = 0.1f)
                        else
                            LightSurface
                    )
                ) {

                    Box {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryIconBox(
                                category = category,
                            )

                            Text(
                                text = category.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )


                        }

                        if(isSelected){
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

            }

        }

        Spacer(modifier = Modifier.weight(1f))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            Button(
                onClick = {onSkip()},
                enabled = true,
                modifier = Modifier
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // background color
                    contentColor = Color.Black,

                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.DarkGray,
                ),
            ) {
                Text(
                    text = "Skip For Now",
                    fontWeight = FontWeight.Bold,
                )
            }

            Button(onClick = {onContinue()},
                enabled = true,
                modifier = Modifier
                    .height(50.dp)
                    .width(2500.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary, // background color
                    contentColor = Color.White,

                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.DarkGray,
                ),
            ) {
                Text(
                    text = "Continue",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }


    }

    }





}

@Preview(showBackground = true)
@Composable
fun IncomeSourceScreenPreview() {
    IncomeSourceScreen(
        state = IncomeSourceUistate(
            availableIncomeSource = allIncomeCategories,
            selectedIncomeSourceId = ExpIncCategory.IncomeCategory.Salary.id
        ),
        onContinue = {},
        onSkip = {},
        onIncomeToggle = {},
        onPrevStep = {}
    )
}