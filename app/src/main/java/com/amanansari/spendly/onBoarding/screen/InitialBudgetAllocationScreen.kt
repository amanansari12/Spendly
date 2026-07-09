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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.components.SwipeToDelete
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.navigation.graph.BudgetCard
import com.amanansari.spendly.onBoarding.state.BudgetAllocationUiState
import com.amanansari.spendly.onBoarding.viewmodel.AllocationRow
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightGray
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.ui.theme.SpendlyTheme

@Composable
fun InitialBudgetAllocationScreen(
    state: BudgetAllocationUiState,
    onAmountChange: (String, String) -> Unit,
    onRemoveCategoryClick: (String) -> Unit,
    onAddCategoryClick: () -> Unit,
    onCategoryToggle: (String) -> Unit,
    onConfirmSelection: () -> Unit,
    onDismissPicker: () -> Unit,
    onPrevStep: () -> Unit,
    onFinishClick: () -> Unit,
){

    val rupee = "\u20B9"

    val progressFraction = if (state.totalIncome > 0)
        (state.totalAllocated / state.totalIncome).toFloat().coerceIn(0f, 1f)
    else 0f




    if(state.isCategoryPickerVisible){
        CategoryPickerForAllocation(
            state = state,
            onCategoryToggle = onCategoryToggle,
            onConfirmSelection = onConfirmSelection,
            onDismiss = onDismissPicker
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),

            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                OnboardingTopBar(3, 3, onPrevStep)
            }

            item {
                Column() {
                    Text(
                        text = "Allocate Budgets",
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp
                    )

                    Text(
                        text = "Distribute your income across essential categories to track your spending habits.",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.Gray

                    )
                }

            }

            stickyHeader {


                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = LightSurface,

                    shadowElevation = 6.dp
                ){

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(
//                                color = Platinum.copy(alpha = 0.4f),
                                color = LightSurface,
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        ){

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "TOTAL BALANCE",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )

                                Text(
                                    text = "LEFT TO ALLOCATE",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = Primary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = rupee + "${state.totalIncome}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )

                                Text(
                                    text = rupee + "${state.remainingToAllocate}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Primary
                                )
                            }

                            Column() {
                                LinearProgressIndicator(
                                    progress = { progressFraction  },
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
                                        text = buildAnnotatedString {
                                            withStyle(
                                                SpanStyle(
                                                    color = IncomeGreen,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            ) {
                                                append(String.format("%.2f", (state.totalAllocated / state.totalIncome) * 100))
                                                append("%")
                                            }
                                            append(" Allocated")
                                        },
                                        fontSize = 14.sp,
                                        color = Color.DarkGray,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = rupee + state.totalAllocated + "/" + rupee + state.totalIncome,
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

            items(
                items = state.allocations,
                key = {it.rowId}
            ) {item ->

                        SwipeToDelete(
                            onDelete = { onRemoveCategoryClick(item.rowId) },
                            modifier = Modifier
                                .padding(bottom = 4.dp)

                        ) {
                            BudgetCard(totalIncome = state.totalIncome, item, onAmountChange)
                        }


            }

            item {
                OutlinedButton(
                    onClick = { onAddCategoryClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 10.dp)
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val dash = 10.dp.toPx()
                            val gap = 6.dp.toPx()

                            drawRoundRect(
                                color = Color.Gray,
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(dash, gap)
                                    )
                                ),
                                cornerRadius = CornerRadius(14.dp.toPx())
                            )
                        },
                    shape = RoundedCornerShape(14.dp),
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Add another category",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }




        }


        Button(onClick = {
            onFinishClick()
        },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary, // background color
                contentColor = Color.White,

                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray,
            ),
        ) {
            Text(
                text = "Finish & Save Remaining",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerForAllocation(state: BudgetAllocationUiState,onCategoryToggle: (String) -> Unit,onConfirmSelection: () -> Unit,onDismiss: () -> Unit){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        containerColor = LightSurface
    ) {

        val selectedCategoryCount = state.selectedCategoryIds.count()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Select Expense Categories",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp
            )

            Text(
                text = "Choose a Category for this Budget",
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = Color.Gray

            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {

                items(state.availableCategoriesForPicker){ category ->

                    val isSelected = category.id in state.selectedCategoryIds

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCategoryToggle(category.id)
                            },

                        border = if (isSelected)
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
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
                                        .background(MaterialTheme.colorScheme.primary),
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

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly

                ){
                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGray, // background color
                            contentColor = Color.Black,

                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.DarkGray,
                        ),
                        onClick = { onDismiss() }
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary, // background color
                            contentColor = LightSurface,

                            disabledContainerColor = PrimaryDark.copy(alpha = 0.5f),
                            disabledContentColor = LightSurface,
                        ),
                        enabled = selectedCategoryCount > 0,
                        onClick = {onConfirmSelection()}
                    ) {
                        Text("Add Categories")
                    }
                }
            }





        }

    }

}

@Preview(showBackground = true, name = "Partially Allocated")
@Composable
fun InitialBudgetAllocationScreenPreview() {
    val sampleState = BudgetAllocationUiState(
        totalIncome = 50000.0,
        allocations = listOf(
            AllocationRow(category = ExpIncCategory.ExpenseCategory.Food, amount = 8000.0)
            ,AllocationRow(category = ExpIncCategory.ExpenseCategory.Transport, amount = 2000.0),
            AllocationRow(category = ExpIncCategory.ExpenseCategory.Shopping, amount = 8000.0)),
        availableCategoriesForPicker = allExpenseCategories.filterNot { candidate ->candidate.id in setOf("food", "transport", "shopping")},
        isCategoryPickerVisible = false,
        selectedCategoryIds = emptySet())

    SpendlyTheme {  // swap for your actual theme composable name
        InitialBudgetAllocationScreen(
            state = sampleState,
            onAmountChange = { _, _ -> },
            onAddCategoryClick = {},
            onRemoveCategoryClick = {},
            onCategoryToggle = {},
            onConfirmSelection = {},
            onDismissPicker = {},
            onPrevStep = {},
            onFinishClick = {},
        )
    }

}