package com.amanansari.spendly.home.screen

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.transaction.screen.TransactionHistoryScreen
import com.amanansari.spendly.transaction.screen.TransactionHistoryScreenContentPreview
import kotlinx.coroutines.launch

// This screen is what actually sits under the "Home" nav route now.
// It doesn't show any UI of its own — it just holds the pager and
// decides which page (Home or Transaction History) is on screen.
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeOverviewScreen(
    onQuickSelect: (ExpIncCategory.ExpenseCategory) -> Unit,
    pagerState: PagerState,
    onViewAllBudgets : () -> Unit,
) {
    val scope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage != 0) {
        scope.launch { pagerState.animateScrollToPage(0) }
    }

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> HomeScreen(
                onQuickSelect = onQuickSelect,
                onViewAll = {
                    // "View All" no longer navigates — it just slides the pager over
                    scope.launch { pagerState.animateScrollToPage(1) }
                },
                onViewAllBudgets = onViewAllBudgets
            )
            1 -> {
                TransactionHistoryScreen(
                    onBack = {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    }
                )

            }
        }
    }
}