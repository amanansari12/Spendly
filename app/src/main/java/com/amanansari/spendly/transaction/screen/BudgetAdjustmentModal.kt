package com.amanansari.spendly.transaction.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.model.categoryFromId
import com.amanansari.spendly.transaction.state.BudgetModalState
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.ui.theme.WarningBackground
import com.amanansari.spendly.ui.theme.WarningText
import com.amanansari.spendly.utils.detectDefaultCurrencyInfo
import com.amanansari.spendly.utils.toCurrencyString
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetAdjustmentModal(
    modalState: BudgetModalState,
    unAllocatedFromBudget : Long,
    onAllocateMore: () -> Unit,
    onMoveFromClick: () -> Unit,
    onPickMoveFromCategory: (String) -> Unit,
    onMoveBack: () -> Unit,
    onLogOverBudget: () -> Unit,
    onDismiss: () -> Unit,
    onClose: ()->Unit
) {
    if (modalState == BudgetModalState.Hidden) return

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = LightSurface
    ) {
        when (modalState) {
            is BudgetModalState.ConfirmOverspend -> ConfirmOverspendContent(
                 modalState,unAllocatedFromBudget, onAllocateMore, onMoveFromClick, onLogOverBudget
            )
            is BudgetModalState.ChooseMoveFrom -> ChooseMoveFromContent(
                modalState, onPickMoveFromCategory, onMoveBack
            )
            is BudgetModalState.Success -> SuccessContent(modalState.message, onDone = onClose, onDismiss = onDismiss)
            BudgetModalState.Hidden -> Unit
        }
    }
}

@Composable
private fun ConfirmOverspendContent(
    modalState: BudgetModalState.ConfirmOverspend,
    unAllocatedFromBudget : Long,
    onAllocateMore: () -> Unit,
    onMoveFromClick: () -> Unit,
    onLogOverBudget: () -> Unit
) {

    val overspend = BigDecimal(modalState.overspend).movePointLeft(2).toCurrencyString(
        detectDefaultCurrencyInfo().code
    )

    val limit = BigDecimal(modalState.limit).movePointLeft(2).toCurrencyString(
        detectDefaultCurrencyInfo().code
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CategoryIconBox(modalState.category)
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = "${modalState.category.title} budget exceeded", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "This transaction goes $overspend over your $limit limit.",
                    fontSize = 13.sp
                )
            }
        }

        Button(
            onClick = onAllocateMore,
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WarningBackground,
                contentColor = WarningText,
                disabledContainerColor = WarningBackground.copy(0.75f),
                disabledContentColor = WarningText.copy(0.75f),
                ),
            border = BorderStroke(1.dp, Color(0xFFFAEBCF)),
            enabled = unAllocatedFromBudget >= modalState.overspend
        ) {
            Text(text = "Allocate $overspend more to $limit")
        }

        Button(
            onClick = onMoveFromClick,
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBg, contentColor = Color.Black),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text(text = "Move $overspend from another category")
        }

        Spacer(modifier = Modifier.height(10.dp))

//        Text(
//            text = "Log it over budget, I'll sort it later",
//            color = Color.Gray,
//            fontSize = 13.sp,
//            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).clickable { onLogOverBudget() }
//        )
    }
}

@Composable
private fun ChooseMoveFromContent(
    modalState: BudgetModalState.ChooseMoveFrom,
    onPickCategory: (String) -> Unit,
    onBack: () -> Unit
) {

    val overspend = BigDecimal(modalState.overspend).movePointLeft(2).toCurrencyString(
        detectDefaultCurrencyInfo().code
    )


    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(text = "Move $overspend from", fontWeight = FontWeight.SemiBold)
        }

        if (modalState.options.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = LightSurface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Primary
                    )

                    Text(
                        text = "No other category has enough remaining budget to cover this expense.\n\nGo back and choose \"Log it over budget\" instead.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            modalState.options.forEach { option ->
                val remaining = option.allocatedAmount - option.amountSpent
                val category = categoryFromId(option.categoryId)
                Button(
                    onClick = { onPickCategory(option.categoryId) },
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val amountRemaining = BigDecimal(remaining).movePointLeft(2)
                            .toCurrencyString(detectDefaultCurrencyInfo().code)
                        Text(text = category?.title ?: option.categoryId)
                        Text(text = "$amountRemaining left", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }
    }

}

@Composable
private fun SuccessContent(message: String, onDone: () -> Unit, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE3F5E1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Done",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(28.dp)
            )
        }

        Text(text = "Saved", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Text(text = message, fontSize = 13.sp, color = Color.Gray)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),

        ){
            Button(onClick = onDone,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
                border = BorderStroke(1.dp, PrimaryDark)
            ) {
                Text("Done")
            }

            Button(onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Text("Add Another")
            }
        }

    }
}



//@Preview(showBackground = true)
//@Composable
//fun BudgetAdjustmentModalPreview() {
//    SpendlyTheme {
//        BudgetAdjustmentModal(
//
//        )
//    }
//}
