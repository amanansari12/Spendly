package com.amanansari.spendly.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amanansari.spendly.R
import com.amanansari.spendly.components.CategoryIconBox
import com.amanansari.spendly.components.SpendlyCategory
import com.amanansari.spendly.components.allCategories
import com.amanansari.spendly.navigation.screens
import com.amanansari.spendly.ui.theme.DarkOverlay
import com.amanansari.spendly.ui.theme.ExpenseRed
import com.amanansari.spendly.ui.theme.IncomeGreen
import com.amanansari.spendly.ui.theme.LightBg
import com.amanansari.spendly.ui.theme.LightBorder
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightSurface
import com.amanansari.spendly.ui.theme.LightTextPrimary
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.Primary
import com.amanansari.spendly.ui.theme.PrimaryDark
import com.amanansari.spendly.ui.theme.PrimaryLight
import com.amanansari.spendly.ui.theme.SpendlyTheme

@Composable
fun HomeScreen(onClickSheet : (SpendlyCategory) -> Unit) {

    val name = "Aman Ansari"

    val gradient = Brush.linearGradient(
        colors = listOf(
            PrimaryDark.copy(alpha = 1.0f),
            Primary.copy(alpha = 1.0f)

        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // Diagonal from top-left to bottom-right
    )

    var quickSelectedCategory by remember { mutableStateOf<SpendlyCategory?>(null) }
    val context = LocalContext.current

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
            
        ) {



            //! Income Information Card

            //? We use a Box instead of a Card because making a Card's background transparent
            //? (which is necessary to show our custom gradient) breaks its built-in drop shadow.
            //? A Box with explicit .shadow() and .background() modifiers gives us perfect control.
            item {
                Box(
                    modifier = Modifier
                        .width(350.dp) // Keeps your horizontal size
                        .clip(RoundedCornerShape(20.dp)) // Keeps the gradient inside the rounded corners
                        .background(gradient) // Applies the purple gradient
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Current Income Column Styling
                        Column {
                            Text(
                                text = "TOTAL SPENT THIS MONTH",
                                style = MaterialTheme.typography.labelMedium,
                                color = LightSurface
                            )
                            Text(
                                text = "$12,480.00",
                                style = MaterialTheme.typography.headlineLarge,
                                color = LightSurface,
                                fontWeight = FontWeight.Bold
                            )
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
                                    text = "INCOME",
                                    fontSize = 12.sp,
                                    color = LightSurface
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = "Income Up",
                                        tint = IncomeGreen,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "+$28,000",
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
                                    text = "EXPENSES",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = "Expense Down", // Updated content description
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
                        }

                        // Month Row View Buttons
                        Row(
                            modifier = Modifier
                                .padding(top = 18.dp) // Only top padding needed now since the Box shrink-wraps
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalButton(
                                onClick = { /*TODO : */ },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.15f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 1.dp)
                                    .height(32.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text("JUN")
                            }

                            FilledTonalButton(
                                onClick = { /*TODO : */ },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.15f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 1.dp)
                                    .height(32.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text("JUL")
                            }

                            FilledTonalButton(
                                onClick = { /*TODO : */ },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.15f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 1.dp)
                                    .height(32.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text("AUG")
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

                    // TODO: Add Linear Progress Indicator
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

                    val exp = 8500
                    val remain = 4000
                    val total = 12000

                    // ? How Much Budget Used and Left.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = buildAnnotatedString {
                                append("$$exp of ")

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("$$total")
                                }

                            },
                            fontSize = 14.sp
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

            val isTransaction = true
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
                                CategoryIconBox(category = SpendlyCategory.Food, isSelected = false, onClick = {  })

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
                        items(allCategories){ category ->

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



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SpendlyTheme {
        HomeScreen(onClickSheet = {})
    }
}