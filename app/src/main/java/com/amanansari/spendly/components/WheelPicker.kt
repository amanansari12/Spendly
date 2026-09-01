package com.amanansari.spendly.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.ui.theme.Primary
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

/**
 * A single scrollable "wheel" column (used once for months, once for years).
 * Whichever row sits at the vertical center counts as selected. Rows further
 * from center shrink and fade automatically as the user drags.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 5,   // keep this odd so there's one true center row
    itemHeight: Dp = 40.dp
) {
    val paddingCount = visibleItemsCount / 2
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    // Jump straight to the currently selected item when the picker first appears
    // One effect, in order: jump to the initial selection first, and only
    // once that jump has actually landed, start listening for centering
    // changes. Doing these as two concurrent effects was the bug — the
    // listener could catch the wheel still at its untouched top position
    // before scrollToItem finished, and report that wrong spot as selected.
    LaunchedEffect(listState) {
        listState.scrollToItem(selectedIndex)

        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val viewportCenter = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f
                val centeredItem = layoutInfo.visibleItemsInfo.minByOrNull { info ->
                    abs((info.offset + info.size / 2f) - viewportCenter)
                }
                centeredItem?.let { info ->
                    val realIndex = (info.index - paddingCount).coerceIn(0, items.lastIndex)
                    if (realIndex != selectedIndex) onIndexSelected(realIndex)
                }
            }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = modifier.height(itemHeight * visibleItemsCount)
    ) {
        // Blank rows above the real items so the first real item can still reach center
        items(paddingCount) {
            Box(Modifier.height(itemHeight))
        }

        itemsIndexed(items) { index, label ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .graphicsLayer {
                        // Distance (in item-heights) from dead-center, recalculated on
                        // every scroll frame directly here — no recomposition needed.
                        val layoutInfo = listState.layoutInfo
                        val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                        val viewportCenter = viewportHeight / 2f
                        val extendedIndex = index + paddingCount
                        val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == extendedIndex }
                        val itemCenter = itemInfo?.let { it.offset + it.size / 2f }
                        val distance = itemCenter?.let { abs(it - viewportCenter) / itemHeightPx } ?: 2f

                        val scale = (1f - distance * 0.22f).coerceIn(0.72f, 1f)
                        scaleX = scale
                        scaleY = scale
                        alpha = (1f - distance * 0.35f).coerceIn(0.35f, 1f)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        // Blank rows below the real items so the last real item can still reach center
        items(paddingCount) {
            Box(Modifier.height(itemHeight))
        }
    }
}

/**
 * Pairs a month wheel and a year wheel side by side behind one shared
 * highlight bar. This is the piece that gets dropped straight into FilterByDate.
 */
@Composable
fun MonthYearWheelPicker(
    months: List<String>,        // e.g. "Jan".."Dec", already formatted
    years: List<String>,         // the selectable year range, as strings
    selectedMonthIndex: Int,     // 0-11
    selectedYearIndex: Int,      // index into `years`, not the raw year value
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    rowHeight: Dp = 40.dp
) {
    Box(contentAlignment = Alignment.Center) {

        // Decorative bar marking the "selected" row — purely visual, does not scroll
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight)
                .background(Primary.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelPicker(
                items = months,
                selectedIndex = selectedMonthIndex,
                onIndexSelected = onMonthChange,
                modifier = Modifier.width(110.dp),
                itemHeight = rowHeight
            )
            WheelPicker(
                items = years,
                selectedIndex = selectedYearIndex,
                onIndexSelected = onYearChange,
                modifier = Modifier.width(90.dp),
                itemHeight = rowHeight
            )
        }
    }
}