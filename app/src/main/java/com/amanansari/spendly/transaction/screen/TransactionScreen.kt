package com.amanansari.spendly.transaction.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.amanansari.spendly.components.TransactionTypeToggle
import com.amanansari.spendly.data.local.entity.TransactionType
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.model.allIncomeCategories
import com.amanansari.spendly.transaction.state.BudgetModalState
import com.amanansari.spendly.transaction.state.TransactionUiState
import com.amanansari.spendly.transaction.viewmodel.TransactionCompletionState
import com.amanansari.spendly.transaction.viewmodel.TransactionViewModel
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.SpendlyTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionScreen(
    transactionViewmodel : TransactionViewModel = hiltViewModel(),
    onClose: () -> Unit,
    onViewAllBudgets : () -> Unit,
    quickSelectedCategoryId : String? = null
){
    LaunchedEffect(quickSelectedCategoryId) {
        transactionViewmodel.toggleCategoryId(quickSelectedCategoryId)
    }

//    //> React to completion state changes instead of checking once
//    LaunchedEffect(transactionViewmodel.completionState, transactionViewmodel.budgetModalState) {
//        if (transactionViewmodel.completionState == TransactionCompletionState.Success
//            &&
//            transactionViewmodel.budgetModalState == BudgetModalState.Hidden
//            ) {
//            onClose()
//        }
//    }

    TransactionScreenContent(
        state = transactionViewmodel.uiState,
        budgetModalState = transactionViewmodel.budgetModalState,
        onClose = onClose,
        onCategoryChange = {
            transactionViewmodel.toggleCategoryId(it)
        },
        onAmountChange = {
            transactionViewmodel.updateAmount(it)
        },
        onNoteChange = {
            transactionViewmodel.updateNote(it)
        },
        onDateChange = {
            transactionViewmodel.updateDate(it)
        },
        onTypeChange = {
            transactionViewmodel.updateType(it)
        },
        onSubmit = {
            transactionViewmodel.completeTransaction()
        },
        onAllocateMore = { transactionViewmodel.allocatedMoreAndSave() },
        onMoveFromClick = { transactionViewmodel.openMoveFrom() },
        onMoveBack = { transactionViewmodel.goBackFromMoveFrom() },
        onPickMoveFromCategory = { transactionViewmodel.moveFromAndSave(it) },
        onLogOverBudget = { transactionViewmodel.logOverBudgetAndSave() },
        onDismissBudgetModal = { transactionViewmodel.dismissModal() },
        onErrorShown = { transactionViewmodel.clearError() },
        onViewAllBudgets = onViewAllBudgets
    )
}

@Composable
fun TransactionScreenContent(
    state : TransactionUiState,
    budgetModalState: BudgetModalState,
    onClose: ()->Unit,
    onCategoryChange : (String) ->Unit,
    onAmountChange : (String) -> Unit,
    onNoteChange : (String) -> Unit,
    onDateChange : (Long) -> Unit,
    onTypeChange : (TransactionType) -> Unit,
    onSubmit : () -> Unit,
    onAllocateMore: () -> Unit,
    onMoveFromClick: () -> Unit,
    onMoveBack : () -> Unit,
    onPickMoveFromCategory: (String) -> Unit,
    onLogOverBudget: () -> Unit,
    onDismissBudgetModal: () -> Unit,
    onErrorShown: () -> Unit,
    onViewAllBudgets : () -> Unit
    )
{

    val pagerState = rememberPagerState(pageCount = { 2 }) // 0 = Expense, 1 = Income
    val scope = rememberCoroutineScope()
    val isExpense = pagerState.currentPage == 0

    val allocatedExpenseCategories = allExpenseCategories.filter { category ->
        state.allocatedBudgets.any { it.categoryId == category.id }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isExpense) {
        onTypeChange(if (isExpense) TransactionType.EXPENSE else TransactionType.INCOME)
    }

    BudgetAdjustmentModal(
        modalState = budgetModalState,
        unAllocatedFromBudget = state.unAllocatedFromBudget,
        onAllocateMore = onAllocateMore,
        onMoveFromClick = onMoveFromClick,
        onMoveBack = onMoveBack,
        onPickMoveFromCategory = onPickMoveFromCategory,
        onLogOverBudget = onLogOverBudget,
        onDismiss = onDismissBudgetModal,
        onClose = onClose
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            TransactionTopBar(isExpense = isExpense,onCloseClick = onClose)

            LaunchedEffect(state.errorMessage) {
                state.errorMessage?.let { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                        onErrorShown()
                    }
                }
            }

            TransactionTypeToggle(isExpense, onTypeChanged = {expenseSelected ->
                scope.launch {
                    pagerState.animateScrollToPage(if (expenseSelected) 0 else 1)
                }})

            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
                    .weight(1f)
            ) { page ->

                if (page == 0) {

                    TransactionForm(
                        state = state,
                        categories = allocatedExpenseCategories,
                        onCategoryChange = onCategoryChange,
                        onAmountChange = onAmountChange,
                        onDateChange = onDateChange,
                        onNoteChange = onNoteChange,
                        accentColor = ExpenseRed,
                        onViewAllBudgets = onViewAllBudgets
                    )

                } else {
                    TransactionForm(
                        state = state,
                        categories = allIncomeCategories,
                        onCategoryChange = onCategoryChange,
                        onAmountChange = onAmountChange,
                        onDateChange = onDateChange,
                        onNoteChange = onNoteChange,
                        accentColor = IncomeGreen,
                        onViewAllBudgets = onViewAllBudgets
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val accent by animateColorAsState(
                targetValue = if (isExpense) ExpenseRed else IncomeGreen,
                label = "saveAccent"
            )

            Button(onClick = {
                onSubmit()
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    disabledContainerColor = accent.copy(0.6f)
                    ),
                enabled = if (isExpense) {
                    allocatedExpenseCategories.isNotEmpty()
                } else{
                    true
                }
                ) {

                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isExpense) "Save expense" else "Save income",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


        }

    }





}



@Composable
fun TransactionTopBar(isExpense: Boolean,
                onCloseClick: () -> Unit,
){
    //? Add Transaction Top Level Bar
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = if (isExpense) "Add Transaction" else "Add Income",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(50)
                )
                .clickable { onCloseClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(35.dp),

                )
        }






    }
}




@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview(){
            TransactionScreenContent(
                state = TransactionUiState(),
                budgetModalState = BudgetModalState.Hidden,
                onClose = {},
                onCategoryChange = {},
                onAmountChange = {},
                onNoteChange = {},
                onDateChange = {},
                onTypeChange = {},
                onSubmit = {},
                onAllocateMore = {  },
                onMoveFromClick = {  },
                onMoveBack = {},
                onPickMoveFromCategory = {  },
                onLogOverBudget = {  },
                onDismissBudgetModal = {},
                onErrorShown = {},
                onViewAllBudgets = {}
            )
}