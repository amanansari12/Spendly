package com.amanansari.spendly.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.model.ExpIncCategory
import com.amanansari.spendly.model.allExpenseCategories
import com.amanansari.spendly.ui.theme.BrightGray
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightGray
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.ui.theme.PrimaryLight
import com.amanansari.spendly.ui.theme.SpendlyTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(onClickSheet : (ExpIncCategory.ExpenseCategory) -> Unit) {

    val name = "Aman Ansari"
    val isTransaction = false

    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val formattedDate = currentDate.format(formatter)

    var quickSelectedCategory by remember { mutableStateOf< ExpIncCategory.ExpenseCategory?>(null) }
    val context = LocalContext.current

    val unAllocatedAmount = remember { mutableIntStateOf(0) }
    val totalIncome = remember { mutableIntStateOf(30000) }
    val currency = remember { mutableStateOf("$") }
    val currentMonthIncome = remember { mutableIntStateOf(28000) }
    val surplus = remember { mutableStateOf(2500) }
    val exp = 8500
    val remain = 4000

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
            
        ) {
            //! Redesigned the Entire BalanceSummaryCard
            //? We use a Box instead of a Card because making a Card's background transparent
            //? (which is necessary to show our custom gradient) breaks its built-in drop shadow.
            //? A Box with explicit .shadow() and .background() modifiers gives us perfect control.
            item {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val gradient = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to PrimaryDark,
                            0.5f to Primary,
                            1.0f to PrimaryLight
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(
                            constraints.maxWidth.toFloat(),
                            constraints.maxHeight.toFloat() * 0.7f
                        )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Keeps your horizontal size
                            .clip(RoundedCornerShape(20.dp)) // Keeps the gradient inside the rounded corners
                            .background(gradient) // Applies the purple gradient
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            // Current Income Column Styling
                            Column {

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    Text(
                                        text = "TOTAL BALANCE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LightGray
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(40))
                                            .background(color = Color.White.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp)

                                    ){
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = BrightGray,
                                            modifier = Modifier.size(14.dp).padding(end = 2.dp)
                                        )

                                        Text(
                                            text = formattedDate,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrightGray,
                                        )
                                    }

                                }

                                Text(
                                    text = "$12,480.00",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = LightSurface,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Available to Spend this Month",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LightGray
                                )

                                if(true){

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(40))
                                            .background(color = Color.Red.copy(alpha = 0.14f))
                                            .padding(horizontal = 6.dp)

                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.WarningAmber,
                                            contentDescription = null,
                                            tint = Color(0xFFFFDB58),
                                            modifier = Modifier.size(14.dp).padding(end = 2.dp)
                                        )


                                        Text(
                                            text = "$${unAllocatedAmount.value} Unallocated - Tap to Assign",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightSurface,
                                            modifier = Modifier.clickable{
                                                Toast.makeText(context, "Unallocated Amount Clicked", Toast.LENGTH_SHORT).show()
                                            }

                                        )
                                    }

                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                                color = Color.White.copy(alpha = 0.2f) // Optional: makes the divider blend better with the gradient
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20))
                                    .background(Color.White.copy(alpha = 0.15f)).
                                    padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ){
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(4.dp)

                                    ){
                                        Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet,
                                            contentDescription = null,
                                            tint = IncomeGreen,
                                            modifier = Modifier.size(15.dp).padding(end = 2.dp)
                                        )

                                        Text(
                                            text = "TOTAL INCOME",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightSurface
                                        )
                                    }

                                    Text(
                                        text = "+${currency.value} ${totalIncome.value}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IncomeGreen
                                    )

                                }


                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Column(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20))
                                            .background(Color.White.copy(alpha = 0.15f))
                                            .padding(horizontal = 12.dp)
                                            .width(100.dp),

                                        verticalArrangement = Arrangement.Center

                                    ){
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                        ){
                                            Icon(
                                                imageVector = Icons.Default.Payments,
                                                contentDescription = null,
                                                tint = LightGray,
                                                modifier = Modifier.size(12.dp).padding(end = 2.dp)
                                            )

                                            Text(
                                                text = "THIS MONTH",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = LightGray
                                            )
                                        }

                                        Text(
                                            text = "${currency.value} ${currentMonthIncome.value}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightSurface
                                        )

                                        Text(
                                            text = "Salary Aug 1",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightGray
                                        )

                                    }

                                    VerticalDivider(
                                        thickness = 1.dp,
                                        modifier = Modifier.height(70.dp),
                                        color = Color.White.copy(alpha = 0.2f)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20))
                                            .background(Color.White.copy(alpha = 0.15f))
                                            .padding(horizontal = 12.dp)
                                            .width(100.dp),

                                        verticalArrangement = Arrangement.Center

                                    ){
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                        ){
                                            Icon(
                                                imageVector = Icons.Default.SouthWest,
                                                contentDescription = null,
                                                tint = IncomeGreen,
                                                modifier = Modifier.size(12.dp).padding(end = 2.dp)
                                            )

                                            Text(
                                                text = "CARRIED",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = IncomeGreen
                                            )
                                        }

                                        Text(
                                            text = "+ ${currency.value}${currentMonthIncome.intValue}",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IncomeGreen
                                        )

                                        Text(
                                            text = "July Surplus",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightGray
                                        )

                                    }
                                }


                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                                color = Color.White.copy(alpha = 0.2f) // Optional: makes the divider blend better with the gradient
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Income Column Styling
                                Column {
                                    Text(
                                        text = "SPENT",
                                        fontSize = 12.sp,
                                        color = LightGray
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = "Spent",
                                            tint = ExpenseRed,
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = "-$28,000",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = LightSurface
                                        )
                                    }
                                }

                                // Expense Column Styling
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "REMAINING",
                                        fontSize = 12.sp,
                                        color = LightGray
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = "Expense Down", // Updated content description
                                            tint = IncomeGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = "+$28,000",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IncomeGreen
                                        )
                                    }
                                }
                            }

                        }
                    }
                }


            }

            //? Budget Information Card
            item {
                Column(
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Budget Used",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "68%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryDark
                        )
                    }

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
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = LightTextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("$$exp ")
                                }
                                append(" of ")

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("$${totalIncome.value}")
                                }

                            },
                            fontSize = 14.sp,
                        )

                        Text(
                            text = "$$remain left",
                            fontSize = 14.sp,
                            color = IncomeGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }


                }
            }

            //? Daily Expense

            item {
                Column() {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Today's Expense",
                            fontWeight = FontWeight.Bold
                        )

                        TextButton(
                            onClick = { },
                        ) {
                            Text(
                                text = "View All",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                    }

                }
            }


            //? Daily Transactions Shown If Transaction is Done
            if(isTransaction){
                items(5) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = LightSurface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                // TODO: Make Changes in This,
                                // TODO: Make the Create Separate Code for Viewing Icon here
                                CategoryIconBox(category = ExpIncCategory.ExpenseCategory.Food, isSelected = false, onClick = {  })

                                Column {
                                    Text(
                                        text = "Burger King",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Food" + "12:45 PM",
                                        fontSize = 10.sp
                                    )
                                }
                            }


                            Text(
                                text = "-$450",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            }
            else{
                item{
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = LightSurface
                        )


                    ){
                        Column(
                            modifier = Modifier
                                .padding(25.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Primary.copy(alpha = 0.15f)),

                                contentAlignment = Alignment.Center

                            ){
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                    contentDescription = null,
                                    tint= PrimaryLight,
                                    modifier = Modifier.size(32.dp)

                                )
                            }

                            Text(
                                text = "Add Transaction",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Tap the + button below to log your first transaction of the month",
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp)
                            )

                        }

                    }
                }

            }


            //? Quick Add Button

            item{
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ){
                    Text(
                        text = "Quick Add",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(end = 16.dp)
                    ) {
                        items(allExpenseCategories){ category ->

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ){
                                CategoryIconBox(
                                    category = category,
                                    isSelected = category == quickSelectedCategory,
                                    onClick = {
                                        quickSelectedCategory = category
                                        onClickSheet(category)

                                    }
                                )

                                Text(
                                    text = category.title.split(" ").firstOrNull() ?: "",
                                    fontSize = 11.sp,
                                    color = LightTextSecondary
                                )
                            }

                        }
                    }
                }
            }

        }
    }


fun getInitials(name : String) : String {
    val initials = name
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { it[0].uppercaseChar() }
        .joinToString("")


    return initials
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SpendlyTheme {
        HomeScreen(onClickSheet = {})
    }
}