package com.amanansari.spendly.onBoarding.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.ui.theme.Primary

@Composable
fun OnboardingTopBar(currentStep: Int, totalStep: Int, onBackClick: (() -> Unit)? = null){

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Back",
                tint = Primary,
                modifier = Modifier.size(25.dp).clickable(enabled = onBackClick != null) { onBackClick?.invoke() }
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "Step $currentStep of $totalStep",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }

}