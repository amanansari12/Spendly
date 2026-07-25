package com.amanansari.spendly.home.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.data.local.dao.AllocatedBudgetPartialDetails
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.transaction.screen.CategoryGrid
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import com.amanansari.spendly.utils.toCurrencyString
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetInformationModal(
    showBottomSheet: Boolean,
    onDismiss : () -> Unit,
    onViewAllBudgets : () -> Unit,
    partialBudgetDetails : List<AllocatedBudgetPartialDetails?>
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    fun dismissThen(action: () -> Unit) {
        scope.launch { sheetState.hide() }
            .invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismiss()
                    action()
                }
            }
    }

    if(showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LightSurface,
            dragHandle = {
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .width(38.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFDAD6E8))
                )
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 560.dp)   // fixed sheet height feel
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Budget Breakdown",
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 6.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )

                if (partialBudgetDetails.isEmpty()) {
                    NoBudgetAllocatedCard(
                        onViewAllBudgets = { dismissThen(onViewAllBudgets) }
                    )
                } else {
                    BudgetInformationList(
                        partialBudgetDetails = partialBudgetDetails,
                        onViewAllBudgets = { dismissThen(onViewAllBudgets) }
                    )
                }
            }
        }
    }


}


@Composable
private fun BudgetInformationList(
    partialBudgetDetails : List<AllocatedBudgetPartialDetails?>,
    onViewAllBudgets: () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ){

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(partialBudgetDetails.size){ index ->


                        val item = partialBudgetDetails[index]

                        val allocated = item?.allocatedAmount ?: 0L
                        val spent = item?.amountSpent ?: 0L

                        val remaining = BigDecimal(allocated - spent).movePointLeft(2)
                            .toCurrencyString(detectDefaultCurrencyInfo().code)

                        val percentSpent: BigDecimal = if (allocated > 0) {
                            BigDecimal(spent)
                                .divide(BigDecimal(allocated), 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal(100))
                        } else {
                            BigDecimal.ZERO
                        }

                        val budgetStatusColor = when {
                            percentSpent >= BigDecimal(100) -> ExpenseRed
                            percentSpent >= BigDecimal(75) -> Color(0xFFFFDB58) // same amber as the unallocated warning
                            else -> IncomeGreen
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val category = categoryFromId(item?.categoryId)

                            //> Category Icon and Indicator Row

                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CategoryIconBox(category  ?: ExpIncCategory.ExpenseCategory.Misc)

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = category?.title ?: "Uncategorized",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    LinearProgressIndicator(
                                        progress = { (percentSpent / BigDecimal(100)).toFloat().coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(9.dp)
                                            .clip(RoundedCornerShape(50)),
                                        color = budgetStatusColor,
                                        trackColor = Color.LightGray,
                                        strokeCap = StrokeCap.Butt, // Natively rounds the ends of the progress bar
                                        gapSize = 0.dp, // Removes the Material 3 gap
                                        drawStopIndicator = {} // Removes the Material 3 stop indicator
                                    )
                                }
                            }

                            //> Amount Left and Percent Column

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {

                                Text(
                                    text = "$remaining left",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "$percentSpent%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = budgetStatusColor
                                )
                            }
                        }

                        if (index != partialBudgetDetails.lastIndex) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

            }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "View All Budgets",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onViewAllBudgets)
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            color = Primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

    }






}

@Composable
private fun NoBudgetAllocatedCard(
    onViewAllBudgets: () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp),
    ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = "No budget has been allocated yet. \nAdd a budget to see the details. \"",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Button(
                    onClick = onViewAllBudgets,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, PrimaryDark)

                ) {
                    Text(text = "Add Budget")
                }

            }
    }



}