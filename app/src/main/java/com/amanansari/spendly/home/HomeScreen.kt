package com.amanansari.spendly.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.amanansari.spendly.R
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.ui.theme.SpendlyTheme

@Composable
fun HomeScreen() {

    val name = "Aman Ansari"


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(R.color.light_bg),
        topBar = {HomeTopBar(name)}

    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
//            verticalArrangement = Arrangement.Center,
//            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
            
        ) {

            val gradient = Brush.linearGradient(
                colors = listOf(
                    colorResource(R.color.primary_dark),
                    colorResource(R.color.primary),
                    colorResource(R.color.primary_light)
                )
            )

            // Income Information Card

            // We use a Box instead of a Card because making a Card's background transparent
            // (which is necessary to show our custom gradient) breaks its built-in drop shadow.
            // A Box with explicit .shadow() and .background() modifiers gives us perfect control.
            Box(
                modifier = Modifier
                    .width(350.dp) // Keeps your horizontal size
                    .shadow(
                        elevation = 25.dp,
                        shape = RoundedCornerShape(20.dp) // Creates the drop shadow
                    )
                    .clip(RoundedCornerShape(20.dp)) // Keeps the gradient inside the rounded corners
                    .background(gradient) // Applies the purple gradient
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Current Income Column Styling
                    Column {
                        Text(
                            text = "TOTAL SPENT THIS MONTH",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Text(
                            text = "$12,480.00",
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
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
                                color = Color.White
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = "Income Up",
                                    tint = colorResource(R.color.income_green),
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "+$28,000",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
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
                                    tint = colorResource(R.color.expense_red),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "-$28,000",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
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

            // Budget Information
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Budget Used",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "68%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(R.color.primary_dark)
                    )
                }

                // Linear Progress Indicator
                // TODO: Add Linear Progress Indicator
                LinearProgressIndicator(
                progress = { 0.68f },
                modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .height(9.dp)
                            .clip(RoundedCornerShape(50)),
                color = colorResource(R.color.primary_dark),
                trackColor = colorResource(R.color.light_nav_inactive),
                strokeCap = StrokeCap.Butt, // Natively rounds the ends of the progress bar
                    gapSize = 0.dp, // Removes the Material 3 gap
                    drawStopIndicator = {} // Removes the Material 3 stop indicator
                )
        
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    val remain = total - exp;
                    val exp = 8500
                    val remain = 4000
                    val total = 12000
                    Text(
                        text = buildAnnotatedString {
                            append("$$exp of ")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                                append("$$total")
                            }
                        }
                    )

                    Text(
                        text = "$$remain left",
                        color = colorResource(R.color.income_green),
                        fontWeight = FontWeight.Bold
                    )
                }


            }


        }
    }
}


// TopApp Bar
@Composable
fun HomeTopBar(name : String){

    Row(
        modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Good Morning")
            Text(text = "Hi, $name 👋",
                // Inherits size, line-height, and font family from your M3 Theme
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
        }

        val profileImg = false

        if(profileImg)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, colorResource(id = R.color.primary), CircleShape)
                            .clickable {
                                // TODO: Navigate to Profile Screen
                            }

            )
        else
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.light_nav_inactive))
                    .border(1.5.dp, colorResource(id = R.color.primary), CircleShape),

                contentAlignment = Alignment.Center
            ){
                Text(
                    text = getInitials(name),
                    fontWeight = FontWeight.Bold, // Using your bold text knowledge!
                    fontSize = 16.sp,
                    color = colorResource(R.color.dark_overlay)
                )
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
        HomeScreen()
    }
}