package com.amanansari.spendly.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.amanansari.spendly.R
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            
        ) {

            val gradient = Brush.linearGradient(
                colors = listOf(
                    colorResource(R.color.primary_dark),
                    colorResource(R.color.primary)
                )
            )

            Card(
                modifier = Modifier.size(width = 350.dp, height = 250.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )

            ) {
                Box(
                    modifier = Modifier.background(gradient).fillMaxSize()
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column {
                            Text(text = "TOTAL SPENT THIS MONTH",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Text(text = "$12,480.00",
                                fontSize = 35.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically

                        ) { }
                    }


                }
            }
        }
    }
}


// TopApp Bar
@Composable
fun HomeTopBar(name : String){

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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